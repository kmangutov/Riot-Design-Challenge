package com.riot_challenge.achievements;

import com.riot_challenge.Achievement;
import com.riot_challenge.Player;

public class BigWinnerAchievement extends Achievement
{
	
	
	public String getName()
	{
		return "Big Winner Award";
	}
	public boolean doesEarn(Player p) {
		return p.getHistoricalData("num_total_wins") >= 200; 
	}
	
}
