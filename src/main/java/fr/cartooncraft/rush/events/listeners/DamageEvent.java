package fr.cartooncraft.rush.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.cartooncraft.rush.RushPlugin;

public class DamageEvent implements Listener {
	
	RushPlugin plugin;
	
	public DamageEvent(RushPlugin p) {
		plugin = p;
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!RushPlugin.isGameRunning() || RushPlugin.isGameFinished()) {
			e.setCancelled(true);
			return;
		}
	}
	
}