package com.cafe24.lastofres.battlerapp.actor;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public abstract class Skill {

	protected CompositeFunction<Pair<Actor, Actor>, TriggeredEffect[]> onCast;
	private String name;
	
	public Skill(String name) {
		this.name = new String(name);
	}
	
	public String getName() {
		return new String(name);
	}
	
	public String createMessage(Actor source, Actor target) {
		return source.getName() + " used " + getName() + " on " + target.getName() + ".";
	}
	
	public TriggeredEffect[] cast(Actor source, Actor target) {
		return onCast.apply(Pair.of(source, target));
	}

}
