package at.chrl.nutils.network;

/**
 * Disconnection Task that will be execute on
 * <code>DisconnectionThreadPool</code>
 * 
 * @author -Nemesiss-
 * @see at.chrl.nutils.network.DisconnectionThreadPool
 */
public class DisconnectionTask implements Runnable {
	/**
	 * Connection that onDisconnect() method will be executed by
	 * <code>DisconnectionThreadPool</code>
	 * 
	 * @see at.chrl.nutils.network.DisconnectionThreadPool
	 */
	private AConnection connection;

	/**
	 * Construct <code>DisconnectionTask</code>
	 * 
	 * @param connection
	 */
	public DisconnectionTask(AConnection connection) {
		this.connection = connection;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		connection.onDisconnect();
	}
}
