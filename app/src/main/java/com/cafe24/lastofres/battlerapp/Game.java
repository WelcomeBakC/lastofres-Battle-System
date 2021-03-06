package com.cafe24.lastofres.battlerapp;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.actor.Actor;
import com.cafe24.lastofres.battlerapp.actor.ActorAction;
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
	
	private int ask(String query,  String[] optionNames, Scanner in, PrintStream out) {
		out.println(query);
		
		for (int i = 0; i < optionNames.length; i++) {
			out.println(i + ": " + optionNames[i]);
		}

		while (true) {
			try {
				String line = in.nextLine();
				int input = Integer.valueOf(line);
				if (input >= 0 && input < optionNames.length && optionNames[input] != null) {
					return input;
				} else {
					out.println("Invalid Input");
				}
			} catch(Exception ex) {
				out.println("Invalid Input");
			}
		}
	}
	
	private ActorAction queryAction(Actor actor) {
		String[] optionNames = new String[8];
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
				if (skills[i].getCost() != null) {
					optionNames[i + 1] = String.format("%s (%d)", skills[i].getName(),
							skills[i].getCost().getOnTrigger().apply(Pair.of(actor, actor)));
				} else {
					optionNames[i + 1] = String.format("%s", skills[i].getName());
				}
				
				options.set(i + 1, skills[i]);
			}
		}
		
		optionNames[5] = "Use Item";
		
		/*
		if (actor.getRest() != null) {
			optionNames[5] = "Rest";
			options.set(5, actor.getRest());
		}*/
		
		optionNames[6] = "Check Status of an Actor";
		optionNames[7] = "Skip";
		
		while (true) {
			int result = ask("Choose an Action", optionNames, scanner, out);
			
			if (result == 5) {
				String[] itemNames = Stream.of(actor.getItems())
						.map(aa -> aa.getName())
						.collect(Collectors.toList()).toArray(new String[0]);
				
				int item = ask("Choose an Item", itemNames, scanner, out);
				
				return actor.getItems()[item];
			}
			if (result == 6) {
				String[] actorNames = new String[playerList.size() + 1];
				List<Actor> actors = new ArrayList<Actor>(playerList);
				actors.add(npc);
				
				for (int i = 0; i < actors.size(); i++) {
					actorNames[i] = actors.get(i).getName();
				}
				
				result = ask("Choose an Actor", actorNames, scanner, out);
				Actor resultActor = actors.get(result);
				
				if (resultActor instanceof Player) {
					out.format("(%s) Health: %d/%d, Focus: %d/%d\n", resultActor.getName(), 
							resultActor.getHealth(), resultActor.getMaxHealth(), 
							((Player) resultActor).getFocus(), ((Player) resultActor).getIntelligence());
				} else {
					out.format("(%s) Health: %d/%d\n", resultActor.getName(),
							resultActor.getHealth(), resultActor.getMaxHealth());
				}
				out.println("Statuses:");
				
				if (resultActor.getAttachedEffects().size() == 0) {
					out.println("No Statuses\n");
				} else {
					for (TriggeredEffect te : resultActor.getAttachedEffects()) {
						out.println(te.getName());
					}
					out.println("");
				}
				
			} else if (result == 7) {
				return null;
				
			} else if (options.get(result).canCast(actor)) {

				return options.get(result);
			} else {
				System.out.println("Not Enough Focus");
			}
		}
	}
	
	private Actor queryTarget(Actor actor) {
		String[] optionNames = new String[playerList.size() + 2];
		List<Actor> options = new ArrayList<Actor>(playerList);
		options.add(npc);
		
		for (int i = 0; i < options.size(); i++) {
			optionNames[i] = options.get(i).getName();
		}
		
		optionNames[optionNames.length - 1] = "Random Player";
		
		String query = "Choose target";
		
		int result = ask(query, optionNames, scanner, out);
		
		Actor target;
		
		if (result == optionNames.length - 1) {
			target = options.get(new Random().nextInt(optionNames.length - 1));
		} else {
			target = options.get(result);
		}
		
		return target;
	}
	
	private void executeTurn(Actor actor) {
		turnNumber++;
		roundTurnNumber++;
		System.out.format("New Turn (%s): %d of %d Total\n", actor.getName(), roundTurnNumber, turnNumber);
		if (actor instanceof Player) {
			Player player = (Player) actor;
			
			System.out.format("Health: %d/%d, Focus: %d/%d\n", player.getHealth(), player.getMaxHealth(),
					player.getFocus(), player.getIntelligence());;
		} else {
			System.out.format("Health: %d/%d\n", actor.getHealth(), actor.getMaxHealth());
		}
		// trigger effects and decrement duration
		for (TriggeredEffect te : actor.getAttachedEffects()) {
			te.trigger();
			te.setDuration(te.getDuration() - 1);
		}
		
		// choose action and target
		if (actor instanceof Player && actor.getHealth() == 1) {
			System.out.println("In Critical Condition! Actions Unavailable");
			return;
		}
		
		ActorAction action = queryAction(actor);
		Actor turnTarget;
		
		if (action != null) {
			if (action.getName().contains("Recover")) {
				turnTarget = actor;
			} else {
				turnTarget = queryTarget(actor);
			}
			
			// cast action on target and attach returned effects
			try {
				for (TriggeredEffect te : action.cast(actor, turnTarget)) {
					te.getTarget().attachTriggeredEffect(te);
					effects.add(te);
					te.trigger();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("\n--- Error encountered, but don't worry too much ---");
			}
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
			/*if (roundTurnNumber == playerList.size() / 2) {
				executeTurn(npc);
			}*/
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
		ArrayList<Player> playerList;
		
		/*Player testplayer1 = new InfantryClass("dummy infantry", 80, 50, 50, 50, 55);
		Player testplayer2 = new CommandClass("dummy command", 80, 50, 50, 50, 35);
		playerList.add(testplayer1);
		playerList.add(testplayer2);
		
		Actor testnpc = new Npc("dummy npc", 32000, 100, 300);

		/*Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try {
			File f = new File("players.json");
			FileReader reader = new FileReader(f);
			
			Player testplayer3 = playerList.get(0);

			System.out.println(testplayer3.getName());
			System.out.println(testplayer3.getHealth());
			System.out.println(testplayer3.getClass().getName());
			
			reader.close();
			
			/*PrintWriter o = new PrintWriter(f);
			
			o.write(gson.toJson(testnpc, Npc.class));
			o.flush();
			o.close();*/
			
			
			/*File f = new File("players.json");
			PrintWriter o = new PrintWriter(f);
			
			o.write(gson.toJson(playerList, new TypeToken<ArrayList<Player>>(){}.getType()));
			o.flush();
			o.close();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}*/
		playerList = JsonModel.getPlayerList();
		Npc npc = JsonModel.getNpc();
		
		Game thisgame = new Game(System.in, System.out, playerList,	npc);
		
		thisgame.run();
	}

}
