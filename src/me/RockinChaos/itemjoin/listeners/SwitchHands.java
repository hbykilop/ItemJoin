package me.RockinChaos.itemjoin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import me.RockinChaos.itemjoin.giveitems.utils.ItemUtilities;
import me.RockinChaos.itemjoin.handlers.PlayerHandler;
import me.RockinChaos.itemjoin.handlers.ServerHandler;

public class SwitchHands implements Listener {
	
	@EventHandler
	private void onHandModify(PlayerSwapHandItemsEvent event) {
		if (ServerHandler.hasCombatUpdate()) {
			ItemStack offhand = event.getOffHandItem();
			ItemStack mainhand = event.getMainHandItem();
			Player player = event.getPlayer();
			if (!ItemUtilities.isAllowed(player, offhand, "inventory-modify")) {
				event.setCancelled(true);
				PlayerHandler.updateInventory(player);
			} else if (!ItemUtilities.isAllowed(player, mainhand, "inventory-modify")) {
				event.setCancelled(true);
				PlayerHandler.updateInventory(player);
			}
		}
	}
}