package kino.client.controls.bindings;

import kino.client.controls.AnalogInput;
import kino.client.controls.DigitalOutput;

public class AnalogRangeDigitalControlBinding extends ControlBinding {

	public AnalogInput input;
	public DigitalOutput output;
	public double min;
	public double max;
	public boolean inverted;
	
	@Override
	public void forceInvoke() {
		if(inverted)
			output.set( (min<input.get() && input.get()<max));
		else
			output.set(!(min<input.get() && input.get()<max));
	}
	
}
