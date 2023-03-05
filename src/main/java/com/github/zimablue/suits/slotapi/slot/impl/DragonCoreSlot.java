package com.github.zimablue.suits.slotapi.slot.impl;

import com.github.zimablue.suits.slotapi.PlayerSlotAPI;
import com.github.zimablue.suits.slotapi.event.SlotUpdateEvent;
import com.github.zimablue.suits.slotapi.event.UpdateTrigger;
import com.github.zimablue.suits.slotapi.slot.PlayerSlot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class DragonCoreSlot extends PlayerSlot {

    private final String identifier;

    public DragonCoreSlot(String identifier) {
        super("DRAGON_CORE_" + identifier, "DRAGON_CORE_SLOT");
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean isAsyncSafe() {
        return true;
    }

    @Override
    public void get(Player player, Consumer<ItemStack> callback) {
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskAsynchronously(PlayerSlotAPI.getPlugin(), () -> {
                PlayerSlotAPI.getDragonCoreHook().getItemFromSlot(player, identifier, callback);
            });
        } else {
            PlayerSlotAPI.getDragonCoreHook().getItemFromSlot(player, identifier, callback);
        }
    }

    @Override
    public void set(Player player, ItemStack item, Consumer<Boolean> callback) {
        PlayerSlotAPI.getDragonCoreHook().setItemToSlot(player, identifier, item,
                result -> {
                    if (result) {
                        SlotUpdateEvent updateEvent = new SlotUpdateEvent(UpdateTrigger.SET, player, this, null, item);
                        updateEvent.setUpdateImmediately();
                        Bukkit.getPluginManager().callEvent(updateEvent);
                    }
                    callback.accept(result);
                });
    }
}
