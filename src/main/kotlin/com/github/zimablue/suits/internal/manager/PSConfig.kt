package com.github.zimablue.suits.internal.manager

import com.github.zimablue.suits.PoemSuits
import com.skillw.pouvoir.api.manager.ConfigManager
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import com.github.zimablue.suits.slotapi.PlayerSlotAPI
import com.github.zimablue.suits.slotapi.slot.PlayerSlot
import com.github.zimablue.suits.slotapi.slot.impl.DragonCoreSlot
import com.github.zimablue.suits.slotapi.slot.impl.GermPluginSlot
import com.github.zimablue.suits.util.loremap.SuitOption
import com.github.zimablue.suits.util.loremap.SuitSlot
import com.skillw.pouvoir.Pouvoir
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.info
import taboolib.common5.LoreMap
import taboolib.module.lang.sendLang
import taboolib.module.nms.getItemTag
import java.io.File
import java.util.regex.Pattern

object PSConfig : ConfigManager(PoemSuits) {
    override val priority: Int = 0
    private val scripts = File(getDataFolder(), "scripts")
    override fun onLoad() {
        createIfNotExists("suit", "ExampleSuit.yml")
        createIfNotExists("scripts","Example.js")

    }

    override fun subReload() {
        Pouvoir.scriptManager.addScriptDir(scripts)

    }
    override fun onEnable() {
        onReload()
    }

    val debug
        get() = this["config"].getBoolean("debug")
    val vanilla
        get() = this["config"].getBoolean("vanilla")
    val germ_enable
        get() = this["config"].getBoolean("germ_plugin_slot.enable")
    val dragonCore_enable
        get() = this["config"].getBoolean("dragon_core_slot.enable")

    @Awake(LifeCycle.ENABLE)
    fun registerSlots() {

        val api = PlayerSlotAPI.getAPI()
        if(vanilla) api.registerVanilla()
        debugMessage("原版槽位注册成功")
        if(germ_enable && Bukkit.getPluginManager().isPluginEnabled("GermPlugin")) this["config"].getStringList("germ_plugin_slot.slots").forEach {
            api.registerSlot(
                GermPluginSlot(
                    it
                )
            )
            console().sendLang("germ-hook",it)
        }
        debugMessage("萌芽槽位注册成功")
        if(dragonCore_enable && Bukkit.getPluginManager().isPluginEnabled("DragonCore")) this["config"].getStringList("dragon_core_slot.slots").forEach {
            api.registerSlot(
                DragonCoreSlot(
                    it
                )
            )
            console().sendLang("dragon-hook",it)
        }
        debugMessage("龙核槽位注册成功")
        api.reload()

    }

    @JvmStatic
    fun debug(debug: () -> Unit) {
        if (PSConfig.debug) {
            debug.invoke()
        }
    }
    @JvmStatic
    fun debugMessage(vararg msg: String) {
        if (PSConfig.debug) {
            debug { info(msg) }
        }
    }
}