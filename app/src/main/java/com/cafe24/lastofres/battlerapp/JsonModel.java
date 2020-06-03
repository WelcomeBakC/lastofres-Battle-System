package com.cafe24.lastofres.battlerapp;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.cafe24.lastofres.battlerapp.actor.CommandClass;
import com.cafe24.lastofres.battlerapp.actor.DivinityClass;
import com.cafe24.lastofres.battlerapp.actor.InfantryClass;
import com.cafe24.lastofres.battlerapp.actor.MagicClass;
import com.cafe24.lastofres.battlerapp.actor.Npc;
import com.cafe24.lastofres.battlerapp.actor.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonModel {
	
	public static class ActorSkeleton {
		protected String name;
		protected final int maxHealth;
		protected int health;
		protected int attack;
		protected int defence;
		
		public ActorSkeleton(String name, int maxHealth, int health, int attack, int defence) {
			this.name = name;
			this.maxHealth = maxHealth;
			this.health = health;
			this.attack = attack;
			this.defence = defence;			
		}
		
	}
	
	public static class PlayerSkeleton extends ActorSkeleton {
		protected String playerClass;
		protected int focus;
		protected int intelligence;
		protected int agility;
		
		public PlayerSkeleton(String name, int maxHealth, int health, int attack, int defence, int focus, int intelligence, int agility) {
			super(name, maxHealth, health, attack, defence);
			this.focus = focus;
			this.intelligence = intelligence;
			this.agility = agility;
		}
		
		public Player toPlayer() {
			switch (playerClass) {
			case ("InfantryClass"):
				return new InfantryClass(name, maxHealth, attack, defence, intelligence, agility);
			case ("CommandClass"):
				return new CommandClass(name, maxHealth, attack, defence, intelligence, agility);
			case ("MagicClass"):
				return new MagicClass(name, maxHealth, attack, defence, intelligence, agility);
			case ("DivinityClass"):
				return new DivinityClass(name, maxHealth, attack, defence, intelligence, agility);
			default:
				return null;
			}
		}
		
	}
	
	public static class NpcSkeleton extends ActorSkeleton {

		public NpcSkeleton(String name, int maxHealth, int health, int attack, int defence) {
			super(name, maxHealth, health, attack, defence);
		}
		
		public Npc toNpc() {
			return new Npc(name, maxHealth, attack, defence);
		}
		
	}
	
	/*public static class InfantryClassSkeleton extends PlayerSkeleton {

		public InfantryClassSkeleton(String name, int maxHealth, int health, int attack, int defence, int focus,
				int intelligence, int agility) {
			super(name, maxHealth, health, attack, defence, focus, intelligence, agility);
		}

		@Override
		public Player toPlayer() {
			// TODO Auto-generated method stub
			return new InfantryClass(name, maxHealth, attack, defence, intelligence, agility);
		}
		
	}
	
	public static class CommandClassSkeleton extends PlayerSkeleton {

		public CommandClassSkeleton(String name, int maxHealth, int health, int attack, int defence, int focus,
				int intelligence, int agility) {
			super(name, maxHealth, health, attack, defence, focus, intelligence, agility);
		}

		@Override
		public Player toPlayer() {
			// TODO Auto-generated method stub
			return new CommandClass(name, maxHealth, attack, defence, intelligence, agility);
		}
		
	}*/
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Player> getPlayerList() {
		ArrayList<Player> playerList = new ArrayList<Player>();
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try {
			InputStreamReader reader = new InputStreamReader(JsonModel.class.getResourceAsStream("/players.json"));

			playerList = ((ArrayList<PlayerSkeleton>) gson.fromJson(reader, new TypeToken<ArrayList<PlayerSkeleton>>(){}.getType()))
					.stream().map(skeleton -> skeleton.toPlayer())
					.collect(Collectors.toCollection(ArrayList::new));
			
			reader.close();
			
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return playerList;
	}

	public static Npc getNpc() {
		Npc npc = null;
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try {
			InputStreamReader reader = new InputStreamReader(JsonModel.class.getResourceAsStream("/npc.json"));

			npc = gson.fromJson(reader, NpcSkeleton.class).toNpc();
			
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return npc;
	}
}
