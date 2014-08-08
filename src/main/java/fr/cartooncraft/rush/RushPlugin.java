package fr.cartooncraft.rush;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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

import fr.cartooncraft.rush.events.listeners.ChatEvent;
import fr.cartooncraft.rush.events.listeners.CraftEvent;
import fr.cartooncraft.rush.events.listeners.DamageByEntityEvent;
import fr.cartooncraft.rush.events.listeners.DamageEvent;
import fr.cartooncraft.rush.events.listeners.DeathEvent;
import fr.cartooncraft.rush.events.listeners.FoodDownEvent;
import fr.cartooncraft.rush.events.listeners.GriefEvent;
import fr.cartooncraft.rush.events.listeners.LoginEvent;
import fr.cartooncraft.rush.events.listeners.RespawnEvent;

public class RushPlugin extends JavaPlugin {
	
	private static Map<String, RushPlayer> rushPlayers = new HashMap<>();
	private static Map<String, RushTeam> rushTeams = new HashMap<>();
	private static Map<String, RushTeam> deadTeams = new HashMap<>();
	
	private static boolean isGameRunning = false;
	private static boolean isGameFinished = false;
	
	private static Location podiumLoc;
	private static Location firstSignLoc;
	private static Location secondSignLoc;
	private static Location thirdSignLoc;
	
	private static int hours = 0;
	private static int minutes = 0;
	private static int seconds = 0;
	private static String hoursString = "0";
	private static String minutesString = "00";
	private static String secondsString = "00";
	private NumberFormat formatter = new DecimalFormat("00");
	
	private static Objective deathsObj;
	private static Objective killsObj;

