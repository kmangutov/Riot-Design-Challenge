package com.riot_challenge;
import java.util.*;


/*
 * this class stores post game information for each player in 'dict'
 * and historical daya it loads from a fake server in 'historicalData'
 * 
 * also keeps track of achievements earned
 */


public class Player 
{
	//store post-game data
	public Map<String, String> dict = new HashMap<String, String>();
	public String summonerName;
	
	//historical data
	transient public Map<String, Integer> historicalData = new HashMap<String, Integer>(); 
	
	
	
	//transient tag makes Gson ignore it when serializing
	//list of achievements earned by this player after the game
	transient List<Achievement> achievementsEarned = new ArrayList<Achievement>();

	
	public Player()
	{

	}
	
	//add achievment to achievments list
	public void addAchievment(Achievement a)
	{
		achievementsEarned.add(a);
	}
	public List<Achievement> getAchievements()
	{
		return achievementsEarned;
	}
	
	public boolean hasAchievement(Achievement needle)
	{
		for(Achievement a : achievementsEarned)
		{
			if(a.getName().equals(needle.getName()))
				return true;
		}
		return false;
	}
	
	//make return this so i can chain adding properties in test
	public Player propertyPut(String key, String value)
	{
		dict.put(key, value);
		return this;
	}
	
	//access properties (post game statistics)
	public String property(String key)
	{
		try{
			if(dict.get(key) != null)
			{
				
				return dict.get(key);
			}
			System.err.println("PROPERTY " + key + " WAS NULL!!");
			return "0";
		}catch(Exception e)
		{
			System.err.println("PROPERTY " + key + " DIDNT EXIST!!");
			e.printStackTrace();
			
			return "0";
		}
	}
	public int propertyAsInt(String key)
	{
		return Integer.parseInt(property(key));
	}
	public double propertyAsDouble(String key)
	{
		return Double.parseDouble(property(key));
	}
	
	
	
	//accessing historical data
	private void putHistoricalData(String key, int val)
	{
		historicalData.put(key, val);
	}
	public int getHistoricalData(String key)
	{
		try
		{
			return historicalData.get(key);
		}catch(Exception e)
		{
			System.err.println("PROPERTY " + key + " DIDNT EXIST!!");
			e.printStackTrace();
			
			return 0;
		}
	}
	
	
	
	//load historical data for this player
	public void loadHistoricalData(DatabaseConnection dc)
	{
		System.out.println("Loading historical data for '" + summonerName + "'...");
		
			
		putHistoricalData("num_total_wins", dc.getTotalWins(summonerName));
		putHistoricalData("num_total_games", dc.getTotalGames(summonerName));
		
		//in real product we would load this from a database... for now we'll work with this
	}
}
