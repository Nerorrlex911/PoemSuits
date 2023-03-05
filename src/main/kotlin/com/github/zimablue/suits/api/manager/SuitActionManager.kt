package com.github.zimablue.suits.api.manager

import com.github.zimablue.suits.api.action.SuitAction
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.BaseMap

abstract class SuitActionManager : Manager, BaseMap<String, Class<out SuitAction>>() {
}