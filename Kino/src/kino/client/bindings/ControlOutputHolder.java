package kino.client.bindings;

public interface ControlOutputHolder {
	public String getName();
	public int digitalOutputSize();
	public int analogOutputSize();
	public DigitalOutput getDigitalOutput();
	public AnalogOutput getAnalogOutput();
}
