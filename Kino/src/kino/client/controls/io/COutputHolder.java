package kino.client.controls.io;


public interface COutputHolder {
	public String getName();
	public int digitalOutputSize();
	public int analogOutputSize();
	public DigitalOutput getDigitalOutput(int i);
	public AnalogOutput getAnalogOutput(int i);
}
