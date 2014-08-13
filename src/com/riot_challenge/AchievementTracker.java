package com.riot_challenge;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;



/*
 * this is the entry point of my program
 */

public class AchievementTracker 
{
	Game game;
	public ArrayList<Player> players; //for testing
	
	public AchievementTracker(String jsonFromServer, DatabaseConnection dc)
	{
		//use google open-source Json library "Gson" (https://sites.google.com/site/gson/Home)
		//to populate a Game class
		Gson gson = new Gson();
		
		game = gson.fromJson(jsonFromServer, Game.class);

		//get all the players in the game
		players = game.redTeam;
		players.addAll(game.blueTeam);

		
		
		
		
		
		//initialize instance of AchievementManager, the class used to manage achievements
		//the public constructor loads achievements
		AchievementManager am = new AchievementManager();
		
		
		
		
		for(Player p : players)
		{
			//load historical data from servers
			p.loadHistoricalData(dc);
			
			//process achievements for this player
			am.processAchievments(p);
		}

		
		
		
		//now we want to print our output
		
		for(Player p : players)
		{
			System.out.println(p.summonerName + " has earned:");
			
			if(p.achievementsEarned.size() == 0)
			{
				System.out.println("\tNONE!");
				continue;
			}
			
			
			for(Achievement a : p.achievementsEarned)
			{
				System.out.println("\t" + a.getName());
			}
		}
	}
	
	public List<Player> getAllPlayers()
	{
		return players;
	}
	public List<Achievement> getSummonerAchievs(String name)
	{
		for(Player p : players)
		{
			if(p.summonerName.equals(name))
				return p.getAchievements();
		}
		return null;
	}
	

	
	public static void main(String[] args)
	{
		//System.out.println(generateFakeJson());
		
		
		//this is a json string summing up post-game statistics we get from the awesome Riot servers
		String jsonFromServer;
		
		if(args.length == 0)
			jsonFromServer = "{\"redTeam\":[{\"dict\":{\"num_total_damage\":\"490\",\"num_spells_cast\":\"8\",\"num_kills\":\"2\",\"num_time_played_sec\":\"600\",\"num_spell_damage_done\":\"20\",\"num_first_hit_kills\":\"5\",\"num_assists\":\"10\",\"num_hits\":\"4\",\"num_creep_score\":\"10\",\"num_attempted_attacks\":\"10\"},\"summonerName\":\"Crs Elementz\"},{\"dict\":{\"num_total_damage\":\"600\",\"num_spells_cast\":\"8\",\"num_kills\":\"2\",\"num_time_played_sec\":\"600\",\"num_spell_damage_done\":\"20\",\"num_first_hit_kills\":\"5\",\"num_assists\":\"10\",\"num_hits\":\"4\",\"num_creep_score\":\"10\",\"num_attempted_attacks\":\"10\"},\"summonerName\":\"Crs Saintvicious\"},{\"dict\":{\"num_total_damage\":\"490\",\"num_spells_cast\":\"8\",\"num_kills\":\"2\",\"num_time_played_sec\":\"600\",\"num_spell_damage_done\":\"20\",\"num_first_hit_kills\":\"5\",\"num_assists\":\"10\",\"num_hits\":\"10\",\"num_creep_score\":\"10\",\"num_attempted_attacks\":\"10\"},\"summonerName\":\"Crs Nyjacky\"}],\"blueTeam\":[{\"dict\":{\"num_total_damage\":\"490\",\"num_spells_cast\":\"8\",\"num_kills\":\"2\",\"num_time_played_sec\":\"600\",\"num_spell_damage_done\":\"20\",\"num_first_hit_kills\":\"5\",\"num_assists\":\"10\",\"num_hits\":\"4\",\"num_creep_score\":\"10\",\"num_attempted_attacks\":\"10\"},\"summonerName\":\"CLG HotshotGG\"},{\"dict\":{\"num_total_damage\":\"800\",\"num_spells_cast\":\"8\",\"num_kills\":\"2\",\"num_time_played_sec\":\"600\",\"num_spell_damage_done\":\"20\",\"num_first_hit_kills\":\"5\",\"num_assists\":\"10\",\"num_hits\":\"4\",\"num_creep_score\":\"100\",\"num_attempted_attacks\":\"10\"},\"summonerName\":\"CLG Doublelift\"},{\"dict\":{\"num_total_damage\":\"490\",\"num_spells_cast\":\"8\",\"num_kills\":\"2\",\"num_time_played_sec\":\"600\",\"num_spell_damage_done\":\"20\",\"num_first_hit_kills\":\"5\",\"num_assists\":\"10\",\"num_hits\":\"4\",\"num_creep_score\":\"10\",\"num_attempted_attacks\":\"10\"},\"summonerName\":\"CLG Chauster\"}]}";

			
		else
			jsonFromServer = args[0];
		
		

		AchievementTracker t = new AchievementTracker(jsonFromServer, new FakeDatabaseConnection());
	}
	
	
	
	
	//method i use to make fake json to test
	public static String generateFakeJson()
	{
		Player p = new Player();
		
		p.summonerName = "pnadd";
		//p.dict.put("str_summoner_name", "pnadd");
		p.dict.put("num_creep_score", "100");
		p.dict.put("num_attempted_attacks", "10");
		p.dict.put("num_hits", "4");
		p.dict.put("num_total_damage", "520");
		p.dict.put("num_kills", "2");
		p.dict.put("num_first_hit_kills", "5");
		p.dict.put("num_assists", "10");
		p.dict.put("num_spells_cast", "8");
		p.dict.put("num_spell_damage_done", "20");
		p.dict.put("num_time_played_sec", "600");
		
		Game g = new Game();
		g.redTeam.add(p);
		//g.blueTeam.add(p);
		//g.blueTeam.add(p);


		
		
		Gson gson = new Gson();
		
		String strJson = gson.toJson(g);
		return strJson.replaceAll("\"", "\\\\\"");

	}
}
class FakeDatabaseConnection implements DatabaseConnection
{

	public int getTotalWins(String summoner)
	{
		return 170 + (int)(Math.random() * 40);
	}

	public int getTotalGames(String summoner) 
	{
		return 985 + (int)(Math.random() * 20);
	}
	
}
