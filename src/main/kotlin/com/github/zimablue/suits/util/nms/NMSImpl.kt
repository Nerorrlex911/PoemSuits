package com.github.zimablue.suits.util.nms

import taboolib.library.reflex.Reflex.Companion.getProperty
import net.minecraft.server.v1_16_R3.ItemStack
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack
import org.bukkit.entity.Player


class NMSImpl: NMS() {
    override fun computeCraftItems(player: Player, packet: Any, func: (org.bukkit.inventory.ItemStack) -> Unit) {
        packet.getProperty<List<ItemStack>>(itemsField)?.onEach {
            compute(it, func)
        }
    }

    private fun compute(item: ItemStack, func: (org.bukkit.inventory.ItemStack) -> Unit) {
        val bukkit = CraftItemStack.asBukkitCopy(item)
        func(bukkit)
        CraftItemStack.setItemMeta(item, bukkit.itemMeta)
    }


    override fun computeCraftItem(player: Player, packet: Any, func: (org.bukkit.inventory.ItemStack) -> Unit) {
        val item = packet.getProperty<ItemStack>(itemField) ?: return
        compute(item, func)
    }

}