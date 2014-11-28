package fr.cartooncraft.rush.events.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.cartooncraft.rush.RushPlugin;

public class LoginEvent implements Listener {
	
	RushPlugin plugin;
	
	public LoginEvent(RushPlugin p) {
		plugin = p;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(!RushPlugin.isGameRunning() || RushPlugin.isGameFinished())
			p.setGameMode(GameMode.ADVENTURE);
		if(RushPlugin.isGameRunning() && !RushPlugin.isGameFinished())
			if(!RushPlugin.isARushPlayer(e.getPlayer()))
				p.sendMessage(ChatColor.GRAY+"The game is already launched. But you can spectate!");
		if(RushPlugin.isGameFinished())
			p.teleport(RushPlugin.getPodiumLoc());
	}
	
}