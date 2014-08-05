package fr.cartooncraft.rush;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class RushPlugin extends JavaPlugin {
	
	private static Map<String, RushPlayer> rushPlayers = new HashMap<>();
	private static Map<String, RushTeam> rushTeams = new HashMap<>();
	
	private static boolean isGameRunning = false;
	
	private static int hours = 0;
	private static int minutes = 0;
	private static int seconds = 0;

	public void onEnable() {
		getLogger().info("CC-Rush is loaded.");
		createRushTeam("Orange", "Orange");
		createRushTeam("Blue", "Blue");
		startSBRefresh();
	}
	
	public void onDisable() {
		getLogger().info("CC-Rush is unloaded.");
	}
	
	public void startSBRefresh() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				if(!isGameRunning())
					setScoreboard();
				getLogger().info("SCOREBOARD");
			}
			
		}, 20L, 20L);
	}
	
	public void setScoreboard() {
		if(!isGameRunning()) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
				CraftPlayer cp = (CraftPlayer)p;
				int ping = cp.getHandle().ping;
				Random r = new Random();
				String sbobjname = "RUSH"+r.nextInt(10000000);
				Objective obj = sb.registerNewObjective(sbobjname, "dummy");
				obj = sb.getObjective(sbobjname);
				obj.setDisplayName(ChatColor.GREEN+"RUSH");
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Ping : "+ChatColor.GREEN+ping)).setScore(2);
				obj.getScore(Bukkit.getOfflinePlayer(" ")).setScore(1);
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN+""+ChatColor.ITALIC+"Waiting...")).setScore(0);
				obj.setDisplaySlot(DisplaySlot.SIDEBAR);
				p.setScoreboard(sb);
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {	
		if(cmd.getName().equalsIgnoreCase("rush")) { // /rush
			if(args[0].equalsIgnoreCase("players")) { // /rush players
				if(args.length >= 3) { // /rush players <playerName>
					if(Bukkit.getPlayer(args[1]) != null) { // /rush players <playerName != null>
						Player p = Bukkit.getPlayer(args[1]);
						RushPlayer rp = getRushPlayer(p);
						if(args[2].equalsIgnoreCase("team")) { // /rush players <playerName != null> team
							if(args[3].equalsIgnoreCase("join")) { // /rush players <playerName != null> team join
								if(args[4].equalsIgnoreCase("Blue") || args[4].equalsIgnoreCase("1")) { // /rush players <playerName != null> team <Blue|1>
									RushTeam rushTeam = getRushTeam("Blue");
									rp.setTeamName(rushTeam.getName());
									rushTeam.getPlayerList().add(p.getName());
									sender.sendMessage(ChatColor.GRAY+CCCommand.getPlayerName(p)+ChatColor.GRAY+" is now in the "+rushTeam.getColor()+rushTeam.getDisplayName()+ChatColor.GRAY+" team.");
								}
								else if(args[4].equalsIgnoreCase("Orange") || args[4].equalsIgnoreCase("2")) { // /rush players <playerName != null> team <Orange|2>
									RushTeam rushTeam = getRushTeam("Orange");
									rp.setTeamName(rushTeam.getName());
									rushTeam.getPlayerList().add(p.getName());
									sender.sendMessage(ChatColor.GRAY+CCCommand.getPlayerName(p)+ChatColor.GRAY+" is now in the "+rushTeam.getColor()+rushTeam.getDisplayName()+ChatColor.GRAY+" team.");
								}
								else {
									sender.sendMessage(ChatColor.RED+"Nope! Usage: /rush players "+args[1]+" team join <Blue|Orange>");
								}
							}
							else if(args[3].equalsIgnoreCase("leave")) { // /rush players <playerName != null> team leave
								rp.setTeamName(null);
								sender.sendMessage(ChatColor.GRAY+CCCommand.getPlayerName(p)+ChatColor.GRAY+" has left his team.");
							}
							else if(args[3].equalsIgnoreCase("getTeam")) { // /rush players <playerName != null> team getTeam
								if(rp.getTeamName() == null)
									sender.sendMessage(ChatColor.GRAY+CCCommand.getPlayerName(p)+ChatColor.GRAY+" isn't in any team.");
								else
									sender.sendMessage(ChatColor.GRAY+CCCommand.getPlayerName(p)+ChatColor.GRAY+" is in the "+rp.getTeam().getColor()+rp.getTeam().getDisplayName()+ChatColor.GRAY+" team.");
							}
							else {
								sender.sendMessage(ChatColor.RED+"Nope! Usage: /rush players "+args[1]+" team <join|leave>");
							}
						}
						else {
							sender.sendMessage(ChatColor.RED+"Nope! Usage: /rush players "+args[1]+" <team>");
						}
					}
					else {
						sender.sendMessage(CCCommand.getPlayerNotFoundSentence(args[1]));
					}
				}
				else {
					sender.sendMessage(ChatColor.RED+"Nope! Usage: /rush players <playerName>");
				}
			}
			else if(args[0].equalsIgnoreCase("teams")) { // /rush teams
				if(args[1].equalsIgnoreCase("list")) { // /rush teams list
					sender.sendMessage(ChatColor.GRAY+"Available teams:");
					String message = ChatColor.GRAY+"";
					int i = 0;
					for(RushTeam rt : getRushTeams()) {
						if(i == 0) {
							message += rt.getColor()+rt.getDisplayName();
						}
						else {
							message += ChatColor.GRAY+", "+rt.getColor()+rt.getDisplayName();
						}
						i++;
					}
					sender.sendMessage(message);
				}
				else if(getRushTeam(args[1]) != null) {
					RushTeam rt = getRushTeam(args[1]);
					if(args[2].equalsIgnoreCase("list")) {
						String[] playerList = rt.getPlayerList().toArray(new String[0]);
						sender.sendMessage(ChatColor.GRAY+"Players in "+rt.getColor()+rt.getDisplayName()+ChatColor.GRAY+" team:");
						String message = ChatColor.GRAY+"";
						int i = 0;
						for(String playerName : playerList) {
							if(i == 0) {
								message += rt.getColor()+playerName;
							}
							else {
								message += ChatColor.GRAY+", "+rt.getColor()+playerName;
							}
						}
						sender.sendMessage(message);
					}
				}
				else {
					sender.sendMessage(ChatColor.RED+"Nope! Usage: /rush teams <list>");
				}
			}
			else if(args[0].equalsIgnoreCase("start")) {
				Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
				for(RushTeam rt : getRushTeams()) {
					Team t = sb.registerNewTeam(rt.getName());
					t.setAllowFriendlyFire(false);
					t.setPrefix(""+rt.getColor());
					t.setDisplayName(rt.getDisplayName());
					for(String playerName : rt.getPlayerList().toArray(new String[0])) {
						t.addPlayer(Bukkit.getOfflinePlayer(playerName));
					}
				}
				for(RushPlayer rp : getRushPlayers()) {
					rp.setDeaths(0);
					rp.setKills(0);
					Player p = rp.getPlayer();
					p.setGameMode(GameMode.SURVIVAL);
					p.setHealth(20);
					p.setFoodLevel(20);
					p.setExhaustion(5F);
					p.getInventory().clear();
					p.getInventory().setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
					p.setExp(0L+0F);
					p.setLevel(0);
					p.closeInventory();
					p.getActivePotionEffects().clear();
				}
			}
			else {
				sender.sendMessage(ChatColor.RED+"Nope! Usage: /rush <players|teams|start>");
			}
		}
		else {
			return false;
		}
		return true;
	}
	
	public RushPlayer getRushPlayer(Player p) {
		if(rushPlayers.get(p.getName()) != null)
			return rushPlayers.get(p.getName());
		RushPlayer rushPlayer = new RushPlayer(p);
		rushPlayers.put(p.getName(), rushPlayer);
		return(rushPlayer);
	}
	
	public static RushTeam getRushTeam(String name) {
		if(rushTeams.get(name) != null)
			return rushTeams.get(name);
		else
			return null;
	}
	
	public static void createRushTeam(String name, String displayName) {
		RushTeam rushTeam = new RushTeam(name, displayName);
		rushTeams.put(name, rushTeam);
		return;
	}
	
	public static void createRushTeam(RushTeam rt) {
		rushTeams.put(rt.getName(), rt);
		return;
	}
	
	public static RushTeam[] getRushTeams() {
		return rushTeams.values().toArray(new RushTeam[0]);
	}
	
	public static RushPlayer[] getRushPlayers() {
		return rushPlayers.values().toArray(new RushPlayer[0]);
	}
	
	public static boolean isGameRunning() {
		return isGameRunning;
	}

	public static int getHours() {
		return hours;
	}

	public static void setHours(int hours) {
		RushPlugin.hours = hours;
	}

	public static int getSeconds() {
		return seconds;
	}

	public static void setSeconds(int seconds) {
		RushPlugin.seconds = seconds;
	}

	public static int getMinutes() {
		return minutes;
	}

	public static void setMinutes(int minutes) {
		RushPlugin.minutes = minutes;
	}
	
}