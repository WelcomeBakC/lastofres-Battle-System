package com.cafe24.lastofres.battlerapp.effect;

import org.apache.commons.lang3.tuple.Pair;

import com.cafe24.lastofres.battlerapp.actor.Actor;

import util.CompositeFunction;

public abstract class TriggeredEffect{
	
	private String name;
	
	protected final Actor source;
	protected Actor target;

	private int duration;
	
	protected CompositeFunction<Pair<Actor, Actor>, Integer> onStart;
	protected CompositeFunction<Pair<Actor, Actor>, Integer> onTrigger;
	protected CompositeFunction<Pair<Actor, Actor>, Integer> onEnd;
	
	
	public TriggeredEffect(String name, Actor source, Actor target, int duration) {
		this.source = source;
		this.target = target;
		this.duration = duration;
	}

	
	public String getName() {
		return new String(name);
	}
	
	public Actor getSource() {
		return source;
	}
	
	public Actor getTarget() {
		return target;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public CompositeFunction<Pair<Actor, Actor>, Integer> getOnInitial() {
		return onStart;
	}
	
	public CompositeFunction<Pair<Actor, Actor>, Integer> getOnTrigger() {
		return onTrigger;
	}
	
	public CompositeFunction<Pair<Actor, Actor>, Integer> getOnEnd() {
		return onEnd;
	}
	
	public abstract void start();
	
	public abstract void trigger();
	
	public abstract void end();
	
	public String createMessage(Actor source, Actor target) {
		return source.getName() + " used " + getName() + " on " + target.getName() + ".";
	}
}
