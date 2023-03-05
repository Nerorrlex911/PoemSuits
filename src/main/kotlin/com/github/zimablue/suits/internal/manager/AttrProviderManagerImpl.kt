package com.github.zimablue.suits.internal.manager

import com.github.zimablue.suits.PoemSuits
import com.github.zimablue.suits.api.manager.AttrProviderManager
import com.skillw.pouvoir.api.plugin.SubPouvoir

object AttrProviderManagerImpl: AttrProviderManager() {
    override val key: String = "AttrProviderManager"
    override val priority: Int = 4
    override val subPouvoir: SubPouvoir = PoemSuits
}