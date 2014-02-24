package kino.client.bindings;

public interface ControlInputHolder {
	public String getName();
	public DigitalInput fetchDigitalInput();
	public AnalogInput fetchAnalogInput();
	public Input fetchEvent();
	public int digitalInputSize();
	public int analogInputSize();
	public DigitalInput getDigitalInput(int i);
	public AnalogInput getAnalogInput(int i);
	
}
