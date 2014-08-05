package fr.cartooncraft.rush.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.cartooncraft.rush.RushPlugin;

public class DamageByEntityEvent implements Listener {
	
	RushPlugin plugin;
	
	public DamageByEntityEvent(RushPlugin p) {
		plugin = p;
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(!RushPlugin.isGameRunning() || RushPlugin.isGameFinished()) {
			e.setCancelled(true);
			return;
		}
		if(e.getDamager() instanceof Player) {
			Player p = (Player)e.getDamager();
			if(RushPlugin.isARushPlayer(p.getName())) {
				if(e.getEntity() instanceof Player) {
					if(!RushPlugin.isARushPlayer(((Player)e.getEntity()))) {
						e.setCancelled(true);
					}
				}
			}
			else {
				e.setCancelled(true);
			}
		}
	}
	
}