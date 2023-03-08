package com.github.zimablue.suits.internal.core.listener

import com.github.zimablue.suits.PoemSuits.suitDataManager
import com.github.zimablue.suits.PoemSuits.suitManager
import com.github.zimablue.suits.internal.manager.PSConfig.debug
import com.github.zimablue.suits.slotapi.event.AsyncSlotUpdateEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info

object InventoryListener {

    @SubscribeEvent(EventPriority.LOW)
    fun onEquipmentChange(e: AsyncSlotUpdateEvent) {
        val oldItem = e.oldItem
        val newItem = e.newItem
        val slot = e.slot ?: return
        //想尽可能不频繁调用onstart和onend，于是做了许多判断
        val old = suitManager.checkItem(oldItem,slot)
        val new = suitManager.checkItem(newItem,slot)
        //val oldSlotCheck = oldItem.isNotAir() && slot!=null && suitSlotManager.checkSlot(oldItem,slot)
        //val newSlotCheck = oldItem.isNotAir() && slot!=null && suitSlotManager.checkSlot(newItem,slot)
        for(suit1 in old) {
            for (suit2 in new) {
                if (suit1 == suit2) {
                    old.remove(suit1)
                    new.remove(suit1)
                }
            }
        }
        debug {
            info(
                "处理装备更新事件",
                "oldItem: $oldItem",
                "newItem: $newItem",
                "slot: $slot",
                "oldSuits> $old",
                "newSuits> $new"
            )
        }
        suitDataManager.removeSuitData(e.player, old)
        suitDataManager.addSuitData(e.player, new)

    }

    @SubscribeEvent(EventPriority.LOW)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        debug { info("处理玩家登录事件") }
        suitDataManager.updateSuitData(e.player)
    }

    @SubscribeEvent(EventPriority.LOW)
    fun onPlayerQuit(e: PlayerQuitEvent) {
        debug { info("处理玩家退出事件") }
        suitDataManager.removeAllSuitData(e.player)
    }

}