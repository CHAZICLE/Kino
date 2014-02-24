package kino.client.bindings;

public abstract class DigitalOutput extends Output {
	private boolean previousState = false;

	/**
	 * Called by a ControlBinding when a new state has been calculated
	 * 
	 * @param state
	 *            The new state of this output
	 */
	public void post(boolean state) {
		if (previousState != state)
			onStateChange(state);
		previousState = state;
	}

	/**
	 * Called by post() when a different state is posted to this output
	 */
	public void onStateChange(boolean state) {}
}
