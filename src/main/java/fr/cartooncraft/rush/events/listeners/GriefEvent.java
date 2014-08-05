package fr.cartooncraft.rush.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.cartooncraft.rush.RushPlugin;

public class GriefEvent implements Listener {
	
	RushPlugin plugin;
	
	public GriefEvent(RushPlugin p) {
		plugin = p;
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent ev) {
		// if (!this.p.isGameRunning() && !ev.getPlayer().isOp())
		if(!RushPlugin.isGameRunning() || RushPlugin.isGameFinished()) {
			if(!ev.getPlayer().isOp())
				ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent ev) {
		if(!RushPlugin.isGameRunning() || RushPlugin.isGameFinished())
			if(!ev.getPlayer().isOp())
				ev.setCancelled(true);
	}
	
}