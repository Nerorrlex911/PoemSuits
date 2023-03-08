package com.github.zimablue.suits.internal.core.action

import com.github.zimablue.suits.api.action.SuitAction
import com.skillw.asahi.api.AsahiAPI.compile
import com.skillw.asahi.api.member.context.AsahiContext
import org.bukkit.configuration.ConfigurationSection
import taboolib.common.util.asList

class AsahiAction(actionConfig: Map<String, Any?>): SuitAction(actionConfig)  {
    override val key: String = "asahi"
    val script = actionConfig["run"].toString()
        .compile(*(actionConfig["namespaces"] as List<*>).map{it as String}.toTypedArray())
    override fun execute(context: MutableMap<String, Any>) {
        super.execute(context)
        script.apply {
            AsahiContext.create(context).execute()
        }
    }
}