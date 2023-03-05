package com.github.zimablue.suits.internal.core.suit

import com.github.zimablue.suits.PoemSuits.suitManager
import com.github.zimablue.suits.internal.manager.PSConfig.debug
import com.github.zimablue.suits.slotapi.PlayerSlotAPI
import org.bukkit.entity.Player
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.api.plugin.map.component.Keyable
import taboolib.common.platform.function.info
import taboolib.common.platform.service.PlatformExecutor
import taboolib.platform.util.isAir
import java.util.UUID

class SuitData(player: Player): Keyable<UUID>, BaseMap<Suit, Int>() {

    override val key: UUID = player.uniqueId
    val suitTasks = mutableMapOf<Suit,PlatformExecutor.PlatformTask>()

    init {
        PlayerSlotAPI.getAPI().slotMap.forEach { (id, slot) ->

            slot.get(player) {
                it?.let{
                    if (!it.isAir) add(suitManager.checkItem(it))
                }
                debug{ info("slotAPI id>$id,slot>$slot,item>$it")}
            }
        }
    }

    fun add(suits: Collection<Suit>) {
        suits.forEach { register(it,(get(it)?:0)+1) }
        debug{
            info(
                "addSuitData: $suits",
                "currentData: $this"
            )
        }
    }

    fun reduce(suits: Collection<Suit>) {
        suits.forEach {
            val amount = (get(it)?:0)-1
            register(it,if(amount > 0) amount else 0)
        }
        debug{
            info(
                "reduceSuitData: $suits",
                "currentData: $this"
            )
        }
    }
}