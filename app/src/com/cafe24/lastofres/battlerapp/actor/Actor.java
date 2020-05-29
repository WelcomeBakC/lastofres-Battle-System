package com.cafe24.lastofres.battlerapp.actor;

public abstract class Actor {

	private String name;
	private int health;
	
	public String getName() {
		return new String(name);
	}
	
	public void setName(String name) {
		this.name = new String(name);
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public abstract int getTurnPriority();
	
}
