package at.chrl.nutils.cron;

@SuppressWarnings("serial")
public class CronServiceException extends RuntimeException {


    public CronServiceException() {
	}

	public CronServiceException(String message) {
		super(message);
	}

	public CronServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CronServiceException(Throwable cause) {
		super(cause);
	}
}
