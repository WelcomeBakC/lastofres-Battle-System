package com.cafe24.lastofres.battlerapp.actor;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public abstract class Player extends Actor {
	
	private int focus;
	private final int intelligence;
	private final int agility;
	
	protected Player(String name, int health, int attack, int defence, int intelligence, int agility) {
		super(name, health, attack, defence);
		this.focus = 100;
		this.intelligence = intelligence;
		this.agility = agility;
		
		rest = new ActorAction("Rest") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, TriggeredEffect[]>(pair -> {
					return new TriggeredEffect[] {
							new TriggeredEffect("Resting", pair.getLeft(), pair.getRight(), 0) {

								@Override
								public void start() {
									target.setHealth(getHealth() + 5);
								}

								@Override
								public void trigger() {}

								@Override
								public void end() {}
								
							}
					};
				}, -1);
			}
		};
	}
	
	@Override
	public void setHealth(int health) {
		if (health < 1) {
			health = 1;
		}
		super.setHealth(health);
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
