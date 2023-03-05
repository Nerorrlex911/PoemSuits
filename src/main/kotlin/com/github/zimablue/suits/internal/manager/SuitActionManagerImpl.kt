package com.github.zimablue.suits.internal.manager

import com.github.zimablue.suits.PoemSuits
import com.github.zimablue.suits.api.manager.SuitActionManager
import com.github.zimablue.suits.internal.core.action.AsahiAction
import com.github.zimablue.suits.internal.core.action.JSAction
import com.github.zimablue.suits.internal.core.action.KetherAction
import com.skillw.pouvoir.api.plugin.SubPouvoir
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object SuitActionManagerImpl : SuitActionManager() {
    override val key: String = "SuitActionManager"
    override val priority: Int = 1
    override val subPouvoir: SubPouvoir = PoemSuits

    override fun onLoad() {
        register("asahi",AsahiAction::class.java)
        register("js", JSAction::class.java)
        register("kether", KetherAction::class.java)
    }
}