package com.cafe24.lastofres.battlerapp.actor;

import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;
import util.Dice;

public class InfantryClass extends Player {

	public InfantryClass(String name, int health, int attack, int defence, int intelligence, int agility) {
		super(name, health, attack, defence, intelligence, agility);
		basicAttack = infantryBasicAttack();
		skills = new Skill[] {
				createSkill1(),
				createSkill2()
		};
	}

	private static Skill infantryBasicAttack() {

		return new Skill("Basic Attack") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, TriggeredEffect[]>(pair -> {
					
					return new TriggeredEffect[] {
							new TriggeredEffect("Basic Attack", pair.getLeft(), pair.getRight(), 0) {
								
								{
									onStart = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
										return Dice.roll(2, 150);
									}, -1);
									
									onStart.addTailSegment(baseDamage -> {
										double modifier = getSource().getAttack() / 100.0;
										return (int) (baseDamage * modifier);
									});
								}
								
								@Override
								public void start() {}

								@Override
								public void trigger() {
									// calculate raw damage
									int rawDamage = onStart.apply(pair);
									// calculate damage after defence
									int actualDamage = target.onReceiveDamage.apply(rawDamage);
									target.receiveDamage(actualDamage);
								}

								@Override
								public void end() {}
								
							}
					};
				}, -1);
			}
		};
	}
	
	private static Skill createSkill1() {
		return new Skill("Buff Attack") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, TriggeredEffect[]>(pair -> {
					
					return new TriggeredEffect[] {
							new TriggeredEffect("Buff Basic Attack", pair.getLeft(), pair.getRight(), 2) {
								
								Function<Pair<Actor, Actor>, Integer> buffedBasicAttack = pair -> {
									return Dice.roll(2, 200);
								};

								@Override
								public void start() {
									getTarget().getBasicAttack().getOnCast().addTailSegment(effects -> {
										return null;
									});
								}

								@Override
								public void trigger() {}

								@Override
								public void end() {
									
								}
								
							}
					};
				}, -1);
			}
		};
	}

	private static Skill createSkill2() {
		return null;
	}

	@Override
	public void receiveDamage(int damage) {
		
	}
	
}
