package util;

import java.util.Random;

public class Dice {
	private static Random generator = new Random();
	
	public static int roll(int factor, int size) {
		
		return factor * (1 + generator.nextInt(size));
	}
}
