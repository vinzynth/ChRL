/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils.configuration.printer -
 * OutputStreamPrinter.java Created: 03.08.2014 - 17:56:00
 */
package at.chrl.nutils.configuration.printer;

import java.io.IOException;
import java.io.OutputStream;

import at.chrl.nutils.JVMInfoUtil;
import at.chrl.nutils.StringUtils;
import at.chrl.nutils.configuration.IConfigPrinter;
import at.chrl.nutils.configuration.Property;

/**
 * @author Vinzynth
 *
 */
public class OutputStreamPrinter implements IConfigPrinter {

	final OutputStream os;

	public OutputStreamPrinter(final OutputStream os) {
		this.os = os;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.nutils.configuration.IConfigPrinter#printConfigField(at.chrl.nutils.configuration.Property,
	 *      java.lang.String)
	 */
	@Override
	public <T> void printConfigField(Property property, String currentValue, Class<T> annotatedType) {
		try {
			os.write(("# " + JVMInfoUtil.getInstance().printSection(property.key()) + System.lineSeparator()).getBytes());
			os.write(("# Description:" + System.lineSeparator()).getBytes());
			os.write(("# " + StringUtils.insertRepetitive(property.description(), JVMInfoUtil.PRINT_SECTION_LENGTH - (property.key().length() + 6), "\n# ") + System.lineSeparator()).getBytes());

			os.write(("# " + System.lineSeparator()).getBytes());

			if (property.examples().length > 0) {
				os.write(("# Examples:" + System.lineSeparator()).getBytes());
				for (String example : property.examples())
					os.write(("# " + example + System.lineSeparator()).getBytes());

				os.write(("# " + System.lineSeparator()).getBytes());
			} else if (annotatedType.isEnum()) {
				os.write(("# Valid Examples:" + System.lineSeparator()).getBytes());
				for (T example : annotatedType.getEnumConstants())
					os.write(("# " + example.toString() + System.lineSeparator()).getBytes());

				os.write(("# " + System.lineSeparator()).getBytes());
			}

			os.write(("# Default Value: " + property.defaultValue() + System.lineSeparator()).getBytes());
			os.write((property.key() + "=" + currentValue + System.lineSeparator()).getBytes());

			os.write((System.lineSeparator()).getBytes());
			os.flush();
		} catch (IOException e) {
			System.err.println("Error writing to OutputStream: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
