/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils.configuration.printer -
 * PrintStreamPrinter.java Created: 03.08.2014 - 16:53:21
 */
package at.chrl.nutils.configuration.printer;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import at.chrl.nutils.FileUtil;
import at.chrl.nutils.JVMInfoUtil;
import at.chrl.nutils.StringUtils;
import at.chrl.nutils.configuration.IConfigPrinter;
import at.chrl.nutils.configuration.Property;

/**
 * @author Vinzynth
 *
 */
public class PropertyFileStreamPrinter implements IConfigPrinter {

	private final File targetFile;

	private boolean recreated = false;

	public PropertyFileStreamPrinter(final File targetFile) {
		this.targetFile = targetFile;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.nutils.configuration.IConfigPrinter#printConfigField(at.chrl.nutils.configuration.Property,
	 *      java.lang.String)
	 */
	@Override
	public <T> void printConfigField(Property property, String currentValue, Class<T> annotatedType) {
		if (!recreated) {
			FileUtil.recreate(this.targetFile);
			this.targetFile.setReadable(true, false);
			this.targetFile.setWritable(true, false);
			recreated = true;
		}
		try (BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath(), Charset.forName("UTF-8"), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
			writer.write("# " + JVMInfoUtil.getInstance().printSection(property.key()) + System.lineSeparator());
			writer.write("# Description:" + System.lineSeparator());
			writer.write("# " + StringUtils.insertRepetitive(property.description(), JVMInfoUtil.PRINT_SECTION_LENGTH - (property.key().length() + 5), "\n# ") + System.lineSeparator());

			writer.write("# " + System.lineSeparator());

			if (property.examples().length > 0) {
				writer.write("# Examples:" + System.lineSeparator());
				for (String example : property.examples())
					writer.write("# " + example + System.lineSeparator());

				writer.write("# " + System.lineSeparator());
			} else if (annotatedType.isEnum()) {
				writer.write("# Valid Examples:" + System.lineSeparator());
				for (T example : annotatedType.getEnumConstants())
					writer.write("# " + example.toString() + System.lineSeparator());

				writer.write("# " + System.lineSeparator());
			}

			writer.write("# Default Value: " + property.defaultValue() + System.lineSeparator());
			writer.write(property.key() + "=" + currentValue + System.lineSeparator());

			writer.write(System.lineSeparator());
		} catch (Exception e) {
			System.err.println("Exception during property exportation: " + targetFile.getAbsolutePath() + " | " + e.getMessage());
			e.printStackTrace();
		}
	}
}
