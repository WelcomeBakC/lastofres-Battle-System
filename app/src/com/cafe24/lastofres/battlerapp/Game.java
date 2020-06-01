package com.cafe24.lastofres.battlerapp;

import java.io.InputStream;
import java.util.PriorityQueue;
import java.util.Scanner;

import com.cafe24.lastofres.battlerapp.actor.Actor;
import com.cafe24.lastofres.battlerapp.actor.ActorAction;
import com.cafe24.lastofres.battlerapp.actor.InfantryClass;
import com.cafe24.lastofres.battlerapp.actor.Player;
import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public class Game {
	
	private InputStream in;
	
	private Scanner scanner;
	
	private Player testplayer;

	private Actor testnpc;
	
	private PriorityQueue<TriggeredEffect> effects = new PriorityQueue<TriggeredEffect>((e1, e2) -> {
		return e1.getDuration() - e2.getDuration();
	});
	
	public Game(InputStream in) {
		this.in = in;
		scanner = new Scanner(this.in);
		
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
		
		while (scanner.hasNext()) {
			scanner.next();
			executeTurn(testplayer);
			System.out.println("turn executed");
		}
		
		scanner.close();
	}
	
	
	public void executeTurn(Actor actor) {
		// trigger effects and decrement duration
		for (TriggeredEffect te : actor.getAttachedEffects()) {
			te.trigger();
			te.setDuration(te.getDuration() - 1);
		}
		
		// choose action and target
		ActorAction action = null;
		Actor turnTarget = null;
		
		while (scanner.hasNext()) {
			switch (scanner.next()) {
			case ("attack"):
				action = actor.getBasicAttack();
				turnTarget = testnpc;
			break;
			case ("buff"):
				action = actor.getSkills()[0];
				turnTarget = actor;
			break;
			default:
				System.out.println("try again");
			}
			
			if (action != null) {
				break;
			}
		}
		
		// cast skill at target and attach returned effects
		for (TriggeredEffect te : action.cast(actor, turnTarget)) {
			turnTarget.attachTriggeredEffect(te);
			effects.add(te);
			te.trigger();
		}
		
		// end 0 duration effects
		TriggeredEffect e;
		while ((e = effects.peek()) != null && e.getDuration() == 0) {
			e = effects.poll();
			Actor target = e.getTarget();
			target.detachTriggeredEffect(e);
		}
	}
	
	public static void main(String[] args) {
		Game thisgame = new Game(System.in);
		
		thisgame.run();
	}

}
