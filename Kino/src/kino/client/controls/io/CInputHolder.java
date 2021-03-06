package kino.client.controls.io;


public interface CInputHolder {
	public String getName();
	public DigitalInput fetchDigitalInput();
	public AnalogInput fetchAnalogInput();
	public Input pullEvent();
	public int digitalInputSize();
	public int analogInputSize();
	public DigitalInput getDigitalInput(int i);
	public AnalogInput getAnalogInput(int i);
	public boolean isStillActive();
}
