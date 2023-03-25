package com.github.zimablue.suits.internal.core.feature.display


import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.github.zimablue.suits.PoemSuits.plugin
import com.github.zimablue.suits.PoemSuits.suitDataManager
import com.github.zimablue.suits.PoemSuits.suitManager
import com.github.zimablue.suits.PoemSuits.suitSlotManager
import com.github.zimablue.suits.internal.core.feature.display.ItemDisplay.parse
import com.github.zimablue.suits.internal.core.suit.SuitData
import com.github.zimablue.suits.internal.manager.PSConfig.debug
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit

object ItemDisplay {
    private val modes = arrayOf(GameMode.SURVIVAL, GameMode.ADVENTURE)

    /*
    @SubscribeEvent(EventPriority.HIGH)
    fun e(event: PacketSendEvent) {
        if (event.isCancelled) return
        val packet = event.packet
        if (packet.name != "PacketPlayOutWindowItems") return
        debug{info("window itemEvent")}
        val player = event.player
        if (player.gameMode !in modes) return
        NMS.INSTANCE.computeCraftItems(
            player,
            packet.source
        ) { item ->
            debug{info("window itemParse")}
            item.parse(player)
        }
    }

    @SubscribeEvent(EventPriority.HIGH)
    fun e2(event: PacketSendEvent) {
        if (event.isCancelled) return
        val packet = event.packet
        if (packet.name != "PacketPlayOutSetSlot") return
        debug{info("slot itemEvent")}
        val player = event.player
        if (player.gameMode !in modes) return
        NMS.INSTANCE.computeCraftItem(
            player,
            packet.source
        ) { item ->
            debug{info("slot itemParse")}
            item.parse(player)
        }
    }
     */
    @Awake(LifeCycle.ENABLE)
    fun packetListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(object :
            PacketAdapter(
                plugin,
                ListenerPriority.NORMAL,
                PacketType.Play.Server.WINDOW_ITEMS,
                PacketType.Play.Server.SET_SLOT) {
            override fun onPacketSending(event: PacketEvent) {
                val player = event.player
                val gameMode = player.gameMode
                if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
                    if (event.packetType == PacketType.Play.Server.WINDOW_ITEMS) {
                        val items = event.packet.itemListModifier.read(0)
                        debug{info("Packet_Window_Items")}
                        items.forEach { itemStack ->
                            itemStack.parse(player, suitDataManager[player.uniqueId])
                        }
                        event.packet.itemListModifier.write(0, items)
                    } else {
                        /*AsyncSlotUpdate会延迟1tick后触发,也就是在setslot包发送后触发,
                        而发包lore必须在套装数据更新后更新才准确。
                        所以在监听setslot包时提前判断此次槽位修改是否为有效套装更新，
                        如果是，就在parse item的时候提前加上因AsyncSlotUpdate还未触发而未能更新的数据
                         */
                        val itemStack = event.packet.itemModifier.read(0)
                        val slotId = event.packet.integers.read(1)
                        debug{info(
                            "Packet_Set_Slot",
                            "slot: $slotId",
                        )}
                        itemStack.parse(player, SuitData(player),slotId)
                        event.packet.itemModifier.write(0, itemStack)
                    }
                }
            }
            override fun onPacketReceiving(event: PacketEvent) {}
        }
        )
    }
    private fun ItemStack.parse(player: Player,data: SuitData?=null,slotId: Int?=null) {
        val slot = suitSlotManager.getSlot(slotId?:-2)
        val suits = suitManager.checkItem(this,slot)
        debug{
            info(
                "ItemStack.parse> slot>> $slot",
                "ItemStack.parse> suits>> $suits",
            )
        }
        //若套装不在其生效位置，就不更改lore
        if (suits.isEmpty()) return
        suits.forEach {
            val amount = data?.get(it)?:0
            debug{
                info(
                    "ItemStack.parse> data>> $data",
                    "ItemStack.parse> amount>> $amount",
                )
            }
            it.parseItem(this, player, amount)
        }
    }
}