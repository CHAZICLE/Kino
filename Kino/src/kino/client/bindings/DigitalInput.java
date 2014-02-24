package kino.client.bindings;

/**
 * A class to represent a stated input
 */
public abstract class DigitalInput extends Input {
	public abstract boolean getState();
	public abstract boolean getCurrentState();
	public abstract boolean getEventState();
}
