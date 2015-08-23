/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - FileUtil.java Created:
 * 03.08.2014 - 22:08:29
 */
package at.chrl.nutils;

import java.io.File;
import java.io.IOException;

/**
 * @author Vinzynth
 *
 */
public final class FileUtil {

	/**
	 * Creates specified File. <br>
	 * Deletes File if it already exists.
	 * 
	 * @param f
	 *            File to recreate
	 */
	public static final void recreate(final File f) {
		try {
			if (!f.createNewFile()) {
				f.delete();
				f.createNewFile();
			}
		} catch (IOException e) {
			System.err.println("Could not create new Property File: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
