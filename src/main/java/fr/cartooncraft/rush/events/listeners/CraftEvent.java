package fr.cartooncraft.rush.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import fr.cartooncraft.rush.RushPlugin;

public class CraftEvent implements Listener {
	
	RushPlugin plugin;
	
	public CraftEvent(RushPlugin p) {
		plugin = p;
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		if(!e.getWhoClicked().isOp())
			e.setCancelled(true);
	}
	
}