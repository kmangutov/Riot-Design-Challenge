package com.riot_challenge;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import com.riot_challenge.*;


/*
 * this class loads all .class files from the achievements directory and tries
 * to instantiate them as an Achievement
 */
public class AchievementManager 
{
	
	List<Achievement> achievements = new ArrayList<Achievement>();
	URLClassLoader loader;
	
	public AchievementManager()
	{
		URL[] urls = null;
		
		try{
			
			URL url = new URL("file://" + getWorkingDir());//getAchievDir());
			urls = new URL[]{ url };
		
		}catch(Exception e)
		{	System.err.println("ERROR LOADING ACHIEVEMENTS!");	e.printStackTrace();	}
		
		
		loader = new URLClassLoader(urls);
		
		
		loadAllAchiev();
	}
	
	//this file takes a player and matches him up against all the achievements
	public void processAchievments(Player p)
	{
		for(Achievement achiev : achievements)
		{
			if(achiev.doesEarn(p))
			{
				try{
				
				
					p.addAchievment(achiev);
				}catch(Exception e)
				{
					System.err.println("ERROR CHECKING IF ACHIEVEMENT MATCHES!! bad json?");
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadAllAchiev()
	{
		File folder = new File(getAchievDir());
		
		for(File f : folder.listFiles())
		{
			String fileName = f.getName();
			if(fileName.endsWith(".class"))
			{
				loadAchiev(f);
			}
		}
		
	}
	//we've found a class file, instantiate it
	private void loadAchiev(File f)
	{
		try{
			
			
			String className = f.getName().replaceAll(".class", "");
			Class skeleton = loader.loadClass("com.riot_challenge.achievements." + className);
			
			
			Achievement instance = (Achievement)skeleton.newInstance();
			achievements.add(instance);
			
			System.out.println("Loaded Achievement '" + instance.getName() + "'");
			
		}catch(Exception e)
		{
			System.err.println("ERROR LOADING ACHIEVEMENT " + f.getName());
			e.printStackTrace();
		}
	}
	
	private String getWorkingDir()
	{
		return System.getProperty("user.dir");
	}
	private String getAchievDir()
	{
		return getWorkingDir() + "\\bin\\com\\riot_challenge\\achievements";
	}
}
