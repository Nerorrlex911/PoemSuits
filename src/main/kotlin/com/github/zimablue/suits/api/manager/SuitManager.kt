package com.github.zimablue.suits.api.manager

import com.github.zimablue.suits.internal.core.suit.Suit
import com.github.zimablue.suits.slotapi.slot.PlayerSlot
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.KeyMap

import org.bukkit.inventory.ItemStack

abstract class SuitManager : Manager, KeyMap<String, Suit>() {

    abstract fun checkItem(item: ItemStack,slot: PlayerSlot?=null): MutableSet<Suit>


}