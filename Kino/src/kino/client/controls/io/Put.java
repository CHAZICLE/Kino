package kino.client.controls.io;

/**
 * Abstract class that represents an analog/digital in/out put
 */
public abstract class Put {
	/**
	 * @return The name of the Input/Output in it's holder
	 */
	public abstract String getName();
	/**
	 * @return The ID of the Input/Output in it's holder
	 */
	public abstract int getID();
}
