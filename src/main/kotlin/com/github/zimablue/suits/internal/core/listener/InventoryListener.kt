package com.github.zimablue.suits.internal.core.listener

import com.github.zimablue.suits.PoemSuits.suitDataManager
import com.github.zimablue.suits.PoemSuits.suitManager
import com.github.zimablue.suits.PoemSuits.suitSlotManager
import com.github.zimablue.suits.internal.manager.PSConfig.debug
import com.github.zimablue.suits.slotapi.event.AsyncSlotUpdateEvent
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
import taboolib.platform.util.isNotAir

object InventoryListener {

    @SubscribeEvent(EventPriority.HIGHEST)
    fun onEquipmentChange(e: AsyncSlotUpdateEvent) {
        val oldItem = e.oldItem
        val newItem = e.newItem
        //想尽可能不频繁调用onstart和onend，于是做了许多判断
        val old = suitManager.checkItem(oldItem)
        val new = suitManager.checkItem(newItem)
        val slot = e.slot
        val oldSlotCheck = oldItem.isNotAir() && slot!=null && suitSlotManager.checkSlot(oldItem,slot)
        val newSlotCheck = oldItem.isNotAir() && slot!=null && suitSlotManager.checkSlot(newItem,slot)
        if (oldSlotCheck) {
            for(suit1 in old) {
                if (newSlotCheck) {
                    for (suit2 in new) {
                        if (suit1 == suit2) {
                            old.remove(suit1)
                            new.remove(suit1)
                        }
                    }
                }
            }
        }
        debug {
            info(
                "处理装备更新事件",
                "oldItem: $oldItem",
                "newItem: $newItem",
                "slot: $slot",
                "oldSlotCheck: $oldSlotCheck",
                "newSlotCheck: $newSlotCheck",
                "oldSuits> $old",
                "newSuits> $new"
            )
        }
        if (oldSlotCheck) suitDataManager.removeSuitData(e.player, old)
        if (newSlotCheck) suitDataManager.addSuitData(e.player, new)

    }

    @SubscribeEvent(EventPriority.HIGHEST)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        debug { info("处理玩家登录事件") }
        suitDataManager.updateSuitData(e.player)
    }

    @SubscribeEvent(EventPriority.HIGHEST)
    fun onPlayerQuit(e: PlayerQuitEvent) {
        debug { info("处理玩家退出事件") }
        suitDataManager.removeAllSuitData(e.player)
    }

    fun onPluginDisable(){
        debug{ info("处理插件卸载") }
        Bukkit.getOnlinePlayers().forEach { player ->
            suitDataManager.endSuitEffect(player)
        }
    }
}