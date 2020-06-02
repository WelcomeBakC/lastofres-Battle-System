package com.cafe24.lastofres.battlerapp.actor;

import java.util.ArrayDeque;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;
import util.Dice;

public class DivinityClass extends Player {

	public DivinityClass(String name, int health, int attack, int defence, int intelligence, int agility) {
		super(name, health, attack, defence, intelligence, agility);

		basicAttack = divinityBasicAttack();
		skills = new ActorAction[] {
				divinitySkill1(this),
				divinitySkill2(this)
		};
	}
	
	private static ActorAction divinityBasicAttack() {
		return new ActorAction("Divinity Basic Attack") { {
			onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
				ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
				
				baseEffects.add(new TriggeredEffect("Divinity Basic Attack", pair.getLeft(), pair.getRight(), 0) {
							
					{
						onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
							return Dice.roll(2, 100);
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
	
	private static ActorAction divinitySkill1(Actor owner) {
		return new ActorAction("치유") {
			{
				cost = new TriggeredEffect("Heal Focus Cost", owner, owner, 0) {
					
					{
						onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
							return 5;
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
					
					baseEffects.add(new TriggeredEffect("Heal", pair.getLeft(), pair.getRight(), 0) {
								
						{
							onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
								int baseHeal = ((Player) source).getIntelligence() / 10;
								return baseHeal + Dice.roll(1, baseHeal);
							}, -1);
						}
						
						@Override
						public void start() {}
	
						@Override
						public void trigger() {
							int healAmount = onTrigger.apply(Pair.of(source, target));
							
							target.setHealth(target.getHealth() + healAmount);
							
							System.out.println(target.getName() + " Healed For " + healAmount);
						}
	
						@Override
						public void end() {}
						
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
	
	private static ActorAction divinitySkill2(Actor owner) {
		return new ActorAction("소생") { {
			onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
				ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
				
				baseEffects.add(new TriggeredEffect("Revive", pair.getLeft(), pair.getRight(), 0) {
					
					{
						onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
							TriggeredEffect critical = target.getAttachedEffects().stream()
									.filter(te -> "Critical Condition".equals(te.getName()))
									.findFirst().orElse(null);
							
							if (critical == null) {
								System.out.println(target.getName() + " Not in Critical Condition");
								
								return 0;
							} else {
								target.detachTriggeredEffect(critical);
								System.out.println(target.getName() + " Revived from Critical Condition");
								
								return target.getMaxHealth() / 2;
							}
						}, -1);
					}
					
					@Override
					public void start() {}

					@Override
					public void trigger() {
						int healAmount = onTrigger.apply(Pair.of(source, target));
						
						target.setHealth(target.getHealth() + healAmount);
						
						System.out.println(target.getName() + " Healed For " + healAmount);
					}

					@Override
					public void end() {}
					
				});
				
				/*baseEffects.add(new TriggeredEffect("Revive Focus Cost", pair.getLeft(), pair.getLeft(), 0) {
					
					{
						onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
							return 5;
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
				});*/
				
				return baseEffects;
			}, -1);
		} };
	}
}
