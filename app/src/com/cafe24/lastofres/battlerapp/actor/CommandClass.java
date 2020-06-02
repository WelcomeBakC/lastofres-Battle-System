package com.cafe24.lastofres.battlerapp.actor;

import java.util.ArrayDeque;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public class CommandClass extends Player {

	public CommandClass(String name, int health, int attack, int defence, int intelligence, int agility) {
		super(name, health, attack, defence, intelligence, agility);

		skills = new ActorAction[] {
				commandSkill1(),
				commandSkill2(),
				commandSkill3()
		};
	}

	private static ActorAction commandSkill1() {
		return new ActorAction("Increase Damage Reduction") { {
			onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
				ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
				
				baseEffects.add(new TriggeredEffect("Damage Reduction Buff", pair.getLeft(), pair.getRight(), 1) {
					
					private Function<Integer, Integer> buffedDamageReduction = reducedDamage -> {
						return (int) (reducedDamage - (source.getDefence() * 0.1));
					};

					@Override
					public void start() {
						getTarget().onReceiveDamage
							.addTailSegment(buffedDamageReduction);
						
						System.out.println("Damage Reduction Increased on " + target.getName()
								+ " for " + getDuration() + " Turns");
					}

					@Override
					public void trigger() {}

					@Override
					public void end() {
						getTarget().onReceiveDamage
							.removeTailSegment(buffedDamageReduction);
					}
				});
				
				return baseEffects;
			}, -1);
		} };
	}

	private static ActorAction commandSkill2() {
		return new ActorAction("Increase Damage") { {
			onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
				ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
				
				baseEffects.add(new TriggeredEffect("Damage Increase Buff", pair.getLeft(), pair.getRight(), 1) {
					
					private Function<Integer, Integer> buffedDamage = rawDamage -> {
						return (int) (rawDamage + this.source.getAttack() * 0.5);
					};
					
					private Function<ArrayDeque<TriggeredEffect>, ArrayDeque<TriggeredEffect>> buffOnCast = effects -> {
						String[] acceptedEffects = {
							"Infantry Basic Attack",
							"Magic Basic Attack",
							"Divinity Basic Attack",
							"Explosion"
						};
						
						for (TriggeredEffect e : effects) {
							if (Stream.of(acceptedEffects).anyMatch(filter -> filter.equals(e.getName()))) {
								e.getOnTrigger().addTailSegment(buffedDamage);
							}
						}
						
						return effects;
					};

					@Override
					public void start() {
						target.basicAttack
							.onCast
							.addTailSegment(buffOnCast);
						for (ActorAction aa : target.getSkills()) {
							aa.onCast
								.addTailSegment(buffOnCast);
						}
						
						System.out.println("Damage Increased on " + target.getName()
								+ " for " + getDuration() + " Turns");
					}

					@Override
					public void trigger() {}

					@Override
					public void end() {
						target.basicAttack
							.onCast
							.removeTailSegment(buffOnCast);
						for (ActorAction aa : target.getSkills()) {
							aa.onCast
								.removeTailSegment(buffOnCast);
						}
					}
				});
				
				return baseEffects;
			}, -1);
		} };
	}

	private static ActorAction commandSkill3() {
		return new ActorAction("Give Defence Penetration") { {
			onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
				ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
				/*
				TriggeredEffect penetratedDefence = new TriggeredEffect("Defence Penetrated", pair.getLeft(), pair.getRight(), 0) {
					
					private Function<Integer, Integer> penetratedDebuff = rawDamage -> {
						return rawDamage;
					};
					
					private Function<ArrayDeque<TriggeredEffect>, ArrayDeque<TriggeredEffect>> debuffOnCast = es -> {
						
						
						return baseEffects;
					};

					@Override
					public void start() {
						target.basicAttack
							.onCast
							.addTailSegment(debuffOnCast);
						
						for (ActorAction aa : target.skills) {
							aa.onCast
								.addTailSegment(debuffOnCast);
						}
					}

					@Override
					public void trigger() {}

					@Override
					public void end() {
						
					}
				};
				
				baseEffects.add(new TriggeredEffect("Defence Penetration Buff", pair.getLeft(), pair.getRight(), 2) {
					
					private Function<ArrayDeque<TriggeredEffect>, ArrayDeque<TriggeredEffect>> buffOnCast = es -> {
						es.addFirst(penetratedDefence);
						
						return baseEffects;
					};

					@Override
					public void start() {
						target.basicAttack
							.onCast
							.addTailSegment(buffOnCast);
					}

					@Override
					public void trigger() {}

					@Override
					public void end() {
						target.basicAttack
							.onCast
							.removeTailSegment(buffOnCast);
					}
				});*/
				
				return baseEffects;
			}, -1);
		} };
	}
}
