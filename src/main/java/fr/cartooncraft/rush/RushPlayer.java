package fr.cartooncraft.rush;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RushPlayer implements Comparable<RushPlayer> {
	
	private UUID thePlayerUUID;
	private String thePlayerName;
	private String teamName;
	private int kills;
	private int deaths;
	private double ratio;
	private boolean isDisqualified;
	
	public RushPlayer(Player p) {
		this.thePlayerUUID = p.getUniqueId();
	}
	
	@SuppressWarnings("deprecation")
	public RushPlayer(String p) {
		this.thePlayerName = Bukkit.getPlayer(p).getName();
		this.thePlayerUUID = Bukkit.getPlayer(p).getUniqueId();
	}

	public RushPlayer(UUID u) {
		this.thePlayerUUID = u;
		this.thePlayerName = Bukkit.getPlayer(u).getName();
	}
	
	@SuppressWarnings("deprecation")
	public Player getPlayer() {
		return Bukkit.getPlayer(this.thePlayerName);
	}
	
	public String getThePlayerName() {
		return this.thePlayerName;
	}

	public int getKills() {
		return kills;
	}
	
	public void refreshKills() {
		this.kills = RushPlugin.getKillsObj().getScore(this.getThePlayerName()).getScore();
	}
	
	public void refreshDeaths() {
		this.deaths = RushPlugin.getDeathsObj().getScore(this.getThePlayerName()).getScore();
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
	
	public void setRatio() {
		if(deaths != 0) {
			this.ratio = (double)kills/deaths;
		}
		else {
			this.ratio = (double)kills/1;
		}
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

	@Override
	public int compareTo(RushPlayer o) {
		int killsA = kills;
		int killsB = o.getKills();
		if(killsA == killsB) {
			int deathsA = deaths;
			int deathsB = o.getDeaths();
			if(deathsA == deathsB)
				return 0;
			else if(deathsA > deathsB)
				return -1;
			else
				return 1;
		}
		else if(killsA > killsB)
			return 1;
		else
			return -1;
		//return kills - o.getKills();
	}
	
	public String toString() {
		System.out.println(thePlayerUUID.toString());
		//System.out.println(this.getThePlayerName());
		//return "playerName:"+this.getThePlayerName()+"; teamName:"+teamName+"; kills:"+kills+"; deaths: "+deaths+"; ratio:"+ratio+"; isDisqualified"+isDisqualified;
		return "teamName:"+teamName+"; kills:"+kills+"; deaths: "+deaths+"; ratio:"+ratio+"; isDisqualified: "+isDisqualified;
	}
	
}