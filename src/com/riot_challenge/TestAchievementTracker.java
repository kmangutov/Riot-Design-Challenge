package com.riot_challenge;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.riot_challenge.achievements.*;

public class TestAchievementTracker {

	
	private NoAchievConnection noAchievConn;
	private VeteranConnection vetConn;
	private BothConnection bothConn;
	
	private Player defaultPlayer;
	private Player sharpshooterPlayer;
	private Player doubleliftPlayer;
	private Player bruiserPlayer;
	private Player doubleliftAndBruiserPlayer;
	
	@Before 
	public void setUp()
	{
		noAchievConn = new NoAchievConnection();
		vetConn = new VeteranConnection();
		bothConn = new BothConnection();
		
		defaultPlayer = getDefaultPlayer();
		sharpshooterPlayer = getDefaultPlayer().propertyPut("num_hits", "9");
		doubleliftPlayer = getDefaultPlayer().propertyPut("num_creep_score", "90");
		bruiserPlayer = getDefaultPlayer().propertyPut("num_total_damage", "600");
		doubleliftAndBruiserPlayer = getDefaultPlayer().propertyPut("num_creep_score", "90").propertyPut("num_total_damage", "600");
	}
	

	@Test
	public void test_defaultAllConn()
	{
		test_playerAllConn(generateJson(defaultPlayer), new ArrayList<Achievement>());	
	}
	
	@Test
	public void test_sharpshooterAllConn()
	{
		List<Achievement> expected = new ArrayList<Achievement>();
		expected.add(new SharpshooterAchievement());
		
		test_playerAllConn(generateJson(sharpshooterPlayer), expected);	
	}
	@Test
	public void test_twoAchievAllConn()
	{
		List<Achievement> expected = new ArrayList<Achievement>();
		expected.add(new BruiserAchievement());
		expected.add(new DoubleliftAchievement());
		
		test_playerAllConn(generateJson(doubleliftAndBruiserPlayer), expected);	
	}
	
	@Test
	public void test_simpleFiveVsFive()
	{
		Game g = new Game();
		
		for(int i = 0; i < 5; i++)
			g.redTeam.add(getDefaultPlayer("player" + i));
		for(int i = 0; i < 4; i++)
			g.blueTeam.add(getDefaultPlayer("player" + (i + 5)));
		
		g.blueTeam.add(doubleliftAndBruiserPlayer);
		//player named pnadd that deserves doubellift + bruiser
		
		String json = (new Gson().toJson(g));
		
		List<Achievement> expected = new ArrayList<Achievement>();
		expected.add(new BruiserAchievement());
		expected.add(new DoubleliftAchievement());
		
		test_playerAllConn(json, expected);
	}
	
	
	
	
	
	
	
	
	@Test
	public void test_negativeValue()
	{
		Player p = getDefaultPlayer();
		p.propertyPut("num_total_damage", "-99");
		
		AchievementTracker state = test_noAchievements(generateJson(p));
		
		assertTrue(verifyAchievementsExact(state.getSummonerAchievs("pnadd"), new ArrayList<Achievement>()));
	}
	
	@Test
	public void test_nullValue()
	{
		Player p = getDefaultPlayer();
		p.propertyPut("num_total_damage", null);
		
		AchievementTracker state = test_noAchievements(generateJson(p));
		
		assertTrue(verifyAchievementsExact(state.getSummonerAchievs("pnadd"), new ArrayList<Achievement>()));
	}
	@Test
	public void test_noValue()
	{
		Player p = getDefaultPlayer();
		p.dict.remove("num_total_damage");
		
		AchievementTracker state = test_noAchievements(generateJson(p));
		
		assertTrue(verifyAchievementsExact(state.getSummonerAchievs("pnadd"), new ArrayList<Achievement>()));

	}
	
	
	
	
	@Test
	public void test_complexThreeVsThree()
	{
		Game g = new Game();
		
		Player crsElementz = getDefaultPlayer("Crs Elementz");
		
		Player crsSv = getDefaultPlayer("Crs Saintvicious");
		crsSv.propertyPut("num_total_damage", "600");//this makes him a bruiser
		
		Player crsNyjacky = getDefaultPlayer("Crs Nyjacky");
		crsNyjacky.propertyPut("num_hits", "10"); //makes him sharpshooter
		
		g.redTeam.add(crsElementz);
		g.redTeam.add(crsSv);
		g.redTeam.add(crsNyjacky);
		
		
		Player hotshot = getDefaultPlayer("CLG HotshotGG");
		
		Player doublelift = getDefaultPlayer("CLG Doublelift");
		doublelift.propertyPut("num_creep_score", "100");//makes him doublelift
		doublelift.propertyPut("num_total_damage", "800");//makes him bruiser
		
		Player chauster = getDefaultPlayer("CLG Chauster");
		
		g.blueTeam.add(hotshot);
		g.blueTeam.add(doublelift);
		g.blueTeam.add(chauster);
		
		String json = (new Gson().toJson(g));
		
		
		//System.out.println(json.replaceAll("\"", "\\\\\""));
		
		AchievementTracker state = new AchievementTracker(json, noAchievConn);
		
		ArrayList<Achievement> expected = new ArrayList<Achievement>();
		assertTrue(verifyAchievementsExact(state.getSummonerAchievs("Crs Elementz"), expected));
		
		expected.add(new BruiserAchievement());
		
		assertTrue(verifyAchievementsExact(state.getSummonerAchievs("Crs Saintvicious"), expected));
		
		//check to make sure doiublelift doesnt JUST have bruiser
		assertFalse(verifyAchievementsExact(state.getSummonerAchievs("CLG Doublelift"), expected));
		
		expected.add(new DoubleliftAchievement());
		assertTrue(verifyAchievementsExact(state.getSummonerAchievs("CLG Doublelift"), expected));
	}
	

	
	
