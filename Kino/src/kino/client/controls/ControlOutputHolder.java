package kino.client.controls;

public interface ControlOutputHolder {
	public String getName();
	public int digitalSize();
	public int analogSize();
	public String getDigitalName(int i);
	public String getAnalogName(int i);
	public DigitalOutput getDigital(int i);
	public AnalogOutput getAnalog(int i);
}
