package com.github.zimablue.suits.internal.core.feature.compat.originatt

import ac.github.oa.api.OriginAttributeAPI
import com.github.zimablue.suits.api.attribute.AttributeProvider

import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.entity.LivingEntity

@AutoRegister("ac.github.oa.api.OriginAttributeAPI")
object OriginAttributeHook : AttributeProvider {
    override val key: String = "OriginAttribute"
    override fun addAttribute(entity: LivingEntity, source: String, attributes: List<String>) {
        OriginAttributeAPI.setExtra(entity.uniqueId, source, OriginAttributeAPI.loadList(attributes))
    }

    override fun removeAttribute(entity: LivingEntity, source: String) {
        OriginAttributeAPI.removeExtra(entity.uniqueId, source)
    }
}