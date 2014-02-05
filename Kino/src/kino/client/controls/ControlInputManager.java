package kino.client.controls;

/**
 * Abstract class for handling a branch of inputs
 */
public interface ControlInputManager {
	/**
	 * Updated any controls based on any stored events
	 */
	public void pollEvents();
	/**
	 * Pulls a newly created digital input from the event cache
	 * @return The new digital input or NULL if no events were fired
	 */
	public DigitalInput readInput();
}
