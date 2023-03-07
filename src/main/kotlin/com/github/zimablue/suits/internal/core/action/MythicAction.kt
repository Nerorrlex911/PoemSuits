package com.github.zimablue.suits.internal.core.action

import com.github.zimablue.suits.api.action.SuitAction
import ink.ptms.um.Mythic
import org.bukkit.entity.Player
import taboolib.common5.cfloat

class MythicAction(actionConfig: Map<String, Any?>): SuitAction(actionConfig) {

    override val key = "mythic"

    val skillId = actionConfig["skillId"].toString()

    override fun execute(context: MutableMap<String, Any>) {
        Mythic.API.castSkill(caster=context["player"] as Player, skillName = skillId, power = context["suit_amount"].cfloat)
    }
}