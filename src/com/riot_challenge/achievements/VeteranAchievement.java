package com.riot_challenge.achievements;

import com.riot_challenge.Achievement;
import com.riot_challenge.Player;

public class VeteranAchievement extends Achievement
{
	
	
	public String getName()
	{
		return "Veteran Award";
	}
	public boolean doesEarn(Player p) {
		return p.getHistoricalData("num_total_games") > 1000; 
	}
	
}
