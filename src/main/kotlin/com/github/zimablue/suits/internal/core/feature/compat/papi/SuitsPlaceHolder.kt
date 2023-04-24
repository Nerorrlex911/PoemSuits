package com.github.zimablue.suits.internal.core.feature.compat.papi

import com.github.zimablue.suits.PoemSuits.suitDataManager
import com.github.zimablue.suits.PoemSuits.suitManager
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object SuitsPlaceHolder: PlaceholderExpansion {
    override val identifier: String
        get() = "poemsuits"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        return if(player != null) {
            val params = args.split("_")
            suitDataManager[player.uniqueId]?.get(suitManager[params[0]]!!).toString()
        } else "0"
    }

}