	//only checks pnadd for expected achievements
	//take player p, create json, feed into my tracker, check to make sure achievments are good
	public void test_playerAllConn(String json, List<Achievement> expected)
	{	
		AchievementTracker state = test_noAchievements(json);
		assertTrue(verifyAchievementsExact(state.getSummonerAchievs("pnadd"), expected));
		
		expected.add((Achievement)new VeteranAchievement());
		
		state = test_veteranOnly(json);
		assertTrue(verifyAchievementsExact(state.getSummonerAchievs("pnadd"), expected));
		
		expected.add((Achievement) new BigWinnerAchievement());
		
		state = test_veteranAndWinner(json);
		assertTrue(verifyAchievementsExact(state.getSummonerAchievs("pnadd"), expected));
	
	}
	
	
	
	public boolean verifyAchievements(List<Player> players, Achievement achiev)
	{
		for(Player p : players)
			if(!p.getAchievements().contains(achiev))
				return false;
		
		return true;
	}
	
	
	//make sure player p has only achievements in expected
	public boolean verifyAchievementsExact(List<Achievement> list, List<Achievement> expected)
	{
		List<Achievement> achievs = new ArrayList<Achievement>(list);
		
		for(Achievement expectedAchiev : expected)
		{
			if(!achievs.contains(expectedAchiev))
				return false;
			
			achievs.remove(expectedAchiev);
		}
		
		return achievs.size() == 0;
	}

	
	
	//these methods take a Player I have built, generate the json that would be sent 
	//from riot servers to me, and instantiate AchievementTracker using a connection
	public AchievementTracker test_noAchievements(String json) 
	{
		AchievementTracker at = new AchievementTracker(json, noAchievConn);

		return at;
	}
	public AchievementTracker test_veteranOnly(String json)
	{
		AchievementTracker at = new AchievementTracker(json, vetConn);

		return at;
	}
	public AchievementTracker test_veteranAndWinner(String json)
	{
		AchievementTracker at = new AchievementTracker(json, bothConn);
		
		return at;
	}
	
	
	
	
	
	
	
	
	public Player getDefaultPlayer()
	{
		return getDefaultPlayer("pnadd");
	}
	public Player getDefaultPlayer(String s)
	{
		Player p = new Player();
		
		p.summonerName = s;

		p.dict.put("num_creep_score", "10");
		p.dict.put("num_attempted_attacks", "10");
		p.dict.put("num_hits", "4");
		p.dict.put("num_total_damage", "490");
		p.dict.put("num_kills", "2");
		p.dict.put("num_first_hit_kills", "5");
		p.dict.put("num_assists", "10");
		p.dict.put("num_spells_cast", "8");
		p.dict.put("num_spell_damage_done", "20");
		p.dict.put("num_time_played_sec", "600");
		
		return p;
	}
	public String generateJson(Player p)
	{
		Game g = new Game();
		g.redTeam.add(p);
		//g.blueTeam.add(p);
		//g.blueTeam.add(p);
		
		Gson gson = new Gson();
		
		String strJson = gson.toJson(g);
		return strJson;
	}
}




class NoAchievConnection implements DatabaseConnection
{
	public int getTotalWins(String summoner)
	{
		return 10;
	}

	public int getTotalGames(String summoner) 
	{
		return 10;
	}
}

class VeteranConnection implements DatabaseConnection
{
	public int getTotalWins(String summoner)
	{
		return 10;
	}

	public int getTotalGames(String summoner) 
	{
		return 10000;
	}
}
class BothConnection implements DatabaseConnection
{
	public int getTotalWins(String summoner)
	{
		return 10000;
	}

	public int getTotalGames(String summoner) 
	{
		return 100000;
	}
}