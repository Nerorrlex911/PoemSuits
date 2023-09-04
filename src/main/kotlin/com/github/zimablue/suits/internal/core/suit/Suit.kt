package com.github.zimablue.suits.internal.core.suit


import com.github.zimablue.suits.PoemSuits.attrProviderManager
import com.github.zimablue.suits.PoemSuits.suitDataManager
import com.github.zimablue.suits.internal.manager.PSConfig.debug
import com.github.zimablue.suits.util.ConfigUtil.getActions
import com.github.zimablue.suits.util.StringUtil.deleteLine
import com.github.zimablue.suits.util.StringUtil.joinLine
import com.github.zimablue.suits.util.StringUtil.splitLine
import com.skillw.asahi.api.AsahiAPI.analysis
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.pouvoir.api.plugin.map.component.Keyable
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.module.chat.colored
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir
import taboolib.platform.util.serializeToByteArray

class Suit(suitConfig: ConfigurationSection) : Keyable<String> {
    override val key: String = suitConfig.name
    val lore = suitConfig["check.lore"]
    val nbt = suitConfig["check.nbt"]
    val lore_enable = lore != "disable"
    val nbt_enable = nbt != "disable"
    val displayOrigin = suitConfig.getStringList("display.origin").joinLine()
    val displayReplace = suitConfig.getStringList("display.replace").joinLine()
    val attrProvider = attrProviderManager[suitConfig.getString("attributes.compat") ?: "AP"]
    val attributes = suitConfig.getStringList("attributes.list")
    val startActions = suitConfig.getActions("action.onStart")
    val timerActions = suitConfig.getActions("action.onTimer")
    val endActions = suitConfig.getActions("action.onEnd")
    fun checkItem(item: ItemStack) : Boolean {
        if(item.isAir) return false
        val suits = item.getItemTag().getDeep("PoemSuits.suits")?.asList()
        val itemLore = item.itemMeta?.lore
        val nbtMatch = if(nbt_enable) suits?.any { it.asString() == nbt }?:false else false
        val loreMatch = if(lore_enable) itemLore?.contains(lore)?:false else false
        debug {
            info(
                "suit.checkItem",
                "suitsNBT> $suits",
                "lores> $itemLore",
                "nbtMatch> $nbtMatch",
                "loreMatch> $loreMatch"
            )
        }
        return nbtMatch||loreMatch
    }
    fun parseItem(item: ItemStack, player: Player, amount: Int) {
        if (amount <= 0) return
        val context = AsahiContext.create(
            mutableMapOf(
                "player" to player,
                "suit_amount" to amount,
                "attributes" to attributes.joinLine()
            )
        )

        val meta = item.itemMeta?:return
        if(!meta.hasLore()) return
        val lore = meta.lore!!
        //因为要解析一遍attributes，再解析attributes里的asahi，所以调用两次analysis
        //analysis会吃掉换行符中的一个\
        val join = lore.joinLine()
            .replace(displayOrigin.colored(), displayReplace.analysis(context).analysis(context).colored())
        meta.lore = join.splitLine().toMutableList().deleteLine()
        item.itemMeta = meta
        debug{ info(
            "join: $join",
            "lore: ${meta.lore}",
            "item> $item"
        )}
    }

    private fun getAttributes(context: AsahiContext): List<String> {
        val attrParsed = attributes.map {
            it.analysis(context)
        }
        debug {
            info(attrParsed)
        }
        return attrParsed
    }

    fun onStart(player: Player, amount: Int) {
        if (amount <= 0) return
        val context = AsahiContext.create(mutableMapOf("player" to player, "suit_amount" to amount))
        debug{info("context: $context")}
        //attributes
        debug{ info("onStart attributes") }
        attrProvider?.addAttribute(player, "PoemSuits-$key", getAttributes(context))
        //onStart Actions
        debug{ info("onStart Actions> $startActions") }
        for (action in startActions) {
            action?.execute(context)
        }
        //onTimer Actions
        debug{ info("onTimer start Actions> > $timerActions") }
        val uuid = player.uniqueId
        suitDataManager[uuid]!!.suitTasks[this@Suit] =
            submit(async = true, period = 1) {
                for (action in timerActions) {
                    if (action == null) continue
                    if(action.buffer.hasNext(uuid.toString())) {
                        action.execute(context)
                    }
                }
            }

    }

    fun onEnd(player: Player, amount: Int) {

        if (amount <= 0) return
        val context = AsahiContext.create(mutableMapOf("player" to player, "suit_amount" to amount))
        debug{info("context: $context")}
        //attributes
        debug{ info("onEnd attributes") }
        attrProvider?.removeAttribute(player, "PoemSuits-$key")
        //onEnd Actions
        debug{ info("onEnd Actions> $endActions") }
        for (action in endActions) {
            action?.execute(context)
        }
        //onTimer Actions
        debug{ info("onTimer end Actions") }
        val uuid = player.uniqueId
        val task by lazy { suitDataManager[uuid]!!.suitTasks.remove(this@Suit) }
        debug{info("task-${this@Suit}> $task")}
        task!!.cancel()
        for (action in timerActions) {
            if (action == null) continue
            action.buffer.reset(uuid.toString())
        }
    }

    override fun toString(): String {
        return this.key
    }
}