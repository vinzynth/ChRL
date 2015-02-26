/**
 * (C) ChRL 2014 - chrl-exodus - at.chrl.exodus.engine - PropertyGenerator.java
 * Created: 03.08.2014 - 20:26:05
 */
package at.chrl.exodus.engine;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

import at.chrl.exodus.Exodus;
import at.chrl.exodus.tasks.PropertyGenerationTask;
import at.chrl.exodus.tasks.PropertyMapTask;
import at.chrl.utils.TaskExecutor.TaskExecutor;

/**
 * Generates Property Files
 * 
 * @author Vinzynth
 *
 */
public final class PropertyGenerator {

	public static void main(String[] args) throws IOException {
		new PropertyGenerator();
	}

	private PropertyGenerator() {
		// System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism",
		// "20");
		long sysTime = System.nanoTime();

		// Double avg = FileUtils.listFiles(new File("input/"), new
		// String[]{"xml"}, true)
		// .parallelStream()
		// .filter(Objects::nonNull)
		// // .map(f -> {Exodus.out.println("Read: " + f.getAbsolutePath());
		// return f;})
		// // .filter(f -> !new File(f.getName() + ".properties").exists())
		// .map(PropertyMapTask::new)
		// .map(TaskExecutor::runInStream)
		// .map(TaskExecutor::joinTask)
		// .map(PropertyMapTask::getPropertyGenerationTask)
		// .map(TaskExecutor::runInStream)
		// .collect(Collectors.averagingInt(PropertyGenerationTask::getPropertiesSize));

		// Exodus.out.println("Processed Reading in " + (System.nanoTime() -
		// sysTime)/1_000_000 + "ms" + " | Average Properties Size: " + avg);

		LinkedList<PropertyMapTask> pmt = new LinkedList<>();
		for (File iterable_element : FileUtils.listFiles(new File("input/"), new String[] { "xml" }, true)) {
			pmt.add(new PropertyMapTask(iterable_element));
		}
		Collections.sort(pmt, new Comparator<PropertyMapTask>() {
			@Override
			public int compare(PropertyMapTask o1, PropertyMapTask o2) {
				return (int) (o2.getFileSize() - o1.getFileSize());
			}
		});
		TaskExecutor.runTaskGroup(true, pmt.toArray(new PropertyMapTask[pmt.size()]));
		LinkedList<PropertyGenerationTask> pgt = new LinkedList<>();
		for (PropertyMapTask propertyMapTask : pmt) {
			pgt.add(propertyMapTask.getPropertyGenerationTask());
		}
		TaskExecutor.runTaskGroup(true, pgt.toArray(new PropertyGenerationTask[pgt.size()]));
		Exodus.out.println("Processed Reading in " + (System.nanoTime() - sysTime) / 1_000_000 + "ms");
	}

	private static final class SingletonHolder {
		private static final PropertyGenerator instance = new PropertyGenerator();
	}

	public final PropertyGenerator getInstance() {
		return SingletonHolder.instance;
	}
}
