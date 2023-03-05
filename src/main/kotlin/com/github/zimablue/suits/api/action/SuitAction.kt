package com.github.zimablue.suits.api.action

import com.github.zimablue.suits.PoemSuits.suitActionManager
import com.skillw.pouvoir.api.plugin.map.component.Keyable
import org.bukkit.configuration.ConfigurationSection
import taboolib.common5.clong

abstract class SuitAction(actionConfig: Map<String, Any?>) : Keyable<String> {

    val period = actionConfig.getOrDefault("period",1).clong

    abstract fun execute(context: MutableMap<String,Any>)

    //因为部分Action需要预加载脚本以提高性能，所以使用Class<>
    companion object {
        fun create(actionConfig: Map<String, Any?>): SuitAction? {
            return suitActionManager[actionConfig["type"].toString()]
                ?.getConstructor(Map::class.java)
                ?.newInstance(actionConfig)
        }
    }

}