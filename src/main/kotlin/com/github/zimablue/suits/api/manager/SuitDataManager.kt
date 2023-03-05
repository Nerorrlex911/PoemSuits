package com.github.zimablue.suits.api.manager

import com.github.zimablue.suits.internal.core.suit.Suit
import com.github.zimablue.suits.internal.core.suit.SuitData
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.KeyMap
import org.bukkit.entity.Player
import java.util.*

abstract class SuitDataManager: Manager, KeyMap<UUID, SuitData>() {
    abstract fun updateSuitData(player: Player)
    abstract fun addSuitData(player: Player, suits: Set<Suit>)
    abstract fun removeSuitData(player: Player, suits: Set<Suit>)
    abstract fun removeAllSuitData(player: Player)
    abstract fun endSuitEffect(player: Player)
    abstract fun startSuitEffect(player: Player)
}