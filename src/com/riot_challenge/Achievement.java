package com.riot_challenge;

public abstract class Achievement 
{
	public boolean equals(Object o)
	{
		return ((Achievement) o).getName().equals(getName());
	}
	
	public abstract boolean doesEarn(Player p);
	public abstract String getName();
}
