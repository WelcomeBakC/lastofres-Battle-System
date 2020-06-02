package com.cafe24.lastofres.battlerapp.actor;

import java.util.ArrayDeque;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public abstract class Actor {

	private String name;
	private int maxHealth;
	private int health;
	private int attack;
	private int defence;
	
	protected ActorAction basicAttack;
	protected ActorAction[] skills;
	protected ActorAction rest;
	protected CompositeFunction<Integer, Integer> onReceiveDamage;
	
	private ArrayDeque<TriggeredEffect> attachedEffects = new ArrayDeque<TriggeredEffect>();
	
	
	public Actor(String name, int maxHealth, int attack, int defence) {
		setName(name);
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.attack = attack;
		this.defence = defence;
	}
	
	public String getName() {
		return new String(name);
	}
	
	public void setName(String name) {
		this.name = new String(name);
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		if (health < 0) {
			health = 0;
		} else if (health > maxHealth) {
			health = maxHealth;
		}
		
		this.health = health;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}
	
	public ActorAction getBasicAttack() {
		return basicAttack;
	}

	public ActorAction[] getSkills() {
		return skills;
	}
	
	public ActorAction getRest() {
		return rest;
	}
	
	public ArrayDeque<TriggeredEffect> getAttachedEffects() {
		return attachedEffects;
	}
	
	public void attachTriggeredEffect(TriggeredEffect effect) {
		attachedEffects.add(effect);
		effect.start();
	}
	
	public void detachTriggeredEffect(TriggeredEffect effect) {
		effect.end();
		attachedEffects.remove(effect);
	}

	public int receiveDamage(int damage) {
		damage = onReceiveDamage.apply(damage);
		if (damage < 0) {
			damage = 0;
		}
		setHealth(getHealth() - damage);
		
		System.out.println(damage + " Damage Received by " + getName());
		return damage;
	}
}
