package com.github.zimablue.suits.internal.manager

import com.github.zimablue.suits.PoemSuits
import com.github.zimablue.suits.PoemSuits.configManager
import com.github.zimablue.suits.api.manager.SlotManager
import com.github.zimablue.suits.internal.manager.PSConfig.debug
import com.github.zimablue.suits.slotapi.slot.PlayerSlot
import com.github.zimablue.suits.util.loremap.SuitOption
import com.github.zimablue.suits.util.loremap.SuitSlot
import com.skillw.pouvoir.api.plugin.SubPouvoir
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.info
import taboolib.common5.LoreMap
import taboolib.module.nms.getItemTag
import java.util.regex.Pattern

object SlotManagerImpl : SlotManager() {

    override val key: String = "SlotManager"
    override val priority: Int = 4
    override val subPouvoir: SubPouvoir = PoemSuits

    val slotConfig = configManager["config"].getConfigurationSection("slot")

    val loremap_enable
        get() = slotConfig?.getBoolean("lore.loremap.enable")?:false
    val loremapMatch
        get() = slotConfig?.getString("lore.loremap.pattern.match")
    val loremapValue
        get() = Pattern.compile(slotConfig?.getString("lore.loremap.pattern.value")?:": <slot>")

    val loreMap = LoreMap<SuitOption>().apply { put(loremapMatch,SuitSlot) }

    val nbt
        get() = slotConfig?.getBoolean("nbt")?:false
    val lore
        get() = slotConfig?.getBoolean("lore.enable")?:false

    override fun onEnable() {
        debug{ info("SlotManager Enable")}
        onReload()
    }
    override fun onReload() {
        debug{ info("SlotManager Reload")}
        val slots = (slotConfig?.get("lore.key") as Map<*, *>)
            .mapKeys { (k,v) -> k.toString() }.mapValues { (k,v) -> v.toString() }
        putAll(slots)
    }
    override fun checkSlot(item: ItemStack, slot: PlayerSlot): Boolean {
        debug { info("slot>> $slot","item>> $item") }
        //nbt
        if (nbt) {
            return item.getItemTag().getDeep("PoemSuits.slots").asList().any { it.asString()==slot.toString() }
        }
        //lore
        if(lore) {
            if(loremap_enable) {
                item.itemMeta?.lore?.let {lores ->
                    for(line in lores) {
                        // 在 LoreMap 中匹配属性
                        val matchResult = loreMap.getMatchResult(line)
                        // 如果没匹配到则处理下一条
                        if (matchResult.obj != SuitSlot) {
                            continue;
                        }
                        // 取属性描述右边剩下没匹配完的,没匹配完的啥也没有，说明属性右边没数字，跳过
                        val remain = matchResult.remain ?: continue;
                        val matched = loremapValue.matcher(remain).group("slot")
                        debug {
                            info(
                                "remain: $remain",
                                "matched: $matched"
                            )
                        }
                        return matched == slot.toString()
                    }
                }
            } else {
                return item.itemMeta?.lore?.contains(get(slot.toString())) ?: false
            }
        }
        return false
    }
}