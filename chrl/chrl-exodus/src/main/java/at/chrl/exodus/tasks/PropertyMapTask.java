/**
 * (C) ChRL 2014 - chrl-exodus - at.chrl.exodus.tasks - PropertyMapTask.java
 * Created: 03.08.2014 - 22:33:59
 */
package at.chrl.exodus.tasks;

import gnu.trove.set.hash.THashSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import at.chrl.exodus.Exodus;
import at.chrl.utils.TaskExecutor.Task;

/**
 * @author Vinzynth
 *
 */
public final class PropertyMapTask extends Task{

	private final File fileToRead;
	private Properties props;
	
	public PropertyMapTask(final File fileToRead) {
		this.fileToRead = fileToRead;
	}
	
	public long getFileSize(){
		return this.fileToRead.length();
	}

	public final PropertyGenerationTask getPropertyGenerationTask(){
		return new PropertyGenerationTask(new File("output" + File.separator + fileToRead.getName() + ".properties"), props);
	}
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.utils.TaskExecutor.Task#runTask()
	 */
	@Override
	public void runTask() {
		long fileLength = fileToRead.length();
		long time = System.nanoTime();
		
		final THashSet<String> outputData = new THashSet<>(300);//<String, String>(300);
		props = new Properties();
		
//		
//		BufferedReader br = null;
//		try {
//			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileToRead), Charset.forName("UTF-8")), 8192<<4);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		
//		
//		System.out.println(br.lines().count());
//		

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileToRead), Charset.forName("UTF-8")), 8192<<4)){
			br.lines()
			.distinct()
			.forEach(strLine -> perLine(outputData, strLine));
		} catch (IOException e1) {
			Exodus.err.println("Error reading File: " + fileToRead.getAbsolutePath() + " | "+ e1.getMessage());
			e1.printStackTrace(Exodus.err);
		}
		
		outputData.stream().forEach(s -> props.put(s, ""));
		
		System.out.println("File read: " + fileLength/1024 + " KiB | " + fileLength*1_000_000_000/(System.nanoTime() - time)/1024 + " KiB/s");
	}

	private static void perLine(final THashSet<String> outputData, String strLine) {
		if(strLine.contains("</")){
			int indexOf = strLine.indexOf("</") +2;
			outputData.add(strLine.substring(indexOf, strLine.indexOf(">", indexOf))); 
		}
		else if(strLine.contains("<") && strLine.contains(">")){
			strLine = strLine.substring(strLine.indexOf("<")+1, strLine.length()-1);
			int index = strLine.indexOf(" ") + 1;
			if(index == 0){
				outputData.add(strLine);
				return;
			}
			final String header = strLine.substring(0, index-1);
			final String headerPrefix = header + ".";
			outputData.add(header); // header
			int spaceIndex = index;
			while(spaceIndex > 0){
				index = strLine.indexOf("=", spaceIndex);
				if(index < 0)
					break;
				outputData.add(headerPrefix + strLine.substring(spaceIndex, index)); // key;
				spaceIndex = strLine.indexOf("\"", index+2) + 2;
				//strLine.subSequence(index+1, spaceIndex-2) // value;
			}
		}
	}
}
