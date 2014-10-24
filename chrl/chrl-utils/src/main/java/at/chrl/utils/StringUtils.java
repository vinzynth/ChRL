/**
 * (C) ChRL 2013 - chrl-utils - at.chrl.utils.strings - StringUtils.java
 * Created: 31.12.2013 - 03:03:01
 */
package at.chrl.utils;

import at.chrl.nutils.ArrayUtils;

/**
 * @author Vinzynth
 *
 */
public final class StringUtils {
	
	/**
	 * No need for instantiation
	 */
	private StringUtils() {	}

	/**
	 * Example: (regex = "\n"); <p>
	 * 
	 * Converts: <p>
	 *  <p>
	 * Hey, <br>
	 * this is a  <br>
	 * simple test  <br>
	 *  <p>
	 * to:
	 *  <p>
	 * simple test <br>
	 * this is a <br>
	 * Hey, <br>
	 * 
	 * @param regex
	 * 			split char
	 * @param data
	 * 			data string
	 * @return
	 * 			modified string like explained in example
	 */
	public static String reverseFragments(final String regex, final String data){
		final String[] split = data.split(regex);
		final StringBuilder sb = new StringBuilder(data.length());
		sb.append(ArrayUtils.lastOf(split));
		for (int i = split.length-2; i >= 0; i--)
			sb.append(regex).append(split[i]);
		return sb.toString();
	}
	
	/**
	 * Equal to {@link StringUtils#trim(String, char...)} with parameter (data, trimChar)
	 * 
	 * @see {@link String#trim()} 
	 * @see {@link StringUtils#trim(String, char...)}
	 * @param trimChar
	 * 			char to trim
	 * @param data
	 * 			data string
	 * @return
	 * 			trimmed string
	 */
    public static String trim(final char trimChar, final String data) {
    	return trim(data, trimChar);
    }
    
    /**
     * Equal to {@link #trim(char, String)} with ' ' as char parameter
     * 
     * @param data
     * 		string to trim
     * @return
     * 		trimmed string
     */
    public static String trimSpaces(final String data){
    	return trim(' ', data);
    }
    
    /**
     * Trims "data" String like {@link String#trim()}, but uses defined "trimChars". <p>
     * 
     * @param data
     * 			data string
     * @param trimChars
     * 			chars to trim
     * @return
     * 			trimmed string
     */
    public static String trim(final String data, final char... trimChars) {
    	final char[] val = data.toCharArray();
        int len = val.length;
        int st = 0;

        while ((st < len) && checkChar(val[st],trimChars)) {
            st++;
        }
        while ((st < len) && checkChar(val[len - 1],trimChars)) {
            len--;
        }
        return ((st > 0) || (len < val.length)) ? data.substring(st, len) : data;
    }
    
    private static boolean checkChar(final char charToCheck, final char... trimChars){
    	for (char c : trimChars)
			if(charToCheck == c)
				return true;
    	return false;
    }
    
    /**
     * Equal to data.substring(count, data.length()-count)
     * 
     * @see {@link String#substring(int, int)}
     * 
     * @param data
     * 			data string to trim
     * @param count
     * 			char count to trim
     * @return
     * 			trimmed string
     */
    public static String trimAmount(final String data, final int count) {
    	return data.substring(count, data.length()-count);
    }
    
    /**
     * Splits data String and trims every substring like {@link #trim(String, char...)} does.
     * <p>
     * Mind: data.split(targetString)
     * 
     * @see {@link String#split(String)}
     * @see {@link #trim(String, char...)}
     * @param data
     * 			data string to trim
     * @param targetString
     * 			string to split data
     * @param trimChars
     * 			chars to trim like in {@link #trim(String, char...)}
     * @return
     * 			trimmed String
     */
    public static String trimAround(final String data, final String targetString, final char... trimChars){
    	String[] splited = data.split(targetString);
    	StringBuilder sb = new StringBuilder(data.length());
    	for (String string : splited) {
    		sb.append(trim(string, trimChars));
		}
    	return sb.toString();
    }
    
    /**
     * Inserts {@link String} "insertMe" periodically in {@link String} s. Integer col defines insertion period. <br>
     * <p>
     * Example: <br> <br>
     * 
     * insertRepetitive("abcdefghij", 2, "!")
     * <br>
     * Results in:
     * <br>
     * "ab!cd!ef!gh!ij"
     *  <p>
     * if <code>s.length() % col == 0</code> no insert string will be added at the end of the data string.
     * 
     * @param s
     * 			data String
     * @param col
     * 			"column length"
     * @param insertMe
     * 			String to insert
     * @return
     *          new string with inserted
     */
    public static String insertRepetitive(final String s, final int col, final String insertMe){
    	final StringBuilder result = new StringBuilder(s);
    	int i = s.length()/col;
    	if(s.length() % col == 0)
    		--i;
    	for (; i > 0; --i)
			result.insert(i * col, insertMe);
    	return result.toString();
    }
    
    /**
     * Equal to call {@link StringUtils#insertRepetitive(String, int, String)} with parameter: (s, col, System.lineSeperator())
     *  <br>
     * Useful to limit column length of a string. 
     * 
     * @see {@link StringUtils#insertRepetitive(String, int, String)}
     * @param s
     * 		data String
     * @param col
     * 		column length
     * @return
     *      String with inserted line breaks
     */
    public static String insertLineBreaks(final String s, final int col){
    	return insertRepetitive(s, col, System.lineSeparator());
    }
    
    /**
     * Counts matches of given Char "matchMe" in given String s
     * @param s
     * 			the string to search for matches
     * @param matchMe
     * 			the char to search for
     * @return
     * 			match count
     */
    public static int countMatches(final String s, final char matchMe){
    	return s.length() - s.replace(matchMe+"", "").length();
    }
    
    /**
     * @see {@link StringUtils#countMatches(String, char)}
     * @param s
     * 			the string to search for matches
     * @param matchMe
     * 			the string to search for
     * @return
     * 			match count
     */
    public static int countMatches(final String s, final String matchMe){
    	return (s.length() - s.replace(matchMe+"", "").length())/matchMe.length();
    }
}
