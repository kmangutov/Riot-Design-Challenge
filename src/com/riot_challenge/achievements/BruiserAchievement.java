package com.riot_challenge.achievements;

import com.riot_challenge.Achievement;
import com.riot_challenge.Player;

public class BruiserAchievement extends Achievement
{
	
	
	public String getName()
	{
		return "Bruiser Award";
	}
	public boolean doesEarn(Player p) {
		return p.propertyAsInt("num_total_damage") > 500;

	}
	
}
