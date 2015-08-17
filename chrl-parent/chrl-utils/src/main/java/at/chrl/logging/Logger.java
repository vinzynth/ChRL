/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.logging - Logging.java Created:
 * 02.08.2014 - 21:10:46
 */
package at.chrl.logging;

import java.io.PrintStream;

import at.chrl.nutils.Constants;

/**
 * @author Vinzynth
 *
 */
public class Logger {

	// static{ ConfigUtil.load(Logger.class); }

	// @Property(key = "chrl.logger.error", defaultValue = "System.err")
	public PrintStream ERROR = System.err;

	// @Property(key = "chrl.logger.warning", defaultValue = "System.out")
	public PrintStream WARNING = System.out;

	// @Property(key = "chrl.logger.info", defaultValue = "System.out")
	public PrintStream INFO = System.out;

	// @Property(key = "chrl.logger.config", defaultValue = "System.out")
	public PrintStream DEBUG = System.out;

	// @Property(key = "chrl.logger.fine", defaultValue = "System.out")
	public PrintStream FINE = System.out;

	// @Property(key = "chrl.logger.finer", defaultValue = "System.out")
	public PrintStream FINER = System.out;

	// @Property(key = "chrl.logger.finest", defaultValue = "System.out")
	public PrintStream FINEST = System.out;

	// @Property(key = "chrl.logger.config", defaultValue = "INFO")
	public LogLevel loglevel = LogLevel.INFO;

	public void log(Object message, LogLevel loglevel) {
		if (this.loglevel.getValue() > loglevel.getValue())
			return;
		switch (loglevel) {
			case FINEST:
				FINEST.println(message);
			case FINER:
				FINER.println(message);
			case FINE:
				FINE.println(message);
			case DEBUG:
				DEBUG.println(message);
			case INFO:
				INFO.println(message);
				break;
			case WARNING:
				WARNING.println(message);
				break;
			case ERROR:
				ERROR.println(message);
			case OFF:
			default:
				break;
		}
	}

	public void warn(Object message, Throwable... e) {
		log(message, LogLevel.WARNING);
		logThrowables(LogLevel.WARNING, e);
	}

	public void debug(Object message, Throwable... e) {
		log(message, LogLevel.DEBUG);
		logThrowables(LogLevel.DEBUG, e);
	}

	public void info(Object message, Throwable... e) {
		log(message, LogLevel.INFO);
		logThrowables(LogLevel.INFO, e);
	}

	public void error(Object message, Throwable... e) {
		log(message, LogLevel.ERROR);
		logThrowables(LogLevel.ERROR, e);
	}

	private void logThrowables(LogLevel l, Throwable... t) {
		if (this.loglevel.getValue() > loglevel.getValue())
			return;
		PrintStream ps = getPs(l);
		for (Throwable throwable : t)
			throwable.printStackTrace(ps);
	}

	private PrintStream getPs(LogLevel l) {
		switch (loglevel) {
			case FINEST:
				return FINEST;
			case FINER:
				return FINER;
			case FINE:
				return FINE;
			case DEBUG:
				return DEBUG;
			case INFO:
				return INFO;
			case WARNING:
				return WARNING;
			case ERROR:
				return ERROR;
			case OFF:
			default:
				return Constants.NOP_PRINT_STREAM;
		}
	}

	/**
	 * @return
	 */
	public boolean isDebugEnabled() {
		return this.loglevel.getValue() >= LogLevel.DEBUG.getValue();
	}
}
