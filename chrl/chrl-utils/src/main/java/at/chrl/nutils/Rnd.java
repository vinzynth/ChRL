package at.chrl.nutils;

import java.util.List;

/**
 * @author Balancer
 * 
 */
public class Rnd {
	private static final MTRandom rnd = new MTRandom();

	/**
	 * @return rnd
	 * 
	 */
	public static float get() // get random number from 0 to 1
	{
		return rnd.nextFloat();
	}
	
	/**
	 * Returns a array of length amount with random floats.
	 * 
	 * @param amount - size of returned array
	 * @return array with random flaots
	 */
	public static float[] getFloats(int amount){
		float[] array = new float[amount];
		for (int i = 0; i < array.length; i++) {
			array[i] = rnd.nextFloat();
		}
		return array;
	}

	/**
	 * Gets a random number from 0(inclusive) to n(exclusive)
	 * 
	 * @param n
	 *            The superior limit (exclusive)
	 * @return A number from 0 to n-1
	 */
	public static int get(int n) {
		return (int) Math.floor(rnd.nextDouble() * n);
	}

	/**
	 * Get a random number from min(inclusiv) to max(inclusiv)
	 * 
	 * @param min
	 * @param max
	 * @return value
	 */
	public static int get(int min, int max) // get random number from
	// min to max (not max-1 !)
	{
		return min + (int) Math.floor(rnd.nextDouble() * (max - min + 1));
	}

	public static boolean chance(int chance) {
		return chance >= 1 && (chance > 99 || nextInt(99) + 1 <= chance);
	}

	public static boolean chance(double chance) {
		return nextDouble() <= chance / 100;
	}

	/**
	 * Though i'm not agree this one should be at Rnd, such slick move - CFx01
	 * 
	 * @param list
	 * @return
	 */
	public static Object get(Object list[]) {
		return list[get(list.length)];
	}

	public static int get(int list[]) {
		return list[get(list.length)];
	}

	public static Object get(List<?> list) {
		return list.get(get(list.size()));
	}

	/**
	 * @param n
	 * @return n
	 */
	public static int nextInt(int n) {
		return (int) Math.floor(rnd.nextDouble() * n);
	}

	/**
	 * @return int
	 */
	public static int nextInt() {
		return rnd.nextInt();
	}

	/**
	 * @return double
	 */
	public static double nextDouble() {
		return rnd.nextDouble();
	}

	/**
	 * @return double
	 */
	public static double nextGaussian() {
		return rnd.nextGaussian();
	}

	/**
	 * @return double
	 */
	public static boolean nextBoolean() {
		return rnd.nextBoolean();
	}
}
