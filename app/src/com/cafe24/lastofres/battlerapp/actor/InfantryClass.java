package com.cafe24.lastofres.battlerapp.actor;

import com.cafe24.lastofres.battlerapp.Dice;
import com.cafe24.lastofres.battlerapp.effect.Effect;

public class InfantryClass extends Player {

	public InfantryClass(String name, int health, int attack, int defence, int intelligence, int agility) {
		super(name, health, attack, defence, intelligence, agility, new Skill[] {
				getSkill1(),
				getSkill2()
		});
	}
	
	private static Skill getSkill1() {
		return new Skill("buff attack") {
			
			@Override
			public Effect[] apply(Actor source, Actor target) {
				return new Effect[] {
						new Effect(source, target, 2, (Actor src, Actor tgt) -> {
							
						})
				};
			}
		};
	}

	private static Skill getSkill2() {
		return null;
	}

	@Override
	public Effect[] attack(Actor target) {
		// TODO Auto-generated method stub
		return new Effect[] {
				new Effect(this, target, 0, (Actor src, Actor tgt) -> {
					int damage = Dice.roll(2, 150)
							* src.getAttack() / 100;
					tgt.receiveDamage(damage);
				})
		};
	}

	@Override
	public void receiveDamage(int damage) {
		
	}
	
}
