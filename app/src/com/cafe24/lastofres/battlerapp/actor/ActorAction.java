package com.cafe24.lastofres.battlerapp.actor;

import java.util.ArrayDeque;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.effect.TriggeredEffect;

import util.CompositeFunction;

public abstract class ActorAction {

	protected CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>> onCast;
	private String name;
	
	public ActorAction(String name) {
		this.name = new String(name);
	}
	
	public String getName() {
		return new String(name);
	}

	public CompositeFunction<Pair<Actor, Actor>, ArrayDeque<TriggeredEffect>> getOnCast() {
		return onCast;
	}
	
	public ArrayDeque<TriggeredEffect> cast(Actor source, Actor target) {
		return onCast.apply(Pair.of(source, target));
	}
	
	public String createMessage(Actor source, Actor target) {
		return source.getName() + " used " + getName() + " on " + target.getName() + ".";
	}

}
