package kino.client.controls;

/**
 * Abstract class for handling a branch of inputs
 */
public interface ControlInputHolder {
	public String getName();
	public int digitalSize();
	public int analogSize();
	public String getDigitalName(int i);
	public String getAnalogName(int i);
	public DigitalOutput getDigital(int i);
	public AnalogOutput getAnalog(int i);
	/**
	 * Updated any controls based on any stored events
	 */
	public void tickEvents(long tick);
	/**
	 * Pulls a newly created digital input from the event cache
	 * @return The new digital input or NULL if no events were fired
	 */
	public DigitalInput readDigitalInput();
}
