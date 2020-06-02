package com.cafe24.lastofres.battlerapp.actor;

import java.util.ArrayDeque;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public class Npc extends Actor {

	public Npc(String name, int maxHealth, int attack, int defence) {
		super(name, maxHealth, attack, defence);
		
		basicAttack = npcBasicAttack();
		
		onReceiveDamage = new CompositeFunction<Integer, Integer>(rawDamage -> {
			return rawDamage - (getDefence() / 10);
		}, -1);
	}
	
	private static ActorAction npcBasicAttack() {

		return new ActorAction("Npc Basic Attack") {
			{
				onCast = new CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>>(pair -> {
					ArrayDeque<TriggeredEffect> baseEffects = new ArrayDeque<TriggeredEffect>();
					
					baseEffects.add(new TriggeredEffect("Npc Basic Attack", pair.getLeft(), pair.getRight(), 0) {
								
						{
							onTrigger = new CompositeFunction<Pair<Actor, Actor>, Integer>(pair -> {
								return 30;
							}, -1);
						}
						
						@Override
						public void start() {}

						@Override
						public void trigger() {
							// calculate raw damage
							int rawDamage = onTrigger.apply(Pair.of(source, target));
							
							//int healthBefore = target.getHealth();
							target.receiveDamage(rawDamage);
							//int actualDamage = target.getHealth() - healthBefore;
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
