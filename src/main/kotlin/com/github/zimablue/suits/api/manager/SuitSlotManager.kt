package com.github.zimablue.suits.api.manager

import com.github.zimablue.suits.slotapi.slot.PlayerSlot
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.BaseMap
import org.bukkit.inventory.ItemStack

abstract class SuitSlotManager : Manager,BaseMap<String,String>() {
    abstract fun checkSlot(item: ItemStack,slot: PlayerSlot) : Boolean
}