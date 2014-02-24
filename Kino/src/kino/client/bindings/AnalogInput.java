package kino.client.bindings;

public abstract class AnalogInput extends Input {
	public abstract boolean getValue();
	public abstract boolean getCurrentValue();
	public abstract boolean getEventValue();
}
