package com.cafe24.lastofres.battlerapp.actor;

import java.util.ArrayDeque;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;
import util.Dice;

public abstract class Player extends Actor {
	
	private int focus;
	private int intelligence;
	private int agility;
	
	public Player(String name, int health, int attack, int defence, int intelligence, int agility) {
		super(name, health, attack, defence);
		this.focus = 100;
		this.intelligence = intelligence;
		this.agility = agility;
		
		rest = new ActorAction("Rest") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
					ArrayDeque<TriggeredEffect> effects = new ArrayDeque<TriggeredEffect>();
					
					effects.add(new TriggeredEffect("Resting", pair.getLeft(), pair.getRight(), 0) {

						@Override
						public void start() {
							target.setHealth(getHealth() + 5);
						}

						@Override
						public void trigger() {}

						@Override
						public void end() {}
						
					});
					
					return effects;
				}, -1);
			}
		};
		
		onReceiveDamage = new CompositeFunction<Integer, Integer>(rawDamage -> {
			if (Dice.roll(1, 100) <= this.getAgility() / 2) {
				System.out.println("Attack evaded");
				return 0;
			} else {
				return rawDamage - (Dice.roll(1, 10) + Dice.roll(2, this.getDefence() / 10));				
			}			
		}, -1);
	}
	
	@Override
	public void setHealth(int health) {
		if (health <= 1) {
			health = 1;
			
			if (getAttachedEffects().stream().noneMatch(te -> "Critical Condition".equals(te.getName()))) {
				attachTriggeredEffect(new TriggeredEffect("Critical Condition", this, this, -1) {

					@Override
					public void start() {
						System.out.println(target.getName() + " Entered Critical Condition");
					}

					@Override
					public void trigger() {}

					@Override
					public void end() {}
					
				});
			}
		}
		
		super.setHealth(health);
	}
	
	public int getFocus() {
		return focus;
	}

	public void setFocus(int focus) {
		if (focus < 0) {
			focus = 0;
		} else if (focus > 100) {
			focus = 100;
		}
		
		this.focus = focus;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public int getAgility() {
		return agility;
	}
	
	public ArrayDeque<TriggeredEffect> rest() {
		return null;
	}
	
	public int getTurnPriority() {
		return agility;
	}
}
