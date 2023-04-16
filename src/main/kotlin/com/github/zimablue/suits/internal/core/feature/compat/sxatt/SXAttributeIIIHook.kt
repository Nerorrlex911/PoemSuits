package com.github.zimablue.suits.internal.core.feature.compat.sxatt

import ac.github.oa.api.OriginAttributeAPI
import com.github.zimablue.suits.api.attribute.AttributeProvider
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import github.saukiya.sxattribute.SXAttribute
import github.saukiya.sxattribute.api.SXAPI
import github.saukiya.sxattribute.data.attribute.SXAttributeData
import org.bukkit.entity.LivingEntity

@AutoRegister("github.saukiya.sxattribute.api.SXAPI")
object SXAttributeIIIHook : AttributeProvider {
    override val key: String = "SXAttributeIII"
    override fun addAttribute(entity: LivingEntity, source: String, attributes: List<String>) {
        OriginAttributeAPI.setExtra(entity.uniqueId, source, OriginAttributeAPI.loadList(attributes))
        val api = SXAttribute.getApi()
        api.getEntityData(entity).add(SXAttribute.getAttributeManager().loadListData(attributes))
    }

    override fun removeAttribute(entity: LivingEntity, source: String) {
        OriginAttributeAPI.removeExtra(entity.uniqueId, source)
        val api = SXAttribute.getApi()
    }
}