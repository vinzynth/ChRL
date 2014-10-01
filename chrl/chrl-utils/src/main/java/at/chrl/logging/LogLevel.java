/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.logging - LogLevel.java
 * Created: 02.08.2014 - 21:14:27
 */
package at.chrl.logging;

/**
 * @author Vinzynth
 *
 */
public enum LogLevel {

	ERROR(7,"Error"),
	WARNING(6,"Warning"),
	INFO(5,"Info"),
	DEBUG(4,"Config"),
	FINE(3,"Fine"),
	FINER(2,"Finer"),
	FINEST(1,"Finest"),
	OFF(0,"Off");
	
	
	private int value;
	private String string;
	
	private LogLevel(int value, String string) {
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return string;
	}

	/**
	 * @param value2
	 * @return
	 */
	public static LogLevel getLogLevelByString(String value2) {
		value2 = value2.toLowerCase();
		for (LogLevel ie : LogLevel.values()) {
			if(ie.toString().toLowerCase().equals(value2))
				return ie;
		}
		return OFF;
	}
}
