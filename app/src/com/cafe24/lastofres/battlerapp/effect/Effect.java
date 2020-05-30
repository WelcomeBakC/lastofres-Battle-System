package com.cafe24.lastofres.battlerapp.effect;

import java.util.function.BiConsumer;

import com.cafe24.lastofres.battlerapp.actor.Actor;

public class Effect {

	private final Actor source;
	private Actor target;

	private int duration;
	
	private final BiConsumer<Actor, Actor> effect;
	
	
	public Effect(Actor source, Actor target, int duration, BiConsumer<Actor, Actor> effect) {
		this.source = source;
		this.target = target;
		this.duration = duration;
		this.effect = effect;
	}
	
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void trigger() {
		effect.accept(source, target);
	}
	
	public void end () {
		;
	}
}
