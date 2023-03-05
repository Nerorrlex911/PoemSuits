package com.github.zimablue.suits.util

import com.github.zimablue.suits.api.action.SuitAction
import org.bukkit.configuration.ConfigurationSection

object ConfigUtil {
    fun ConfigurationSection.getSections(path: String): List<ConfigurationSection> {
        val sections = mutableListOf<ConfigurationSection>()
        var i = 0
        while (contains(path+i.toString())) {
            sections.add(i,getConfigurationSection(path+i.toString())!!)
            i += 1
        }
        return sections
    }
    fun ConfigurationSection.getActions(path: String) = getMapList(path).map { SuitAction.create(it as Map<String, Any?>) }
}