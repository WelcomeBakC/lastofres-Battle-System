package com.cafe24.lastofres.battlerapp;

import java.util.Scanner;

import com.cafe24.lastofres.battlerapp.actor.Actor;
import com.cafe24.lastofres.battlerapp.actor.InfantryClass;
import com.cafe24.lastofres.battlerapp.actor.Player;
import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

public class Game {
	
	private Player testplayer;

	private Actor testnpc;
	
	public Game() {

		testplayer = new InfantryClass("dummy player", 80, 70, 80, 90, 100);
		
		testnpc = new Actor("dummy npc", 32000, 100, 300) {
			
			@Override
			public void receiveDamage(int damage) {
				if (damage < 0) {
					damage = 0;
				}
				
				int modified = damage - (getDefence() / 10);
				setHealth(getHealth() - modified);
				
			}
			
		};
		
		Scanner scanner = new Scanner(System.in);
		
		while (scanner.hasNextLine()) {
			executeTurn(testplayer);
		}
		
		scanner.close();
	}
	
	
	public void executeTurn(Actor actor) {
		// trigger effects
		
		for (TriggeredEffect te : actor.useBasicAttack(testnpc)) {
			Actor target = te.getTarget();
			
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
