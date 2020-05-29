package com.cafe24.lastofres.battlerapp;

import java.util.function.BiConsumer;

import com.cafe24.lastofres.battlerapp.actor.Actor;

public class Effect {

	private final Actor source;
	private Actor target;
	
	private final BiConsumer<Actor, Actor> effect;
	
	private int duration;
	
	public Effect(Actor source, Actor target, BiConsumer<Actor, Actor> effect, int duration) {
		this.source = source;
		this.target = target;
		this.effect = effect;
		this.duration = duration;
	}
	
	
	public void trigger() {
		effect.accept(source, target);
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
}
