package com.cafe24.lastofres.battlerapp.actor;

public abstract class Actor {

	private String name;
	private int health;
	private final int attack;
	private final int defence;
	
	
	public Actor(String name, int health, int attack, int defence) {
		setName(name);
		this.health = health;
		this.attack = attack;
		this.defence = defence;
	}
	
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

	public int getAttack() {
		return attack;
	}

	public int getDefence() {
		return defence;
	}

	public abstract int getTurnPriority();
	
	public abstract void receiveDamage(int damage);
	
}
