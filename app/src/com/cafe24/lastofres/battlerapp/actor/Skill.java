package com.cafe24.lastofres.battlerapp.actor;

import java.util.function.BiFunction;

import com.cafe24.lastofres.battlerapp.Effect;

public abstract class Skill implements BiFunction<Actor, Actor, Effect[]> {

	private String name;
	
	public Skill(String name) {
		this.name = new String(name);
	}
	
	public String getName() {
		return new String(name);
	}
	
	public abstract String createMessage(Actor source, Actor target);

}
