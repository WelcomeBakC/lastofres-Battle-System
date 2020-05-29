package com.cafe24.lastofres.battlerapp.actor;

import com.cafe24.lastofres.battlerapp.Effect;

public class Military implements School {

	
	private static Skill getSkill1() {
		return new Skill("") {
			
			@Override
			public Effect[] apply(Actor source, Actor target) {
				return null;
			}
			
			@Override
			public String createMessage(Actor source, Actor target) {
				return "";
			}
		};
	}

	private static Skill getSkill2() {
		return null;
	}

	public static Effect[] doAttack(Actor source, Actor target) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Skill[] getSkills() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Effect[] useSkill(int number, Actor target) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void receiveDamage(int damage) {
		// TODO Auto-generated method stub
		
	}
	
}
