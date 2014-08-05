package fr.cartooncraft.rush.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.cartooncraft.rush.RushPlugin;

public class FoodDownEvent implements Listener {
	
	RushPlugin plugin;
	
	public FoodDownEvent(RushPlugin p) {
		plugin = p;
	}
	
	@EventHandler
	public void onFoodUpdate(FoodLevelChangeEvent ev) {
		if(!RushPlugin.isGameRunning() || RushPlugin.isGameFinished())
			ev.setCancelled(true);
	}
	
}