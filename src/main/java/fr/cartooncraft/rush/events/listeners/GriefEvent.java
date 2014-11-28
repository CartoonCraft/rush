package fr.cartooncraft.rush.events.listeners;

import org.bukkit.entity.Player;
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
	
	public static boolean cannotGrief(Player p) {
		if(p.isOp()) {
			return false;
		}
		if(RushPlugin.isGameRunning() && !RushPlugin.isGameFinished() && RushPlugin.isARushPlayer(p)) {
			if(RushPlugin.getRushPlayer(p).isDisqualified())
				return true;
			else
				return false;
		}
		else {
			return true;
		}
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent ev) {
		ev.setCancelled(cannotGrief(ev.getPlayer()));
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent ev) {
		ev.setCancelled(cannotGrief(ev.getPlayer()));
	}
	
}