package fr.cartooncraft.rush;

import java.util.ArrayList;

import org.bukkit.ChatColor;

public class RushTeam {

	private ArrayList<String> playerList = new ArrayList<>();
	private int kills;
	private int deaths;
	private double ratio;
	private String name;
	private String displayName;
	private ChatColor color;
	private int remainingPlayers;
	private int totalPlayers;
	
	public RushTeam(String name, String displayName) {
		setName(name);
		setDisplayName(displayName);
		switch(displayName) {
			case "Blue":
				setColor(ChatColor.BLUE);
				break;
			case "Orange":
				setColor(ChatColor.GOLD);
				break;
			default:
				break;
		}
	}
	
	public int getKills() {
		return kills;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public ArrayList<String> getPlayerList() {
		return playerList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public int getRemainingPlayers() {
		return remainingPlayers;
	}

	public void setRemainingPlayers(int remainingPlayers) {
		this.remainingPlayers = remainingPlayers;
	}

	public int getTotalPlayers() {
		return totalPlayers;
	}

	public void setTotalPlayers(int totalPlayers) {
		this.totalPlayers = totalPlayers;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	
}