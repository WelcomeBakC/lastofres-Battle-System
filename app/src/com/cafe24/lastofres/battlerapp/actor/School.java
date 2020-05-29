package com.cafe24.lastofres.battlerapp.actor;

import java.util.function.BiFunction;

import com.cafe24.lastofres.battlerapp.Effect;

public interface School {
	
	
	Effect[] doAttack(Actor source, Actor target);

	Skill[] getSkills();
	
	Effect[] useSkill(int number, Actor source, Actor target);
	
	void receiveDamage(int damage);
}
