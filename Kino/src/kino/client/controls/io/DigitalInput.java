package kino.client.controls.io;


/**
 * A class to represent a stated input
 */
public abstract class DigitalInput extends Input {
	public abstract boolean getCurrentState();
	public abstract boolean getEventState();
}
