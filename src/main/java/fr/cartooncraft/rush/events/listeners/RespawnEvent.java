package fr.cartooncraft.rush.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.cartooncraft.rush.RushPlayer;
import fr.cartooncraft.rush.RushPlugin;
import fr.cartooncraft.rush.RushTeam;

public class RespawnEvent implements Listener {
	
	RushPlugin plugin;
	
	public RespawnEvent(RushPlugin p) {
		plugin = p;
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		if(RushPlugin.isGameFinished()) {
			e.getPlayer().teleport(RushPlugin.getPodiumLoc());
		}
		else {
			Player p = e.getPlayer();
			RushPlayer rp = RushPlugin.getRushPlayer(p);
			if(!e.isBedSpawn() && RushPlugin.isGameRunning() && !rp.isDisqualified()) {
				rp.setDisqualified(true);
				RushTeam rt = RushPlugin.getRushPlayer(p).getTeam();
				rt.setRemainingPlayers(rt.getRemainingPlayers() - 1);			
				switch(rt.getRemainingPlayers()) {
					case 1:
						Bukkit.broadcastMessage(rt.getColor()+e.getPlayer().getName()+ChatColor.GRAY+" is disqualified. "+rt.getRemainingPlayers()+" player remaining. Last one!");
						break;
					case 0:
						Bukkit.broadcastMessage(rt.getColor()+e.getPlayer().getName()+ChatColor.GRAY+" is disqualified. The "+rt.getColor()+rt.getName()+ChatColor.GRAY+" team is disqualified.");
						break;
					default:
						Bukkit.broadcastMessage(rt.getColor()+e.getPlayer().getName()+ChatColor.GRAY+" is disqualified. "+rt.getRemainingPlayers()+" players remaining.");
						break;
				}
				RushPlugin.aTeamDied(rt);
			}
		}
	}
	
}