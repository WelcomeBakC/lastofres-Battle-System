package com.cafe24.lastofres.battlerapp.actor;

import com.cafe24.lastofres.battlerapp.Effect;

public class MilitaryClass extends Player {

	public MilitaryClass(int attack, int defence, int intelligence, int agility) {
		super(attack, defence, intelligence, agility, new Skill[] {
				getSkill1(),
				getSkill2()
		});
	}
	
	private static Skill getSkill1() {
		return new Skill("test skill 1") {
			
			@Override
			public Effect[] apply(Actor source, Actor target) {
				return null;
			}
			
			@Override
			public String createMessage(Actor source, Actor target) {
				return source.getName() + " used " + getName() + " on " + target.getName() + ".";
			}
		};
	}

	private static Skill getSkill2() {
		return null;
	}

	@Override
	public Effect[] attack(Actor target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receiveDamage(int damage) {
		// TODO Auto-generated method stub
		
	}
	
}
