package com.github.zimablue.suits.internal.manager

import com.github.zimablue.suits.PoemSuits
import com.github.zimablue.suits.PoemSuits.configManager
import com.github.zimablue.suits.api.manager.SuitSlotManager
import com.github.zimablue.suits.internal.manager.PSConfig.debug
import com.github.zimablue.suits.slotapi.slot.PlayerSlot
import com.github.zimablue.suits.slotapi.slot.impl.VanillaEquipSlot
import com.github.zimablue.suits.util.loremap.SuitOption
import com.github.zimablue.suits.util.loremap.SuitSlot
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.api.plugin.map.KeyMap
import com.skillw.pouvoir.util.toMap
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.info
import taboolib.common5.LoreMap
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir
import java.util.regex.Pattern

object SuitSlotManagerImpl : SuitSlotManager() {

    override val key: String = "SuitSlotManager"
    override val priority: Int = 1
    override val subPouvoir: SubPouvoir = PoemSuits
    val slotConfig = configManager["slot"].getConfigurationSection("slot")
    val VANILLASLOT = BaseMap<Int,PlayerSlot>().apply {
        register(-1,VanillaEquipSlot.MAINHAND)
        register(45,VanillaEquipSlot.OFFHAND)
        register(5,VanillaEquipSlot.HELMET)
        register(6,VanillaEquipSlot.CHESTPLATE)
        register(7,VanillaEquipSlot.LEGGINGS)
        register(8,VanillaEquipSlot.BOOTS)
    }
    val displayMap = mutableMapOf<String,String>()

    val loremap_enable
        get() = slotConfig?.getBoolean("lore.loremap.enable")?:false
    val loremapMatch
        get() = slotConfig?.getString("lore.loremap.pattern.match")
    val loremapValue
        get() = Pattern.compile(slotConfig?.getString("lore.loremap.pattern.value")?:":(.*)")

    val loreMap = LoreMap<SuitOption>().apply { put(loremapMatch,SuitSlot) }

    val nbt
        get() = slotConfig?.getBoolean("nbt")?:false
    val lore
        get() = slotConfig?.getBoolean("lore.enable")?:false

    override fun onEnable() {
        onReload()
    }
    override fun onReload() {
        debug{ info(
            "处理插件重载，更新槽位设置",
            "lore读取: $lore,loremap启用: $loremap_enable",
            "nbt读取: $nbt,"
        )}
        val slots = (slotConfig?.get("lore.key") as ConfigurationSection).toMap()
            .mapValues { (k,v) ->
                displayMap[v.toString()] = k
                v.toString()
            }
        putAll(slots)
    }

    override fun getSlot(slot: Int): PlayerSlot? {
        debug{
            info("getSlot> slot>> $slot")
        }
        return VANILLASLOT[slot]
    }

    override fun checkSlot(item: ItemStack, slot: PlayerSlot): Boolean {
        debug { info("SuitSlotCheck>>  slot>> $slot","item>> $item") }
        if(item.isAir) return false
        var result = false
        //nbt
        if (nbt) {
            if(item.getItemTag().getDeep("PoemSuits.slots")
                    ?.asList()?.any { it!=null&&it.asString()==slot.toString() } == true) {
                result = true
            }
        }
        //lore
        if(lore) {
            if(loremap_enable) {
                item.itemMeta?.lore?.let {lores ->
                    for(line in lores) {
                        // 在 LoreMap 中匹配属性
                        val matchResult = loreMap.getMatchResult(line)?:continue
                        // 如果没匹配到则处理下一条
                        if (matchResult.obj != SuitSlot) {
                            continue
                        }
                        // 取属性描述右边剩下没匹配完的,没匹配完的啥也没有，说明属性右边没数字，跳过
                        val remain = matchResult.remain ?: continue
                        debug{
                            info(
                                "remain: $remain",
                                "loremapValue: $loremapValue"
                            )
                        }
                        val matcher = loremapValue.matcher(remain)
                        if(matcher.find()) {
                            val matched = matcher.group(1)
                            debug {
                                info(
                                    "remain: $remain",
                                    "matched: $matched"
                                )
                            }
                            if(matched == get(slot.toString())) {
                                result = true
                                break
                            }
                        }
                    }
                    debug {
                        info(
                            "result: $result"
                        )
                    }
                }
            } else {
                if(item.itemMeta?.lore?.contains(get(slot.toString()))==true) result =true
            }
        }
        return result
    }

}