package com.cafe24.lastofres.battlerapp.actor;

import java.util.ArrayList;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public abstract class Actor {

	private String name;
	private int health;
	private final int attack;
	private final int defence;
	
	protected Skill basicAttack;
	protected Skill[] skills;
	protected CompositeFunction<Integer, Integer> onReceiveDamage;
	
	private ArrayList<TriggeredEffect> effects;
	
	
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

	public Skill[] getSkills() {
		return skills;
	}
	
	public TriggeredEffect[] useBasicAttack(Actor target) {
		return basicAttack.cast(this, target);
	}

	public TriggeredEffect[] castSkill(int number, Actor target) {
		return skills[number].cast(this, target);
	}
	
	public void attachTriggeredEffect(TriggeredEffect effect) {
		effects.add(effect);
		effect.start();
	}
	
	public void detachTriggeredEffect(TriggeredEffect effect) {
		effect.end();
		effects.remove(effect);
	}
	
	// TODO figure out how to implement damage reduction and defence ignore buff
	public abstract void receiveDamage(int damage);
	
}
