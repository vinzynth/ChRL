/**
 * (C) ChRL 2014 - chrl-exodus - at.chrl.exodus.exodus - ExodusParser.java
 * Created: 03.08.2014 - 13:48:28
 */
package at.chrl.exodus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import at.chrl.exodus.engine.InputType;
import at.chrl.exodus.engine.OutputType;

/**
 * @author Vinzynth
 *
 */
public class ExodusParser {
	
	final Map<InputType, File> inputFiles;
	final Map<OutputType, File> outputFiles;
	

	/**
	 * 
	 */
	public ExodusParser(Iterable<File> inputFiles, Iterable<File> outputFiles) {
		this.outputFiles = new HashMap<OutputType, File>();
		this.inputFiles = new HashMap<InputType, File>();
		for (File file : outputFiles) {
			this.outputFiles.put(OutputType.DATA_FILE, file);
		}
		for (File file : inputFiles) {
			this.inputFiles.put(InputType.DATA_FILE, file);
		}
	}
	
}
