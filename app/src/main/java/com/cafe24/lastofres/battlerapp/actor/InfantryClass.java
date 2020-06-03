package com.cafe24.lastofres.battlerapp.actor;

import java.util.ArrayDeque;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;
import util.Dice;

public class InfantryClass extends Player {

	public InfantryClass(String name, int maxHealth, int health, int attack, int defence, int focus, int intelligence, int agility) {
		super(name, maxHealth, health, attack, defence, focus, intelligence, agility);
		
		basicAttack = infantryBasicAttack();
		skills = new ActorAction[] {
				infantrySkill1(this),
				infantrySkill2(this)
		};
	}

	private static ActorAction infantryBasicAttack() {
		return new ActorAction("Infantry Basic Attack") { {
			onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
				ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
				
				baseEffects.add(new TriggeredEffect("Infantry Basic Attack", pair.getLeft(), pair.getRight(), 0) {
							
					{
						onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
							return Dice.roll(2, 150);
						}, -1);
						
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
						int rawDamage = onTrigger.apply(Pair.of(source, target));
						
						//int healthBefore = target.getHealth();
						target.receiveDamage(rawDamage);
						//int actualDamage = healthBefore - target.getHealth();
						
						//System.out.println(actualDamage + " Damage done to " + target.getName());
					}

					@Override
					public void end() {}
					
				});
				return baseEffects;
			}, -1);
		} };
	}
	
	private static ActorAction infantrySkill1(Actor owner) {
		return new ActorAction("Improve Basic Attack") {
			{
				cost = new TriggeredEffect("Improved Basic Attack Focus Cost", owner, owner, 0) {
					
					{
						onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
							return 10;
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
					
					baseEffects.add(new TriggeredEffect("Improved Basic Attack", pair.getLeft(), pair.getRight(), 2) {
						
						private Function<Pair<Actor, Actor>, Integer> buffedBasicAttack = pair -> {
							return Dice.roll(2, 200);
						};
						
						private Function<ArrayDeque<TriggeredEffect>, ArrayDeque<TriggeredEffect>> attachBuffOnCast = effects -> {
							TriggeredEffect basicAttackEffect = effects.stream()
									.filter(e -> e.getName().equals("Infantry Basic Attack"))
									.findAny().get();
							
							basicAttackEffect.getOnTrigger().addHead(buffedBasicAttack, 0);
							
							return effects;
						};
	
						@Override
						public void start() {
							target.getBasicAttack()
								.getOnCast()
								.addTailSegment(attachBuffOnCast);
							
							System.out.println("Attack Boosted on " + target.getName()
									+ " for " + getDuration() + " Turns");
						}
	
						@Override
						public void trigger() {}
	
						@Override
						public void end() {
							target.getBasicAttack()
								.getOnCast()
								.removeTailSegment(attachBuffOnCast);
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

	private static ActorAction infantrySkill2(Actor owner) {
		return new ActorAction("Invulnerability") {
			{
				cost = new TriggeredEffect("Invulnerability Focus Cost", owner, owner, 0) {
					
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
					
					baseEffects.add(new TriggeredEffect("Invulnerability", pair.getLeft(), pair.getRight(), 2) {
	
						private Function<Integer, Integer> invulnerabilityBuff = actualDamage -> {
							return 0;
						};
						
						@Override
						public void start() {
							target.onReceiveDamage.addTailSegment(invulnerabilityBuff);
							
							System.out.println(target.getName() + " Gained Invulnerability for "
									+ getDuration() + " Turns");
						}
	
						@Override
						public void trigger() {}
	
						@Override
						public void end() {
							target.onReceiveDamage.removeTailSegment(invulnerabilityBuff);
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
