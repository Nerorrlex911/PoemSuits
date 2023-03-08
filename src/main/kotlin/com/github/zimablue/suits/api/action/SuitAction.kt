package com.github.zimablue.suits.api.action

import com.github.zimablue.suits.PoemSuits.suitActionManager
import com.github.zimablue.suits.internal.manager.PSConfig.debug
import com.skillw.pouvoir.api.plugin.map.component.Keyable
import taboolib.common.platform.function.info
import taboolib.common5.Baffle
import taboolib.common5.clong
import java.util.concurrent.TimeUnit

abstract class SuitAction(actionConfig: Map<String, Any?>) : Keyable<String> {

    val period = actionConfig.getOrDefault("period",1).clong
    val buffer = Baffle.of(period*50,TimeUnit.MILLISECONDS)

    open fun execute(context: MutableMap<String,Any>) {
        debug{
            info("suitAction>${this.key} execute with context $context")
        }
    }

    //因为部分Action需要预加载脚本以提高性能，所以使用Class<>
    companion object {
        fun create(actionConfig: Map<String, Any?>): SuitAction? {
            return suitActionManager[actionConfig["type"].toString()]
                ?.getConstructor(Map::class.java)
                ?.newInstance(actionConfig)
        }
    }

}