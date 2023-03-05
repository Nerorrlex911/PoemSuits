package com.github.zimablue.suits.internal.core.action

import com.github.zimablue.suits.api.action.SuitAction
import com.skillw.pouvoir.Pouvoir.scriptManager
import org.bukkit.configuration.ConfigurationSection

class JSAction(actionConfig: Map<String, Any?>): SuitAction(actionConfig) {
    override val key = "js"
    val script = actionConfig["run"].toString()
    override fun execute(context: MutableMap<String, Any>) {
        scriptManager.invoke<Any?>("/plugins/PoemSuits/scripts/$script",context)
    }
}