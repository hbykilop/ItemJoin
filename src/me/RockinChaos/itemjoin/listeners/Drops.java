package me.RockinChaos.itemjoin.listeners;

import java.util.List;
import java.util.ListIterator;

import me.RockinChaos.itemjoin.ItemJoin;
import me.RockinChaos.itemjoin.giveitems.utils.ItemMap;
import me.RockinChaos.itemjoin.handlers.ItemHandler;
import me.RockinChaos.itemjoin.handlers.PlayerHandler;
import me.RockinChaos.itemjoin.handlers.ServerHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class Drops implements Listener {
	

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		ItemStack item = event.getItemDrop().getItemStack();
		Player player = event.getPlayer();
		if (!ItemHandler.isAllowed(player, item, "self-drops")) {
			ItemMap itemMap = ItemHandler.getMappedItem(item, player.getWorld());
			if (!ItemHandler.isCraftingSlot(itemMap.getSlot())) {
				if (!ServerHandler.hasCombatUpdate() && InvClickSurvival.dropClick.get(PlayerHandler.getPlayerID(player)) != null && InvClickSurvival.dropClick.get(PlayerHandler.getPlayerID(player)) == true) {
					InvClickSurvival.droppedItem.put(PlayerHandler.getPlayerID(player), true);
					event.getItemDrop().remove();
				} else { if (PlayerHandler.isCreativeMode(player)) {
					ItemStack readd = new ItemStack(item);
					event.getItemDrop().remove();
					Bukkit.getScheduler().scheduleSyncDelayedTask(ItemJoin.getInstance(), (Runnable) new Runnable() {
						public void run() {
							player.closeInventory();
							player.getInventory().addItem(readd);
							PlayerHandler.delayUpdateInventory(player, 1L);
						}
					}, 1L);
				} else { event.setCancelled(true); } }
			}
		} else if (!ServerHandler.hasCombatUpdate() && PlayerHandler.isCreativeMode(player) && InvClickSurvival.dropClick.get(PlayerHandler.getPlayerID(player)) != null && InvClickSurvival.dropClick.get(PlayerHandler.getPlayerID(player)) == true && !ItemHandler.isAllowed(player, item, "self-drops")) {
			InvClickCreative.dropGlitch.put(PlayerHandler.getPlayerID(player), true);
		}
	}

	@EventHandler
	public void onDeathDrops(PlayerDeathEvent event) {
		List < ItemStack > drops = event.getDrops();
		ListIterator < ItemStack > litr = drops.listIterator();
		Player player = event.getEntity();
		ItemHandler.closeAnimations(player);
		while (litr.hasNext()) {
			ItemStack stack = litr.next();
			if (!ItemHandler.isAllowed(player, stack, "death-drops")) { litr.remove(); }
		}
	}
}