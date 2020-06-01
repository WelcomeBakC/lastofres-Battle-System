package com.cafe24.lastofres.battlerapp.actor;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public class InfantryClass extends Player {

	public InfantryClass(String name, int health, int attack, int defence, int intelligence, int agility) {
		super(name, health, attack, defence, intelligence, agility);
		basicAttack = getBasicAttack();
		skills = new Skill[] {
				getSkill1(),
				getSkill2()
		};
	}
	
	private static Skill getSkill1() {
		return new Skill("Buff Attack") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, TriggeredEffect[]>(pair -> {
					
					return null;
				}, -1);
			}
		};
	}

	private static Skill getSkill2() {
		return null;
	}

	private static Skill getBasicAttack() {

		return new Skill("Basic Attack") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, TriggeredEffect[]>(pair -> {
					
					return new TriggeredEffect[] {
							new TriggeredEffect("Basic Attack", pair.getLeft(), pair.getRight(), 0) {
								
								{
									onStart = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
										return Integer.valueOf(1);
									}, -1);
								}
								
								@Override
								public void start() {
									// calculate raw damage
									int rawDamage = onStart.apply(pair);
									// calculate damage after defence
									int actualDamage = target.onReceiveDamage.apply(rawDamage);
									target.receiveDamage(actualDamage);
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
	public void receiveDamage(int damage) {
		
	}
	
}
