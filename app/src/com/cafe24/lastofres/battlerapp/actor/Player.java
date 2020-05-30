package com.cafe24.lastofres.battlerapp.actor;

import com.cafe24.lastofres.battlerapp.effect.Effect;

public abstract class Player extends Actor {
	
	private int focus;
	private final int intelligence;
	private final int agility;
	private Skill[] skills;
	
	protected Player(String name, int health, int attack, int defence, int intelligence, int agility, Skill[] skills) {
		super(name, health, attack, defence);
		this.focus = 100;
		this.intelligence = intelligence;
		this.agility = agility;
		this.skills = skills;
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

	public abstract Effect[] attack(Actor target);

	public Skill[] getSkills() {
		return skills;
	}

	public Effect[] castSkill(int number, Actor target) {
		return skills[number].apply(this, target);
	}
	
	public Effect[] rest() {
		return null;
	}
	
	@Override
	public int getTurnPriority() {
		return agility;
	}
	
	
}
