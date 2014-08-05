package fr.cartooncraft.rush.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.cartooncraft.rush.RushPlugin;

public class ChatEvent implements Listener {
	
	RushPlugin plugin;
	
	public ChatEvent(RushPlugin p) {
		plugin = p;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent ev) {
		ev.setCancelled(true);
		Player p = ev.getPlayer();
		if(RushPlugin.isARushPlayer(p)) {
			if(p.isOp())
				Bukkit.broadcastMessage(ChatColor.RED+"<"+ChatColor.RESET+RushPlugin.getRushPlayer(p).getTeam().getColor()+p.getName()+ChatColor.RED+"> "+ChatColor.RESET+ChatColor.WHITE+ev.getMessage());
			else
				Bukkit.broadcastMessage(ChatColor.WHITE+"<"+ChatColor.RESET+RushPlugin.getRushPlayer(p).getTeam().getColor()+p.getName()+ChatColor.WHITE+"> "+ChatColor.RESET+ChatColor.WHITE+ev.getMessage());
		}
		else {
			if(p.isOp())
				Bukkit.broadcastMessage(ChatColor.RED+"<"+ChatColor.RESET+ChatColor.GREEN+p.getName()+ChatColor.RED+"> "+ChatColor.RESET+ChatColor.WHITE+ev.getMessage());
			else
				Bukkit.broadcastMessage(ChatColor.WHITE+"<"+ChatColor.RESET+ChatColor.GREEN+p.getName()+ChatColor.WHITE+"> "+ChatColor.RESET+ChatColor.WHITE+ev.getMessage());
		}
	}
	
}