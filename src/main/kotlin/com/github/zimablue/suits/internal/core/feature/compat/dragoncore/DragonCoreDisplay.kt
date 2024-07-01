package com.github.zimablue.suits.internal.core.feature.compat.dragoncore

import com.github.zimablue.suits.PoemSuits.suitDataManager
import com.github.zimablue.suits.PoemSuits.suitManager
import com.github.zimablue.suits.internal.manager.PSConfig.debug
import com.github.zimablue.suits.util.StringUtil.deleteLine
import com.github.zimablue.suits.util.StringUtil.joinLine
import com.github.zimablue.suits.util.StringUtil.longestLength
import com.github.zimablue.suits.util.StringUtil.splitLine
import com.skillw.asahi.api.AsahiAPI.analysis
import com.skillw.asahi.api.member.context.AsahiContext
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import eos.moe.dragoncore.network.PacketSender
import eos.moe.dragoncore.util.ItemUtil
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
import taboolib.module.chat.colored

object DragonCoreDisplay {
    @SubscribeEvent(EventPriority.HIGHEST)
    fun itemTip(e: CustomPacketEvent) {
        if(e.identifier != "PoemSuits"||e.data[0] != "check") return
        val item = ItemUtil.jsonToItem(e.data[1])
        val suits = suitManager.checkItem(item)
        if(suits.isEmpty()) return
        val placeHolder = mutableMapOf<String,String>()
        for(suit in suits) {
            val context = AsahiContext.create(
                mutableMapOf(
                    "player" to e.player,
                    "suit_amount" to (suitDataManager[e.player.uniqueId]?.get(suit) ?:0),
                    "attributes" to suit.attributes.joinLine()
                )
            )
            val suitLore = suit.displayReplace
                .analysis(context).analysis(context).colored().splitLine().toMutableList().deleteLine()
            suitLore.forEachIndexed { index, s ->
                placeHolder["PoemSuits_Info_${suit.key}_$index"] = s
            }
            placeHolder["PoemSuits_RowCount_${suit.key}"] = suitLore.size.toString()
            placeHolder["PoemSuits_LongestLength_${suit.key}"] = suitLore.longestLength().toString()
        }
        debug{
            info(
                "placeHolder",
                placeHolder
            )
        }
        PacketSender.sendSyncPlaceholder(e.player,placeHolder)
    }
}