package com.cafe24.lastofres.battlerapp.actor;

import java.util.ArrayDeque;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;
import util.Dice;

public class MagicClass extends Player {

	public MagicClass(String name, int maxHealth, int health, int attack, int defence, int focus, int intelligence, int agility) {
		super(name, maxHealth, health, attack, defence, focus, intelligence, agility);

		basicAttack = magicBasicAttack();
		skills = new ActorAction[] {
				magicSkill1(this),
				magicSkill2(this)
		};
	}
	
	
	private static ActorAction magicBasicAttack() {
		return new ActorAction("Magic Basic Attack") { {
			onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
				ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
				
				baseEffects.add(new TriggeredEffect("Magic Basic Attack", pair.getLeft(), pair.getRight(), 0) {
							
					{
						onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
							return Dice.roll(2, 200);
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
	
	private static ActorAction magicSkill1(Actor owner) {
		return new ActorAction("Double Attack") {
			{
				cost = new TriggeredEffect("Double Attack Focus Cost", owner, owner, 0) {
					
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
					
					baseEffects.add(new TriggeredEffect("Double Attack", pair.getLeft(), pair.getRight(), 1) {
						
						private Function<ArrayDeque<TriggeredEffect>, ArrayDeque<TriggeredEffect>> attachBuffOnCast = effects -> {
							ArrayDeque<TriggeredEffect> bonusAttacks = new ArrayDeque<TriggeredEffect>();
							effects.stream().filter(e -> e.getName().equals("Magic Basic Attack"))
									.forEach(e -> bonusAttacks.add(e));
							
							effects.addAll(bonusAttacks);
							
							return effects;
						};
	
						@Override
						public void start() {
							target.getBasicAttack()
								.getOnCast()
								.addTailSegment(attachBuffOnCast);
							
							System.out.println("Double Attack on " + target.getName()
									+ " for " + getDuration() + " Turn");
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

	private static ActorAction magicSkill2(Actor owner) {
		return new ActorAction("Explosion") {
			{
				cost = new TriggeredEffect("Explosion Focus Cost", owner, owner, 0) {
					
					{
						onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
							return 50;
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
					
					TriggeredEffect explosionDamage = new TriggeredEffect("Explosion", pair.getLeft(), pair.getRight(), 0) {
						
						{
							onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
								return Dice.roll(2, 200);
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
						
					};
					
					baseEffects.add(explosionDamage);
					baseEffects.add(explosionDamage);
					baseEffects.add(explosionDamage);
					baseEffects.add(explosionDamage);
					
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
