/**
 * (C) ChRL 2013 - chrl-utils - at.chrl.utils - ArrayParser.java Created:
 * 30.12.2013 - 16:56:19
 */
package at.chrl.nutils;

/**
 * @author Vinzynth
 *
 */
public class ArrayParser {

	public static int[] parseToIntArray(String[] data) {
		int[] returnMe = new int[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = Integer.valueOf(data[i]);
		return returnMe;
	}

	public static long[] parseToLongArray(String[] data) {
		long[] returnMe = new long[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = Long.valueOf(data[i]);
		return returnMe;
	}

	public static boolean[] parseToBooleanArray(String[] data) {
		boolean[] returnMe = new boolean[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = Boolean.valueOf(data[i]);
		return returnMe;
	}

	public static short[] parseToShortArray(String[] data) {
		short[] returnMe = new short[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = Short.valueOf(data[i]);
		return returnMe;
	}

	public static String[] parseToStringArray(int[] data) {
		String[] returnMe = new String[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = String.valueOf(data[i]);
		return returnMe;
	}

	public static String[] parseToStringArray(long[] data) {
		String[] returnMe = new String[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = String.valueOf(data[i]);
		return returnMe;
	}

	public static String[] parseToStringArray(boolean[] data) {
		String[] returnMe = new String[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = String.valueOf(data[i]);
		return returnMe;
	}

	public static String[] parseToStringArray(short[] data) {
		String[] returnMe = new String[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = String.valueOf(data[i]);
		return returnMe;
	}

	public static String[] parseToStringArray(Object[] data) {
		String[] returnMe = new String[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = String.valueOf(data[i]);
		return returnMe;
	}

	public static Integer[] parseToIntegerArray(String[] data) {
		Integer[] returnMe = new Integer[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = Integer.valueOf(data[i]);
		return returnMe;
	}

	public static Long[] parseToLongObjectArray(String[] data) {
		Long[] returnMe = new Long[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = Long.valueOf(data[i]);
		return returnMe;
	}

	public static Boolean[] parseToBooleanObjectArray(String[] data) {
		Boolean[] returnMe = new Boolean[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = Boolean.valueOf(data[i]);
		return returnMe;
	}

	public static Short[] parseToShortObjectArray(String[] data) {
		Short[] returnMe = new Short[data.length];
		for (int i = 0; i < returnMe.length; i++)
			returnMe[i] = Short.valueOf(data[i]);
		return returnMe;
	}
}
