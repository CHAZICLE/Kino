package kino.client.controls.io;


/**
 * Abstract class that handles an output
 */
public abstract class Output extends Put {
	/**
	 * @return The input holder that manages this input
	 */
	public abstract COutputHolder getHolder();
}
