package com.github.zimablue.suits.util.nms

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsProxy

abstract class NMS {

    val itemsField by lazy {
        when (MinecraftVersion.major) {
            in 9..11 -> "c"
            else -> "b"
        }
    }
    val itemField by lazy {
        when (MinecraftVersion.major) {
            in 9..11 -> "f"
            else -> "c"
        }
    }

    abstract fun computeCraftItems(player: Player, packet: Any, func: (ItemStack) -> Unit)
    abstract fun computeCraftItem(player: Player, packet: Any, func: (ItemStack) -> Unit)

    companion object {
        @JvmStatic
        val INSTANCE by lazy {
            nmsProxy<NMS>()
        }
    }
}