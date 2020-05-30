package com.cafe24.lastofres.battlerapp;

import java.util.Random;

public class Dice {
	
	public static int roll(int factor, int size) {
		Random gen = new Random();
		
		return factor * (1 + gen.nextInt(size - 1));
	}
}
