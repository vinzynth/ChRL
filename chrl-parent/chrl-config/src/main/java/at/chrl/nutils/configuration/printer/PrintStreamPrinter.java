/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils.configuration.printer -
 * PrintStreamPrinter.java Created: 03.08.2014 - 16:53:21
 */
package at.chrl.nutils.configuration.printer;

import java.io.PrintStream;

import at.chrl.nutils.JVMInfoUtil;
import at.chrl.nutils.configuration.IConfigPrinter;
import at.chrl.nutils.configuration.Property;
import at.chrl.utils.StringUtils;

/**
 * @author Vinzynth
 *
 */
public class PrintStreamPrinter implements IConfigPrinter {

	final PrintStream ps;

	public PrintStreamPrinter(final PrintStream ps) {
		this.ps = ps;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.nutils.configuration.IConfigPrinter#printConfigField(at.chrl.nutils.configuration.Property,
	 *      java.lang.String)
	 */
	@Override
	public <T> void printConfigField(Property property, String currentValue, Class<T> annotatedType) {
		ps.println("# " + JVMInfoUtil.printSection(property.key()));
		ps.println("# Description:");
		ps.println("# " + StringUtils.insertRepetitive(property.description(), JVMInfoUtil.PRINT_SECTION_LENGTH - (property.key().length() + 6), "\n# "));

		ps.println();

		if (property.examples().length > 0) {
			ps.println("# Examples:" + System.lineSeparator());
			for (String example : property.examples())
				ps.println("# " + example + System.lineSeparator());

			ps.println("# " + System.lineSeparator());
		} else if (annotatedType.isEnum()) {
			ps.println("# Valid Examples:" + System.lineSeparator());
			for (T example : annotatedType.getEnumConstants())
				ps.println("# " + example.toString() + System.lineSeparator());

			ps.println("# " + System.lineSeparator());
		}

		ps.println("# Default Value: " + property.defaultValue());
		ps.println(property.key() + "=" + currentValue);

		ps.println();
	}
}
