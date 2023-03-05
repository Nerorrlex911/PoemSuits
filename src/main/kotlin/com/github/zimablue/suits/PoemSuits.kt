package com.github.zimablue.suits

import com.github.zimablue.suits.api.manager.*
import com.github.zimablue.suits.internal.core.listener.InventoryListener
import com.github.zimablue.suits.internal.manager.PSConfig
import com.github.zimablue.suits.internal.manager.SuitSlotManagerImpl
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.annotation.PouManager
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.platform.BukkitPlugin

object PoemSuits : Plugin(), SubPouvoir {

    override val key: String = "PoemSuits"
    override val plugin: JavaPlugin by lazy {
        BukkitPlugin.getInstance()
    }

    /** Config */
    @Config("config.yml")
    lateinit var config: ConfigFile

    @Config("slot.yml")
    lateinit var slot: ConfigFile

    /** Managers */

    override lateinit var managerData: ManagerData

    @JvmStatic
    @PouManager
    lateinit var configManager: PSConfig

    @JvmStatic
    @PouManager
    lateinit var suitManager: SuitManager

    @JvmStatic
    @PouManager
    lateinit var suitDataManager: SuitDataManager

    @JvmStatic
    @PouManager
    lateinit var suitActionManager: SuitActionManager

    @JvmStatic
    @PouManager
    lateinit var attrProviderManager: AttrProviderManager

    @JvmStatic
    @PouManager
    lateinit var suitSlotManager: SuitSlotManager

    override fun onLoad() {
        load()
    }

    override fun onEnable() {
        enable()
    }

    override fun onActive() {
        active()
    }

    override fun onDisable() {
        InventoryListener.onPluginDisable()
        disable()
    }
}