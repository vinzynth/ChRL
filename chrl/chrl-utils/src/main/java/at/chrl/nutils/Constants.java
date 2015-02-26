/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - Constants.java Created:
 * 02.08.2014 - 21:31:59
 */
package at.chrl.nutils;

import java.io.OutputStream;
import java.io.PrintStream;

import at.chrl.logging.Logger;

/**
 * @author Vinzynth
 *
 */
public class Constants {

	public static final Logger log = new Logger();

	public static final PrintStream NOP_PRINT_STREAM = new PrintStream(new OutputStream() {
		@Override
		public void write(int b) {
		}

		@Override
		public void write(byte[] b, int off, int len) {
		}

		@Override
		public void write(byte[] b) {
		}
	});
}
