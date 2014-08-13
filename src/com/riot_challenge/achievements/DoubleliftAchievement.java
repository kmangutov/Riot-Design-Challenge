package com.riot_challenge.achievements;

import com.riot_challenge.Achievement;
import com.riot_challenge.Player;

public class DoubleliftAchievement extends Achievement
{
	//my custom achievement
	//rewarded to players who average more than 80 cs per 10 minutes
	
	public String getName()
	{
		return "Doublelift Award";
	}
	public boolean doesEarn(Player p) 
	{
		double cs = p.propertyAsDouble("num_creep_score");
		int timePlayedSec = p.propertyAsInt("num_time_played_sec");
		
		return (cs/timePlayedSec) >= ((double)80)/600;
	}
	
}
