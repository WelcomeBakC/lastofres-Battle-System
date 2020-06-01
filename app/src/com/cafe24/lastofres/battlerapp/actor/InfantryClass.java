package com.cafe24.lastofres.battlerapp.actor;

import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;
import util.Dice;

public class InfantryClass extends Player {

	public InfantryClass(String name, int health, int attack, int defence, int intelligence, int agility) {
		super(name, health, attack, defence, intelligence, agility);
		basicAttack = infantryBasicAttack();
		skills = new ActorAction[] {
				createSkill1(),
				createSkill2()
		};
	}

	private static ActorAction infantryBasicAttack() {

		return new ActorAction("Basic Attack") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, TriggeredEffect[]>(pair -> {
					
					return new TriggeredEffect[] {
							new TriggeredEffect("Basic Attack", pair.getLeft(), pair.getRight(), 0) {
								
								{
									onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
										return Dice.roll(2, 150);
									}, 1);
									
									onTrigger.addTailSegment(baseDamage -> {
										double modifier = getSource().getAttack() / 100.0;
										return (int) (baseDamage * modifier);
									});
								}
								
								@Override
								public void start() {}

								@Override
								public void trigger() {
									// calculate raw damage
									int rawDamage = onTrigger.apply(pair);
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
	
	private static ActorAction createSkill1() {
		return new ActorAction("Buff Attack") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, TriggeredEffect[]>(pair -> {
					return new TriggeredEffect[] {
							new TriggeredEffect("Basic Attack Buff", pair.getLeft(), pair.getRight(), 2) {
								
								private Function<Pair<Actor, Actor>, Integer> buffedBasicAttack = pair -> {
									return Dice.roll(2, 200);
								};
								
								private Function<TriggeredEffect[], TriggeredEffect[]> attachBuff = effects -> {
									TriggeredEffect basicAttackEffect = Stream.of(effects)
											.filter(e -> e.getName().equals("Basic Attack"))
											.findAny().get();
									
									basicAttackEffect.getOnTrigger().addHead(buffedBasicAttack, 0);
									
									return effects;
								};

								@Override
								public void start() {
									getTarget().getBasicAttack().getOnCast().addTailSegment(attachBuff);
								}

								@Override
								public void trigger() {}

								@Override
								public void end() {
									getTarget().getBasicAttack().getOnCast().removeTailSegment(attachBuff);
								}
								
							}
					};
				}, -1);
			}
		};
	}

	private static ActorAction createSkill2() {
		return null;
	}

	@Override
	public void receiveDamage(int damage) {
		
	}
	
}
