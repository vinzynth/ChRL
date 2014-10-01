/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils.configuration.transformers - PrintStreamTransformer.java
 * Created: 29.07.2014 - 22:35:43
 */
package at.chrl.nutils.configuration.transformers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;

import at.chrl.nutils.Constants;
import at.chrl.nutils.configuration.PropertyTransformer;
import at.chrl.nutils.configuration.TransformationException;

/**
 * @author Vinzynth
 *
 */
public class PrintStreamTransformer implements PropertyTransformer<PrintStream> {

	public static final PrintStreamTransformer	SHARED_INSTANCE	= new PrintStreamTransformer();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrintStream transform(String value, Field field) throws TransformationException {
		value = value.toLowerCase();
		switch (value) {
			case "standart":
			case "default":
			case "out":
			case "std":
			case "stdo":
			case "stdout":
			case "syso":
			case "sysout":
			case "system.out":
				return System.out;

			case "err":
			case "error":
			case "stde":
			case "stder":
			case "stderr":
			case "syse":
			case "syserr":
			case "system.err":
				return System.err;
				
			case "nop":
				return Constants.NOP_PRINT_STREAM;
			
			default:
				try {
					return new PrintStream(new File(value.endsWith(".log") ? value : value.concat(".log")));
				} catch (FileNotFoundException e) {
					e.printStackTrace(System.err);
				}
				return Constants.NOP_PRINT_STREAM;
		}
	}

}
