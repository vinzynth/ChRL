/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - CollectionUtils.java
 * Created: 02.08.2014 - 11:48:00
 */
package at.chrl.nutils;

/**
 * @author Vinzynth
 *
 */
public class CollectionUtils {

	  /**
	   * Returns a capacity that is sufficient to keep the map from being resized as
	   * long as it grows no larger than expectedSize and the load factor is >= its
	   * default (0.75).
	   */
	  public static int capacity(int expectedSize) {
	    return (int) ((expectedSize/0.75)+1);
	  }
}
