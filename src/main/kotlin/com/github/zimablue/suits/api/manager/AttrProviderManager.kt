package com.github.zimablue.suits.api.manager

import com.github.zimablue.suits.api.attribute.AttributeProvider
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.KeyMap

abstract class AttrProviderManager : Manager,KeyMap<String,AttributeProvider>() {

}