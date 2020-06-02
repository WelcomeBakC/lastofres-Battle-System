package com.cafe24.lastofres.battlerapp;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import com.cafe24.lastofres.battlerapp.actor.Actor;
import com.cafe24.lastofres.battlerapp.actor.ActorAction;
import com.cafe24.lastofres.battlerapp.actor.CommandClass;
import com.cafe24.lastofres.battlerapp.actor.InfantryClass;
import com.cafe24.lastofres.battlerapp.actor.Npc;
import com.cafe24.lastofres.battlerapp.actor.Player;
import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

public class Game {
	
	//private InputStream in;
	private Scanner scanner;
	private PrintStream out;
	
	private Actor npc;
	private ArrayList<Player> playerList;
	
	private int turnNumber;
	private int roundNumber;
	private int roundTurnNumber;
	private PriorityQueue<Player> roundOrder = new PriorityQueue<Player>((p1, p2) -> {
		return p2.getTurnPriority() - p1.getTurnPriority();
	});
	
	private PriorityQueue<TriggeredEffect> effects = new PriorityQueue<TriggeredEffect>((e1, e2) -> {
		return e1.getDuration() - e2.getDuration();
	});
	
	
	public Game(InputStream in, PrintStream out, ArrayList<Player> playerList, Actor npc) {
		//this.in = in;
		this.scanner = new Scanner(in);
		this.out = out;
		this.playerList = playerList;
		this.npc = npc;
		
	}
	
	private <R> R ask(String query,  String[] optionNames, List<R> options, Scanner in, PrintStream out) {
		out.println(query);
		
		for (int i = 0; i < optionNames.length; i++) {
			out.println(i + ": " + optionNames[i]);
		}

		while (true) {
			try {
				String line = in.nextLine();
				int input = Integer.valueOf(line);
				if (input >= 0 && input < optionNames.length && optionNames[input] != null) {
					return options.get(input);
				} else {
					out.println("Invalid Input");
				}
			} catch(Exception ex) {
				out.println("Invalid Input");
			}
		}
	}
	
	private ActorAction queryAction(Actor actor) {
		String[] optionNames = new String[7];
		List<ActorAction> options = new ArrayList<ActorAction>();
		
		for (int i = 0; i < 6; i++) {
			options.add(null);
		}
		
		if (actor.getBasicAttack() != null) {
			optionNames[0] = "Attack";
			options.set(0, actor.getBasicAttack());
		}
		
		ActorAction[] skills = actor.getSkills();
		if (skills != null) {
			for (int i = 0; i < skills.length; i++) {
				optionNames[i + 1] = skills[i].getName();
				options.set(i + 1, skills[i]);
			}
		}
		
		optionNames[5] = "Rest";
		options.set(5, actor.getRest());
		
		String query = "Choose Action";
		
		//Scanner scanner = new Scanner(in);
		ActorAction action = ask(query, optionNames, options, scanner, out);
		//scanner.close();
		
		return action;
	}
	
	private Actor queryTarget(Actor actor) {
		String[] optionNames = new String[playerList.size() + 1];
		List<Actor> options = new ArrayList<Actor>(playerList);
		options.add(npc);
		
		for (int i = 0; i < options.size(); i++) {
			optionNames[i] = options.get(i).getName();
		}
		
		String query = "Choose target";
		
		//Scanner scanner = new Scanner(in);
		Actor target = ask(query, optionNames, options, scanner, out);
		//scanner.close();
		
		return target;
	}
	
	private void executeTurn(Actor actor) {
		turnNumber++;
		roundTurnNumber++;
		System.out.println("New Turn (" + actor.getName() + "): "
				+ roundTurnNumber + " of " + turnNumber + " Total");
		// trigger effects and decrement duration
		for (TriggeredEffect te : actor.getAttachedEffects()) {
			te.trigger();
			te.setDuration(te.getDuration() - 1);
		}
		
		// choose action and target
		if (actor instanceof Player && actor.getHealth() == 1) {
			return;
		}
		
		ActorAction action = queryAction(actor);		
		Actor turnTarget = queryTarget(actor);

		
		// cast action on target and attach returned effects
		try {
			for (TriggeredEffect te : action.cast(actor, turnTarget)) {
				turnTarget.attachTriggeredEffect(te);
				effects.add(te);
				te.trigger();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("\n--- Error encountered, but don't worry too much ---");
		}
		
		// end 0 duration effects
		TriggeredEffect e;
		while ((e = effects.peek()) != null && e.getDuration() == 0) {
			e = effects.poll();
			Actor target = e.getTarget();
			target.detachTriggeredEffect(e);
		}
		
		System.out.println("Turn Done\n");
	}
	
	private void executeRound() {
		roundNumber++;
		System.out.println("New Round: " + roundNumber);
		roundOrder.addAll(playerList);
		
		Player nextPlayer;
		while ((nextPlayer = roundOrder.poll()) != null) {
			if (roundTurnNumber == playerList.size() / 2) {
				executeTurn(npc);
			}
			executeTurn(nextPlayer);
		}
		
		executeTurn(npc);
		
		roundTurnNumber = 0;
		System.out.println("Round Done\n");
	}
	
	public void run() {
		
		while (true) {
			executeRound();
		}
		
	}
	
	public static void main(String[] args) {
		ArrayList<Player> playerList = new ArrayList<Player>();
		
		Player testplayer1 = new InfantryClass("dummy infantry", 80, 50, 50, 50, 55);
		Player testplayer2 = new CommandClass("dummy command", 80, 50, 50, 50, 35);
		playerList.add(testplayer1);
		playerList.add(testplayer2);
		
		Actor testnpc = new Npc("dummy npc", 32000, 100, 300);
		
		Game thisgame = new Game(System.in, System.out, playerList,	testnpc);
		
		thisgame.run();
	}

}
