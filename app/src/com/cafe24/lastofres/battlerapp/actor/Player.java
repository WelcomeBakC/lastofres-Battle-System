package com.cafe24.lastofres.battlerapp.actor;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

public abstract class Player extends Actor {
	
	private int focus;
	private final int intelligence;
	private final int agility;
	
	protected Player(String name, int health, int attack, int defence, int intelligence, int agility) {
		super(name, health, attack, defence);
		this.focus = 100;
		this.intelligence = intelligence;
		this.agility = agility;
	}

	public int getFocus() {
		return focus;
	}

	public void setFocus(int focus) {
		this.focus = focus;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public int getAgility() {
		return agility;
	}
	
	public TriggeredEffect[] rest() {
		return null;
	}
	
	public int getTurnPriority() {
		return agility;
	}
	
	
}
