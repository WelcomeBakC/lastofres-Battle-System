package com.cafe24.lastofres.battlerapp.item;

import java.util.ArrayDeque;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.actor.Actor;
import com.cafe24.lastofres.battlerapp.actor.ActorAction;
import com.cafe24.lastofres.battlerapp.actor.Player;
import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public class Items {
	
	public static ActorAction getRecoverHealth1() {
		return new ActorAction("Recover Health 1") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
					ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
					
					baseEffects.add(new TriggeredEffect("Health Recovery 1", pair.getLeft(), pair.getRight(), 0) {
					
						{
							onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
								return 5;
							}, -1);
						}
						
						@Override
						public void start() {}
	
						@Override
						public void trigger() {
							int healAmount = onTrigger.apply(Pair.of(source, target));
							int health = target.getHealth();
							
							target.setHealth(health + healAmount);
							
							System.out.println(target.getName() + " Healed for " + (target.getHealth() - health));
						}
	
						@Override
						public void end() {}
						
					});
					
					return baseEffects;
				}, -1);
			}
		};
	}

	
	public static ActorAction getRecoverHealth2() {
		return new ActorAction("Recover Health 2") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
					ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
					
					baseEffects.add(new TriggeredEffect("Health Recovery 2", pair.getLeft(), pair.getRight(), 0) {
					
						{
							onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
								return 10;
							}, -1);
						}
						
						@Override
						public void start() {}
	
						@Override
						public void trigger() {
							int healAmount = onTrigger.apply(Pair.of(source, target));
							int health = target.getHealth();
							
							target.setHealth(health + healAmount);
							
							System.out.println(target.getName() + " Healed for " + (target.getHealth() - health));
						}
	
						@Override
						public void end() {}
						
					});
					
					return baseEffects;
				}, -1);
			}
		};
	}

	
	public static ActorAction getRecoverFocus1() {
		return new ActorAction("Recover Focus 1") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
					ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
					
					baseEffects.add(new TriggeredEffect("Focus Recovery 1", pair.getLeft(), pair.getRight(), 0) {
					
						{
							onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
								return 10;
							}, -1);
						}
						
						@Override
						public void start() {}
	
						@Override
						public void trigger() {
							int refocusAmount = onTrigger.apply(Pair.of(source, target));
							int recoilAmount = refocusAmount / 5;
							
							if (target instanceof Player) {
								Player player = (Player) target;
								
								int focus = player.getFocus();
								int health = target.getHealth();
								
								player.setFocus(focus + refocusAmount);
								target.setHealth(health - recoilAmount);
								
								System.out.println(target.getName() + " Refocused for " + (player.getFocus() - focus));
								System.out.println(target.getName() + " Received Recoil of " + (health - player.getHealth()));
							} else {
								System.out.println("Target is Not a Player.");
							}
						}
	
						@Override
						public void end() {}
						
					});
					
					return baseEffects;
				}, -1);
			}
		};
	}

	
	public static ActorAction getRecoverFocus2() {
		return new ActorAction("Recover Focus 2") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
					ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
					
					baseEffects.add(new TriggeredEffect("Focus Recovery 2", pair.getLeft(), pair.getRight(), 0) {
					
						{
							onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
								return 30;
							}, -1);
						}
						
						@Override
						public void start() {}
	
						@Override
						public void trigger() {
							int refocusAmount = onTrigger.apply(Pair.of(source, target));
							int recoilAmount = refocusAmount / 15 * 4;
							
							if (target instanceof Player) {
								Player player = (Player) target;
								
								int focus = player.getFocus();
								int health = target.getHealth();
								
								player.setFocus(focus + refocusAmount);
								target.setHealth(health - recoilAmount);
								
								System.out.println(target.getName() + " Refocused for " + (player.getFocus() - focus));
								System.out.println(target.getName() + " Received Recoil of " + (health - player.getHealth()));
							} else {
								System.out.println("Target is Not a Player.");
							}
						}
	
						@Override
						public void end() {}
						
					});
					
					return baseEffects;
				}, -1);
			}
		};
	}

	
	public static ActorAction getRecoverFocus3() {
		return new ActorAction("Recover Focus 3") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
					ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
					
					baseEffects.add(new TriggeredEffect("Focus Recovery 3", pair.getLeft(), pair.getRight(), 0) {
					
						{
							onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
								return 50;
							}, -1);
						}
						
						@Override
						public void start() {}
	
						@Override
						public void trigger() {
							int refocusAmount = onTrigger.apply(Pair.of(source, target));
							int recoilAmount = refocusAmount / 10 * 3;
							
							if (target instanceof Player) {
								Player player = (Player) target;
								
								int focus = player.getFocus();
								int health = target.getHealth();
								
								player.setFocus(focus + refocusAmount);
								target.setHealth(health - recoilAmount);
								
								System.out.println(target.getName() + " Refocused for " + (player.getFocus() - focus));
								System.out.println(target.getName() + " Received Recoil of " + (health - player.getHealth()));
							} else {
								System.out.println("Target is Not a Player.");
							}
						}
	
						@Override
						public void end() {}
						
					});
					
					return baseEffects;
				}, -1);
			}
		};
	}
}
