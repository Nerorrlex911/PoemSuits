package com.github.zimablue.suits.internal.manager

import com.github.zimablue.suits.api.manager.SuitDataManager
import com.github.zimablue.suits.PoemSuits
import com.github.zimablue.suits.internal.core.suit.Suit
import com.github.zimablue.suits.internal.core.suit.SuitData
import com.github.zimablue.suits.internal.manager.PSConfig.debug
import com.skillw.pouvoir.api.plugin.SubPouvoir
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.function.info
import java.util.*

object SuitDataManagerImpl: SuitDataManager() {
    override val key: String = "SuitDataManager"
    override val priority: Int = 5
    override val subPouvoir: SubPouvoir = PoemSuits

    override fun onEnable() {
        super.onEnable()
        onReload()
    }
    override fun onReload() {
        debug { info("处理插件重载，更新套装效果") }
        Bukkit.getOnlinePlayers().forEach { player ->
            updateSuitData(player)
        }
    }
    override fun onDisable() {
        debug { info("处理插件卸载，消除套装效果") }
        Bukkit.getOnlinePlayers().forEach { player ->
            endSuitEffect(player)
        }
    }

    override fun get(key: UUID): SuitData? {
        var data = super.get(key)
        if(data==null) {
            data = register(key, SuitData(Bukkit.getPlayer(key)!!))
            return data
        }
        return super.get(key)
    }
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