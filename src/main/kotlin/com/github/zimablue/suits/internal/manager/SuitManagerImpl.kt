package com.github.zimablue.suits.internal.manager

import com.github.zimablue.suits.api.manager.SuitManager
import com.github.zimablue.suits.internal.core.suit.Suit
import com.github.zimablue.suits.PoemSuits
import com.github.zimablue.suits.util.FileWatcher.unwatch
import com.github.zimablue.suits.util.FileWatcher.watch
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.util.listSubFiles
import com.skillw.pouvoir.util.loadYaml
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.getDataFolder
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir
import java.io.File

object SuitManagerImpl: SuitManager() {
    override val key: String = "SuitManager"
    override val priority: Int = 2
    override val subPouvoir: SubPouvoir = PoemSuits
    private val suits = BaseMap<File, HashSet<String>>()

    private fun reload(file: File) {
        suits[file]?.forEach(this::remove)
        suits.remove(file)
        file.loadYaml()?.apply {
            getKeys(false).forEach { key ->
                val section = this[key] as? ConfigurationSection? ?: return@forEach
                register(key, Suit(section))
            }
        }
    }

    override fun onEnable() {
        onReload()
    }

    override fun clear() {
        suits.keys.forEach { it.unwatch() }
        suits.clear()
        super.clear()
    }

    override fun onReload() {
        clear()
        File(getDataFolder(), "suit").listSubFiles()
            .filter { it.extension == "yml" }
            .forEach { it.watch(this::reload); reload(it) }
    }

    override fun checkItem(item: ItemStack): MutableSet<Suit> {
        val suitSet = mutableSetOf<Suit>()
        forEach { (_,suit) ->
            if(suit.checkItem(item)) suitSet.add(suit)
        }
        return suitSet
    }
}