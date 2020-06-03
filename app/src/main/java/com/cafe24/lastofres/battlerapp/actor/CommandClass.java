package com.cafe24.lastofres.battlerapp.actor;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public class CommandClass extends Player {

	public CommandClass(String name, int maxHealth, int health, int attack, int defence, int focus, int intelligence, int agility) {
		super(name, maxHealth, health, attack, defence, focus, intelligence, agility);

		skills = new ActorAction[] {
				commandSkill1(this),
				commandSkill2(this),
				commandSkill3(this),
				commandSkill4(this)
		};
	}

	private static ActorAction commandSkill1(Actor owner) {
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
								+ " for " + getDuration() + " Turn");
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

	private static ActorAction commandSkill2(Actor owner) {
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
								+ " for " + getDuration() + " Turn");
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

	private static ActorAction commandSkill3(Actor owner) {
		return new ActorAction("Give Defence Penetration") { {
			onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
				ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();

				Function<Actor, TriggeredEffect> penetratedDefence = actor -> new TriggeredEffect("Defence Penetrated", pair.getRight(), actor, 0) {
					
					private Function<Integer, Integer> penetratedDebuff = rawDamage -> {
						return rawDamage;
					};

					@Override
					public void start() {
						target.onReceiveDamage.addHead(penetratedDebuff, 0);
					}

					@Override
					public void trigger() {}

					@Override
					public void end() {
						target.onReceiveDamage.removeHead(penetratedDebuff);
					}
				};
				
				baseEffects.add(new TriggeredEffect("Defence Penetration Buff", pair.getLeft(), pair.getRight(), 2) {
					
					private Function<ArrayDeque<TriggeredEffect>, ArrayDeque<TriggeredEffect>> buffOnCast = es -> {
						Set<Actor> targets = new HashSet<Actor>();
						es.stream().filter(te -> te.getSource() != te.getTarget())
							.forEach(te -> targets.add(te.getTarget()));
						
						targets.forEach(t -> es.addFirst(penetratedDefence.apply(t)));
						
						return es;
					};

					@Override
					public void start() {
						target.basicAttack
							.onCast
							.addTailSegment(buffOnCast);
						
						for (ActorAction aa : target.skills) {
							aa.onCast
								.addTailSegment(buffOnCast);
						}
						
						System.out.println("All Damage Penetrating on " + target.getName() + " for "
								+ getDuration() + " Turns");
					}

					@Override
					public void trigger() {}

					@Override
					public void end() {
						target.basicAttack
							.onCast
							.removeTailSegment(buffOnCast);
					
						for (ActorAction aa : target.skills) {
							aa.onCast
								.removeTailSegment(buffOnCast);
						}
					}
				});
				
				return baseEffects;
			}, -1);
		} };
	}
	
	private static ActorAction commandSkill4(Actor owner) {
		return new ActorAction("Bolster") {
			{
				cost = new TriggeredEffect("Bolster Focus Cost", owner, owner, 0) {
	
					{
						onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
							return 35;
						}, -1);
					}
	
					@Override
					public void start() {}
	
					@Override
					public void trigger() {
						int cost = onTrigger.apply(Pair.of(source, target));
						
						Player targetPlayer = (Player) target;
						
						targetPlayer.setFocus(targetPlayer.getFocus() - cost);
					}
	
					@Override
					public void end() {}
				};
				
				onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
					ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
					
					baseEffects.add(new TriggeredEffect("Bolstered", pair.getLeft(), pair.getRight(), 1) {
	
						@Override
						public void start() {
							Player p = (Player) target;
							
							p.setMaxHealth(p.getMaxHealth() + 20);
							p.setHealth(p.getHealth() + 20);
							p.setAttack(p.getAttack() + 20);
							p.setDefence(p.getDefence() + 20);
							p.setAgility(p.getAgility() + 20);
							p.setIntelligence(p.getIntelligence() + 20);
							p.setFocus(p.getFocus() + 20);
							
							System.out.println("All Stats Bolstered on " + target.getName() + " for "
									+ getDuration() + " Turn");
						}
	
						@Override
						public void trigger() {}
	
						@Override
						public void end() {
							Player p = (Player) target;
							
							p.setMaxHealth(p.getMaxHealth() - 20);
							p.setAttack(p.getAttack() - 20);
							p.setDefence(p.getDefence() - 20);
							p.setAgility(p.getAgility() - 20);
							p.setIntelligence(p.getIntelligence() - 20);
						}
					});
					
					baseEffects.add(cost);
					
					return baseEffects;
				}, -1);
			}
			
			@Override
			public boolean canCast(Actor actor) {
				Player player = (Player) actor;
				
				return player.getFocus() >= cost.getOnTrigger().apply(Pair.of(actor, actor));
			}
		};
	}
}