	public void onEnable() {
		getLogger().info("CC-Rush is loaded.");
		createRushTeam("Orange", "Orange");
		createRushTeam("Blue", "Blue");
		startSBRefresh();
		Bukkit.getPluginManager().registerEvents(new ChatEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new CraftEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new DamageByEntityEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new DamageEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new DeathEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new FoodDownEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new GriefEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new LoginEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new RespawnEvent(this), this);
		podiumLoc = new Location(Bukkit.getWorlds().get(0), -221.5, 76, 58.5);
		firstSignLoc = new Location(Bukkit.getWorlds().get(0), -218, 76, 62);
		secondSignLoc = new Location(Bukkit.getWorlds().get(0), -217, 75, 62);
		thirdSignLoc = new Location(Bukkit.getWorlds().get(0), -219, 75, 62);
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
				else {
					if(!isGameFinished) {
						seconds++;
						if(seconds == 60) {
							seconds = 0;
							minutes++;
						}
						if(minutes == 60) {
							hours++;
						}
						minutesString = formatter.format(minutes);
						secondsString = formatter.format(seconds);
					}
					setScoreboard();
				}
			}
			
		}, 20L, 20L);
	}
	
	public void setScoreboard() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			Scoreboard sb = getDefaultScoreboard();
			Random r = new Random();
			String sbobjname = "RUSH"+r.nextInt(10000000);
			int ping = ((CraftPlayer)p).getHandle().ping;
			Objective obj = sb.registerNewObjective(sbobjname, "dummy");
			obj = sb.getObjective(sbobjname);
			obj.setDisplayName(ChatColor.GREEN+"RUSH");
			obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY+"Ping : "+ChatColor.GREEN+ping)).setScore(10);
			obj.getScore(Bukkit.getOfflinePlayer(" ")).setScore(9);
			if(!isGameRunning()) {
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN+""+ChatColor.ITALIC+"Waiting...")).setScore(8);
			}
			else { // if game running
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN+""+ChatColor.ITALIC+"PLAYING !")).setScore(8);
				obj.getScore(Bukkit.getOfflinePlayer("  ")).setScore(7);
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.BLUE+""+rushTeams.get("Blue").getRemainingPlayers()+ChatColor.GRAY+"v"+ChatColor.GOLD+""+rushTeams.get("Orange").getRemainingPlayers())).setScore(6);
				if(isARushPlayer(p)) {
					RushPlayer rp = getRushPlayer(p);
					obj.getScore(Bukkit.getOfflinePlayer("   ")).setScore(5);
					obj.getScore(Bukkit.getOfflinePlayer(""+ChatColor.GRAY+"Kills : "+ChatColor.GREEN+rp.getKills())).setScore(4);
					obj.getScore(Bukkit.getOfflinePlayer(""+ChatColor.GRAY+"Deaths : "+ChatColor.GREEN+rp.getDeaths())).setScore(3);
					obj.getScore(Bukkit.getOfflinePlayer(""+ChatColor.GRAY+"Ratio : "+ChatColor.GREEN+rp.getStringRatio())).setScore(2);
				}
				obj.getScore(Bukkit.getOfflinePlayer("    ")).setScore(1);
				if(hours != 0) {
					obj.getScore(Bukkit.getOfflinePlayer(ChatColor.WHITE+hoursString+ChatColor.GRAY+":"+ChatColor.WHITE+minutesString+ChatColor.GRAY+":"+ChatColor.WHITE+secondsString)).setScore(0);
				}
				else {
					obj.getScore(Bukkit.getOfflinePlayer(ChatColor.WHITE+minutesString+ChatColor.GRAY+":"+ChatColor.WHITE+secondsString)).setScore(0);
				}
			}
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			p.setScoreboard(sb);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {	
		if(cmd.getName().equalsIgnoreCase("rush")) { // /rush
			if(args[0].equalsIgnoreCase("players")) { // /rush players
				if(args.length >= 3) { // /rush players <playerName>
					if(Bukkit.getPlayer(args[1]) != null) { // /rush players <playerName != null>
						Player p = Bukkit.getPlayer(args[1]);
						RushPlayer rp = getOrCreateRushPlayer(p);
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
								rushPlayers.remove(p.getName());
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
				for(Team t : sb.getTeams().toArray(new Team[0])) {
					t.unregister();
				}
				for(RushTeam rt : getRushTeams()) {
					Team t = sb.registerNewTeam(rt.getName());
					t.setAllowFriendlyFire(false);
					t.setPrefix(""+rt.getColor());
					t.setSuffix(""+ChatColor.RESET);
					t.setDisplayName(rt.getDisplayName());
					int nbPlayers = 0;
					for(String playerName : rt.getPlayerList().toArray(new String[0])) {
						t.addPlayer(Bukkit.getOfflinePlayer(playerName));
						nbPlayers++;
						getOrCreateRushPlayer(Bukkit.getPlayer(playerName));
					}
					rt.setTotalPlayers(nbPlayers);
					rt.setRemainingPlayers(nbPlayers);
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
				removeObj(sb, "rush_deaths");
				removeObj(sb, "rush_kills");
				deathsObj = sb.registerNewObjective("rush_deaths", "deathCount");
				killsObj = sb.registerNewObjective("rush_kills", "playerKillCount");
				isGameRunning = true;
			}
			else {
				sender.sendMessage(ChatColor.RED+"Nope! Usage: /rush <players|teams|start>");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("stats")) {
			if(args.length < 2) {
				if(CCCommand.isPlayer(args[0])) {
					Player p = CCCommand.getPlayer(args[0]);
					if(isARushPlayer(p)) {
						RushPlayer rp = getRushPlayer(p);
						String[] message = {"", "", "", ""};
						message[0] = ChatColor.GRAY+"Stats for "+rp.getTeam().getColor()+rp.getThePlayerName()+ChatColor.GRAY+":";
						message[1] = ChatColor.GRAY+"Kills: "+ChatColor.GREEN+rp.getKills();
						message[2] = ChatColor.GRAY+"Deaths: "+ChatColor.GREEN+rp.getDeaths();
						message[3] = ChatColor.GRAY+"Ratio: "+ChatColor.GREEN+rp.getStringRatio();
						sender.sendMessage(message);
					}
					else {
						sender.sendMessage(ChatColor.RED+"Hmmm... "+p.getName()+" isn't a rush player!");
					}
				}
				else {
					sender.sendMessage(CCCommand.getPlayerNotFoundSentence(args[0]));
				}
			}
			else {
				sender.sendMessage(ChatColor.RED+"Nope! Usage: /stats <player>");
			}
		}
		else {
			return false;
		}
		return true;
	}
	
	public void removeObj(Scoreboard sb, String objectiveName) {
		if(sb.getObjective(objectiveName) != null) {
			sb.getObjective(objectiveName).unregister();
		}
	}
	
	public static boolean isARushPlayer(String playerName) {
		if(getRushPlayer(playerName) != null) {
			return true;
		}
		return false;
	}
	
	public static boolean isARushPlayer(Player p) {
		return isARushPlayer(p.getName());
	}
	
	public static RushPlayer getRushPlayer(String playerName) {
		if(rushPlayers.get(playerName) != null)
			return rushPlayers.get(playerName);
		return null;
	}
	
	public static RushPlayer getRushPlayer(Player p) {
		return getRushPlayer(p.getName());
	}
	
	public Scoreboard addDefaultScoreboard(Scoreboard sb) {
		if(isGameRunning()) {
			for(RushTeam rt : getRushTeams()) {
				Team t = sb.registerNewTeam(rt.getName());
				t.setAllowFriendlyFire(false);
				t.setPrefix(""+rt.getColor());
				t.setSuffix(ChatColor.RESET+"");
				t.setDisplayName(rt.getDisplayName());
				for(String playerName : rt.getPlayerList().toArray(new String[0]))
					t.addPlayer(Bukkit.getOfflinePlayer(playerName));
			}
		}
		Objective HPobj = sb.registerNewObjective("HP", "dummy");
		HPobj.setDisplayName(ChatColor.RED+" \u2764");
		HPobj.setDisplaySlot(DisplaySlot.BELOW_NAME);
		for(Player p : Bukkit.getOnlinePlayers())
			HPobj.getScore(Bukkit.getOfflinePlayer(p.getName())).setScore((int)p.getHealth());
		
		Objective Pingobj = sb.registerNewObjective("PING", "dummy");
		Pingobj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		for(Player p : Bukkit.getOnlinePlayers())
			Pingobj.getScore(Bukkit.getOfflinePlayer(p.getName())).setScore(((CraftPlayer)p).getHandle().ping);
		
		return sb;
	}
	
	public void endOfTheGame(final RushTeam winnerTeam) {
		
		// Rank
		
		List<RushPlayer> rps = Arrays.asList(getRushPlayers());
		Collections.sort(rps, Collections.reverseOrder());
		
		// End Rank
		/*
		 * for(int i = 0; i < rps.size(); i++) {
			int i2 = i + 1;
			getLogger().info(i2+" :"+rps.get(i).getThePlayerName());
			}
		 * 
		 * 
		 * 
		 * 
		 */	
		
		Block firstSignBlock = firstSignLoc.getBlock();
		Block secondSignBlock = secondSignLoc.getBlock();
		Block thirdSignBlock = thirdSignLoc.getBlock();
		
		Sign firstSign = (Sign)firstSignBlock.getState();
		Sign secondSign = (Sign)secondSignBlock.getState();
		Sign thirdSign = (Sign)thirdSignBlock.getState();
		
		Bukkit.getLogger().info(firstSign.getLine(0));
		Bukkit.getLogger().info(secondSign.getLine(0));
		Bukkit.getLogger().info(thirdSign.getLine(0));
		
		firstSign.setLine(1, rps.get(0).getThePlayerName());
		switch(rps.get(0).getKills()) {
			case 0:
				firstSign.setLine(2, 0+" kill");
				break;
			case 1:
				firstSign.setLine(2, 1+" kill");
				break;
			default:
				firstSign.setLine(2, rps.get(0).getKills()+" kills");
		}
		switch(rps.get(0).getDeaths()) {
			case 0:
				secondSign.setLine(2, 0+" death");
				break;
			case 1:
				secondSign.setLine(2, 1+" death");
				break;
			default:
				secondSign.setLine(2, rps.get(0).getDeaths()+" deaths");
		}
		firstSign.update(true);
		
		if(rps.size() > 1) {
			secondSign.setLine(1, rps.get(1).getThePlayerName());
			switch(rps.get(1).getKills()) {
				case 0:
					secondSign.setLine(2, 0+" kill");
					break;
				case 1:
					secondSign.setLine(2, 1+" kill");
					break;
				default:
					secondSign.setLine(2, rps.get(1).getKills()+" kills");
			}
			switch(rps.get(1).getDeaths()) {
				case 0:
					secondSign.setLine(2, 0+" death");
					break;
				case 1:
					secondSign.setLine(2, 1+" death");
					break;
				default:
					secondSign.setLine(2, rps.get(1).getDeaths()+" deaths");
			}
			secondSign.update(true);
		}
		
		if(rps.size() > 2) {
			thirdSign.setLine(1, rps.get(2).getThePlayerName());
			switch(rps.get(2).getKills()) {
				case 0:
					secondSign.setLine(2, 0+" kill");
					break;
				case 1:
					secondSign.setLine(2, 1+" kill");
					break;
				default:
					secondSign.setLine(2, rps.get(2).getKills()+" kills");
			}
			switch(rps.get(2).getDeaths()) {
				case 0:
					secondSign.setLine(2, 0+" death");
					break;
				case 1:
					secondSign.setLine(2, 1+" death");
					break;
				default:
					secondSign.setLine(2, rps.get(2).getDeaths()+" deaths");
			}
			thirdSign.update(true);
		}
		
		Bukkit.broadcastMessage(""+ChatColor.RED+ChatColor.BOLD+"Congrats! The "+winnerTeam.getColor()+ChatColor.BOLD+winnerTeam.getName()+ChatColor.RED+ChatColor.BOLD+" team has won with "+winnerTeam.getRemainingPlayers()+" player(s) remaining, in "+hours+" hour(s), "+minutes+" minute(s) and "+seconds+" second(s)!");
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers())
					p.teleport(getPodiumLoc());				
			}
		}, 2L);
		setGameFinished(true);
	}
	
	public void aTeamDied(RushTeam rushTeam) {
		deadTeams.put(rushTeam.getName(), rushTeam);
		int remainingTeams = 0;
		RushTeam remainingTeam = new RushTeam("lol", "lol");
		ArrayList<RushTeam> remainingTeamsList = new ArrayList<>();
		for(RushTeam rt : getRushTeams()) {
			if(!(rt.getRemainingPlayers() == 0)) {
				remainingTeams++;
				remainingTeamsList.add(rt);
			}
		}
		if(remainingTeams == 1) {
			remainingTeam = remainingTeamsList.get(0);
			endOfTheGame(remainingTeam);
		}
	}
	
	public Scoreboard getDefaultScoreboard() {
		return addDefaultScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	public RushPlayer getOrCreateRushPlayer(String playerName) {
		if(!isARushPlayer(playerName))
			createRushPlayer(playerName);
		return rushPlayers.get(playerName);
	}
	
	public RushPlayer getOrCreateRushPlayer(Player p) {
		return getOrCreateRushPlayer(p.getName());
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
	
	public static void createRushPlayer(String playerName) {
		RushPlayer rp = new RushPlayer(playerName);
		rushPlayers.put(playerName, rp);
		return;
	}
	
	public static void createRushPlayer(Player p) {
		createRushPlayer(p.getName());
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

	public static Objective getDeathsObj() {
		return deathsObj;
	}

	public static Objective getKillsObj() {
		return killsObj;
	}

	public static boolean isGameFinished() {
		return isGameFinished;
	}

	public static void setGameFinished(boolean isGameFinished) {
		RushPlugin.isGameFinished = isGameFinished;
	}
	
	public static Location getPodiumLoc() {
		return podiumLoc;
	}
	
}