package at.chrl.algorithms.search;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Search Util
 *
 * Created by Vinzynth on 14.08.2014.
 */
public class SearchUtil {

	/**
	 * Search method which return given search pool ordered by search result. <br>
	 * Returned set may not contain every element of given set.
	 *
	 *
	 * @param searchPool
	 *          given search pool
	 * @param searchKey
	 *          given search key
	 * @return
	 *          sorted pool
	 */
	public static String[] search(final Set<String> searchPool, final String searchKey){
		if(searchKey.isEmpty())
			return new String[0];
		final char[] searchKeyChars = searchKey.toLowerCase().toCharArray();
		final char[] searchKeyCharsUpper = searchKey.toUpperCase().toCharArray();
		final int searchKeyCharsLength = searchKey.length();

		//if(searchPool.size() > 1){
			return streamSearch(searchPool, searchKeyChars, searchKeyCharsUpper, searchKeyCharsLength);
		//}
/*
		final double[] scores = new double[searchPool.size()];
		final String[] result = new String[searchPool.size()];

		int j = 0;
		for (String s : searchPool){
			double score = getScore(s, searchKeyChars, searchKeyCharsUpper, searchKeyCharsLength);

			if(score >= searchKeyCharsLength)
				continue;

			int index = indexSearch(score, j++, scores);
			int shiftLength = j - index;
			shiftArray(scores, index, shiftLength);
			shiftArray(result, index, shiftLength);
			scores[index] = score;
			result[index] = s;
		}
		String[] returnMe = new String[j];
		System.arraycopy(result,0,returnMe,0,j);
		return returnMe;
		*/
	}

	private static double getScore(final String s, final char[] keyChars, final char[] keyCharsUpper, final int keyCharsLength){
		double score = keyCharsLength;
		int charIndex = 0;
		int lastScored = 0;
		double fac = .5;
		final char[] arr = s.toCharArray();
		final int length = arr.length;
		for (int i = 0; i < length; i++) {
			char c = arr[i];
			if(keyCharsUpper[charIndex] == c){
				int facCounter = i - lastScored;
				while (facCounter-- > 0)
					fac *= .99;
				lastScored = i;
				score -= fac;
				if(i == 0 || !Character.isLetter(arr[i-1]))
					score -= fac;

				if (++charIndex >= keyCharsLength)
					break;
			}
			else if(keyChars[charIndex] == c) {
				int facCounter = i - lastScored;
				while (facCounter-- > 0)
					fac *= .99;
				lastScored = i;
				score -= fac/2;
				if (++charIndex >= keyCharsLength)
					break;
			}
		}
		return score;
	}

	private static String[] streamSearch(final Set<String> searchPool, final char[] keyChars, final char[] keyCharsUpper, final int keyCharsLength){
		Map<? extends Double, List<String>> m = searchPool.stream()
				.collect(Collectors.groupingBy(s -> SearchUtil.getScore(s, keyChars, keyCharsUpper, keyCharsLength)));
		m.remove(keyCharsLength);

		return m.entrySet().stream()
				.sorted((e1, e2) -> Double.compare(e1.getKey(), e2.getKey()))
				.flatMap(
						e -> e.getValue().stream()
				)
				.toArray(String[]::new);
	}

	@SuppressWarnings("unused")
	private static void shiftArray(double[] array, int index, int length){
		System.arraycopy(array, index, array, index+1, length);
	}

	@SuppressWarnings("unused")
	private static void shiftArray(String[] array, int index, int length){
		System.arraycopy(array, index, array, index+1, length);
	}

	public static int indexSearch(final double value, final int size, final double[] array) {
		if(value <= array[0] || size <= 0)
			return 0;
		if(value >= array[size-1])
			return size;
		int i = size >>> 1;
		while (array[i] > value)
			i = i >>> 1;
		if(array[i+1] >= value)
			return i+1;
		int o = i >>> 1;
		while(true) {
			while (array[i+o] > value)
				i = i >>> 1;
			if(array[i+o+1] >= value)
				return i+o+1;
			o += i >>> 1;
		}
	}

	public static int indexSearch(final int value, final int size, final int[] array) {
		if(value <= array[0] || size <= 0)
			return 0;
		if(value >= array[size-1])
			return size;
		int i = size >>> 1;
		int o = 0;
		while(true) {
			while (array[i+o] > value)
				i = i >>> 1;
			if(array[i+o+1] >= value)
				return i+o+1;
			o += i >>> 1;
		}
	}
}
