package kino.client.controls;

/**
 * Handles a branch of inputs. E.g. Keyboard & Mouse, Controllers etc
 * 
 */
public abstract class ControlInputHolder {
	public String getName(){return "Unknown Input";}
	public int digitalSize(){return 0;}
	public int analogSize(){return 0;}
	public String getDigitalName(int i){return "Unknown";}
	public String getAnalogName(int i){return "Unknown";}
	public DigitalInput getDigital(int i){return null;}
	public AnalogInput getAnalog(int i){return null;}
	/**
	 * Pulls a newly created digital input 
	 * @return The new digital input or NULL if no events were fired
	 */
	public DigitalInput readNewDigitalInput() { return null; }
	/**
	 * Processes any pending events in this branch of controls.
	 * 
	 * @param tick The current tick
	 */
	public abstract void processEvents(long tick);
}
