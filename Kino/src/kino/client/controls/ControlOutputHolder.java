package kino.client.controls;

public class ControlOutputHolder {
	public String getName(){return "Unknown Output";}
	public int digitalSize(){return 0;}
	public int analogSize(){return 0;}
	public String getDigitalName(int i){return "Unknown";}
	public String getAnalogName(int i){return "Unknown";}
	public DigitalInput getDigital(int i){return null;}
	public AnalogInput getAnalog(int i){return null;}
	
	private boolean removed = false;
	public final void remove() { removed = true; }
	public final boolean isRemoved() { return removed; }
}
