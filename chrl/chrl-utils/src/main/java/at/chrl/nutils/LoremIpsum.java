/**
 * @author Christian Richard Leopold - ChRL
 * Jul 29, 2015 - 4:15:49 PM
 * chrl-utils
 * at.chrl.nutils
 */
package at.chrl.nutils;

/**
 * Class for Random Strings
 * 
 * @author Christian Richard Leopold - ChRL <br>
 * Jul 29, 2015 - 4:15:49 PM
 *
 */
public final class LoremIpsum {
	public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
	public static final String[] SPLITED = LOREM_IPSUM.split(" ");
	
	public static final String getHeader(){
		return "Lorem ipsum dolor sit amet";
	}
	
	public static final String get(int start, int length){
		final int end = Math.min(start+length, SPLITED.length);
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++) {
			sb.append(SPLITED[i]).append(" ");
		}
		return sb.toString();
	}
	
	public static final String getRand(){
		return Rnd.get(SPLITED);
	}
	
	public static final String getRand(int length){
		if(length >= SPLITED.length)
			return getRand();
		return get(Rnd.nextInt(SPLITED.length - length), length);
	}
}
