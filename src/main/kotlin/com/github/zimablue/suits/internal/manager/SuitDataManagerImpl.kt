package com.github.zimablue.suits.internal.manager

import com.github.zimablue.suits.api.manager.SuitDataManager
import com.github.zimablue.suits.PoemSuits
import com.github.zimablue.suits.internal.core.suit.Suit
import com.github.zimablue.suits.internal.core.suit.SuitData
import com.skillw.pouvoir.api.plugin.SubPouvoir
import org.bukkit.entity.Player
import taboolib.common.platform.function.info

object SuitDataManagerImpl: SuitDataManager() {
    override val key: String = "SuitDataManager"
    override val priority: Int = 5
    override val subPouvoir: SubPouvoir = PoemSuits

    override fun updateSuitData(player: Player) {
        endSuitEffect(player)
        register(player.uniqueId, SuitData(player))
        startSuitEffect(player)
    }

    override fun removeAllSuitData(player: Player) {
        endSuitEffect(player)
        remove(player.uniqueId)
    }

    override fun addSuitData(player: Player, suits: Set<Suit>) {
        if(suits.isEmpty()) return
        endSuitEffect(player)
        get(player.uniqueId)!!.add(suits)
        startSuitEffect(player)
    }

    override fun removeSuitData(player: Player, suits: Set<Suit>) {
        if(suits.isEmpty()) return
        endSuitEffect(player)
        get(player.uniqueId)!!.reduce(suits)
        startSuitEffect(player)
    }

    override fun endSuitEffect(player: Player) {
        get(player.uniqueId)?.forEach { (suit,amount) ->
            PSConfig.debug { info("End -> Suit: ${suit.key} Amount: $amount") }
            suit.onEnd(player,amount)
        }
    }

    override fun startSuitEffect(player: Player) {
        get(player.uniqueId)?.forEach { (suit,amount) ->
            PSConfig.debug { info("Start -> Suit: ${suit.key} Amount: $amount") }
            suit.onStart(player,amount)
        }
    }
    
}