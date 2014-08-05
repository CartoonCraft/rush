package fr.cartooncraft.rush.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.cartooncraft.rush.RushPlayer;
import fr.cartooncraft.rush.RushPlugin;

public class DeathEvent implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		for(RushPlayer rp : RushPlugin.getRushPlayers()) {
			rp.refreshDeaths();
			rp.refreshKills();
		}
	}
	
}
