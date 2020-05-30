package com.cafe24.lastofres.battlerapp.actor;

import com.cafe24.lastofres.battlerapp.Effect;

public abstract class Player extends Actor {
	
	private int focus;
	private final int attack;
	private final int defence;
	private final int intelligence;
	private final int agility;
	private Skill[] skills;
	
	protected Player(int attack, int defence, int intelligence, int agility, Skill[] skills) {
		this.focus = 100;
		this.attack = attack;
		this.defence = defence;
		this.intelligence = intelligence;
		this.agility = agility;
		this.skills = skills;
	}

	// getters and setters
	public int getFocus() {
		return focus;
	}

	public void setFocus(int focus) {
		this.focus = focus;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefence() {
		return defence;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public int getAgility() {
		return agility;
	}

	// instance methods
	public abstract Effect[] attack(Actor target);

	public Skill[] getSkills() {
		return skills;
	}

	public Effect[] useSkill(int number, Actor target) {
		return skills[number].apply(this, target);
	}
	
	public Effect[] rest() {
		return null;
	}
	
	public abstract void receiveDamage(int damage);
	
	@Override
	public int getTurnPriority() {
		return agility;
	}
	
	
}
