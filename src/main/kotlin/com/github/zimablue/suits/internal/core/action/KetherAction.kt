package com.github.zimablue.suits.internal.core.action

import com.github.zimablue.suits.api.action.SuitAction
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.adaptCommandSender
import taboolib.library.kether.LocalizedException
import taboolib.module.kether.KetherShell
import taboolib.platform.util.onlinePlayers

class KetherAction(actionConfig: Map<String, Any?>): SuitAction(actionConfig)  {
    override val key: String = "kether"
    val action = actionConfig["run"].toString()
    override fun execute(context: MutableMap<String, Any>) {
        super.execute(context)
        try {
            KetherShell.eval(source = action, vars = KetherShell.VariableMap(context)) {
                sender = context["player"]?.let { adaptCommandSender(it) }

            }
        } catch (e: LocalizedException) {
            e.printStackTrace()
        }
    }
}