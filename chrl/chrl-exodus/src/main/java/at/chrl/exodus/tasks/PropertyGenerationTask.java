/**
 * (C) ChRL 2014 - chrl-exodus - at.chrl.exodus.tasks -
 * PropertyGenerationTask.java Created: 03.08.2014 - 21:59:31
 */
package at.chrl.exodus.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Properties;

import at.chrl.logging.Logger;
import at.chrl.nutils.FileUtil;
import at.chrl.utils.TaskExecutor.Task;

/**
 * @author Vinzynth
 *
 */
public class PropertyGenerationTask extends Task {

	private static final Logger log = new Logger();

	private final File f;
	private final Properties[] props;

	public PropertyGenerationTask(final File f, final Properties... props) {
		this.f = f;
		this.props = props;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.utils.TaskExecutor.Task#runTask()
	 */
	@Override
	public void runTask() {
		FileUtil.recreate(this.f);
		try (BufferedWriter writer = Files.newBufferedWriter(this.f.toPath(), StandardOpenOption.APPEND)) {
			for (Properties properties : props)
				properties.store(writer, null);
		} catch (Exception e) {
			log.error("Error writing property Files", e);
		}
	}

	/**
	 * Returns the target File
	 * 
	 * @return the target File
	 */
	public File getFile() {
		return f;
	}

	public int getPropertiesSize() {
		return Arrays.stream(props).mapToInt(Properties::size).sum();
	}

}
