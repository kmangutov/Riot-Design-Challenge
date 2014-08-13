package com.riot_challenge.achievements;

import com.riot_challenge.Achievement;
import com.riot_challenge.Player;

public class SharpshooterAchievement extends Achievement
{
	

	
	
	public String getName()
	{
		return "Sharpshooter Award";
	}
	
	//land 75% of attacks if attacks >= 1
	public boolean doesEarn(Player p) {
		

		double attempted_attacks = p.propertyAsDouble("num_attempted_attacks");
		double hits = p.propertyAsDouble("num_hits");
		
		if(hits < 0)
			return false;
		
		if(attempted_attacks < 1)
			return false;
		
		return (hits/attempted_attacks)  >= 0.75;
	}
	
}
