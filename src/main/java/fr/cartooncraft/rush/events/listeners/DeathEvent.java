package fr.cartooncraft.rush.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.cartooncraft.rush.RushPlayer;
import fr.cartooncraft.rush.RushPlugin;

public class DeathEvent implements Listener {
	
	RushPlugin plugin;
	
	public DeathEvent(RushPlugin p) {
		plugin = p;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		for(final RushPlayer rp : RushPlugin.getRushPlayers()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {

					rp.refreshDeaths();
					rp.refreshKills();
				}
			}, 2L);
		}
	}
	
}
