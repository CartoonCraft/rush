package fr.cartooncraft.rush;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RushPlayer {
	
	private String thePlayerName;
	private String teamName;
	private int kills;
	private int deaths;
	private double ratio;
	
	public RushPlayer(Player p) {
		this.thePlayerName = p.getName();
	}
	
	public RushPlayer(String p) {
		this.thePlayerName = p;
	}
	
	public String getThePlayerName() {
		return thePlayerName;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(thePlayerName);
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

	public double getRatio() {
		return ratio;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public RushTeam getTeam() {
		return RushPlugin.getRushTeam(teamName);
	}
	
}