/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.iryna - ConnectionStates.java
 * Created: 29.07.2014 - 22:28:01
 */
package at.chrl.iryna;

/**
 * @author Vinzynth
 *
 */
public enum ConnectionState {
	DISCONNECTED(0),
	CONNECTED(1);
	
	
	int value;
	
	private ConnectionState(int value) {
		this.value = value;
	}
	
	public boolean isConnected(){
		return this.value >= CONNECTED.value;
	}
	
}
