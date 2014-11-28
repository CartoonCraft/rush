package fr.cartooncraft.rush;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CCCommand {

	public String noPermission = ""+ChatColor.RESET+ChatColor.RED+"Sorry, you're not authorized to do this.";
	public String senderConsole = ""+ChatColor.RESET+ChatColor.RED+"Sorry, you're a console, you can't do this!";
	
	public static boolean isPlayer(CommandSender sender) {
		return(sender instanceof Player);
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isPlayer(String p) {
		return(Bukkit.getPlayer(p) != null);
	}
	
	public static Player getPlayer(CommandSender sender) {
		if(isPlayer(sender)) {
			return (Player)sender;
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static Player getPlayer(String sender) {
		if(Bukkit.getPlayer(sender) != null) {
			return Bukkit.getPlayer(sender);
		}
		else {
			return null;
		}
	}
	
	public static boolean areSamePlayers(Player p1, Player p2) {
		return(p1.getName() == p2.getName());
	}
	
	public static String getPlayerNotFoundSentence(String name) {
		return ChatColor.RED+"Can't find "+name+". Is him offline?";
	}
	
	public static String getPlayerName(Player p) {
		String name = "";
		if(p.isOp())
			name += ChatColor.RED;
		name += p.getName();
		return name;
	}
	
	@SuppressWarnings("deprecation")
	public static String getPlayerName(String playerName) {
		
		String name = "";
		
		if(playerName == "CONSOLE") {
			name = ChatColor.RED+"CONSOLE";
		}
		else {
			Player p = Bukkit.getPlayer(playerName);
			if(p.isOp())
				name += ChatColor.RED;
			name += p.getName();
		}
		return name;
	}
}
