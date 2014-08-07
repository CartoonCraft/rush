package fr.cartooncraft.rush;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RushPlayer {
	
	private String thePlayerName;
	private String teamName;
	private int kills;
	private int deaths;
	private double ratio;
	private boolean isDisqualified;
	
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
	
	public void refreshKills() {
		this.kills = RushPlugin.getKillsObj().getScore(Bukkit.getOfflinePlayer(thePlayerName)).getScore();
	}
	
	public void refreshDeaths() {
		this.deaths = RushPlugin.getDeathsObj().getScore(Bukkit.getOfflinePlayer(thePlayerName)).getScore();
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
	
	public double setRatio() {
		if(deaths != 0) {
			this.ratio = (double)kills/deaths;
		}
		else {
			this.ratio = (double)kills/1;
		}
		return this.ratio;
	}
	
	public double getRatio() {
		return this.ratio;
	}
	
	public String getStringRatio() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setDecimalSeparatorAlwaysShown(true);
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		return df.format(this.ratio);
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

	public boolean isDisqualified() {
		return isDisqualified;
	}

	public void setDisqualified(boolean isDisqualified) {
		this.isDisqualified = isDisqualified;
	}
	
}