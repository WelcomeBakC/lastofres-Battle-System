package com.cafe24.lastofres.battlerapp;

import java.util.PriorityQueue;
import java.util.Scanner;

import com.cafe24.lastofres.battlerapp.actor.Actor;
import com.cafe24.lastofres.battlerapp.actor.InfantryClass;
import com.cafe24.lastofres.battlerapp.actor.Player;
import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public class Game {
	
	private Player testplayer;

	private Actor testnpc;
	
	private PriorityQueue<TriggeredEffect> effects = new PriorityQueue<TriggeredEffect>((e1, e2) -> {
		return e1.getDuration() - e2.getDuration();
	});
	
	public Game() {
		testplayer = new InfantryClass("dummy player", 80, 70, 80, 90, 100);
		
		testnpc = new Actor("dummy npc", 32000, 100, 300) {
			{
				onReceiveDamage = new CompositeFunction<Integer, Integer>(rawDamage -> {
					return rawDamage - (getDefence() / 10);
				}, -1);
			}
			
			@Override
			public void receiveDamage(int damage) {
				if (damage < 0) {
					damage = 0;
				}
				setHealth(getHealth() - damage);
				
			}
			
		};
	}
	
	public void run() {
		Scanner scanner = new Scanner(System.in);
		
		while (scanner.hasNext()) {
			scanner.next();
			executeTurn(testplayer);
			System.out.println("turn executed");
		}
		
		scanner.close();
	}
	
	
	public void executeTurn(Actor actor) {
		// trigger effects and decrement duration
		for (TriggeredEffect te : actor.getEffects()) {
			te.trigger();
			te.setDuration(te.getDuration() - 1);
		}
		
		// choose target
		
		
		// cast skill at target and attach returned effects
		for (TriggeredEffect te : actor.useBasicAttack(testnpc)) {
			Actor target = te.getTarget();
			target.attachTriggeredEffect(te);
			effects.add(te);
		}
		
		// end 0 duration effects
		while (effects.peek().getDuration() == 0) {
			TriggeredEffect effect = effects.poll();
			Actor target = effect.getTarget();
			target.detachTriggeredEffect(effect);
		}
	}
	
	public static void main(String[] args) {
		Game thisgame = new Game();
		
		thisgame.run();
	}

}
