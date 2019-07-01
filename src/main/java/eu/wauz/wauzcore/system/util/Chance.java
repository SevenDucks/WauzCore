package eu.wauz.wauzcore.system.util;

import java.util.Random;

public class Chance {
	
	public static boolean oneIn(int chance) {
		return new Random().nextInt(chance) == 0;
	}
	
	public static boolean percent(int chance) {
		return new Random().nextInt(100) < chance;
	}
	
	public static float negativePositive(float maximum) {
		return negativePositive((int) (maximum * 100)) / 100;
	}
	
	private static float negativePositive(int maximum) {
		return (float) ((float) (new Random().nextInt(maximum * 200 + 1) / (float) 100) - maximum);
	}

}
