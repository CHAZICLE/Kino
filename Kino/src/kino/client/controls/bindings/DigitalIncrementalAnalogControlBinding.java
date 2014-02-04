package kino.client.controls.bindings;

import kino.client.controls.AnalogOutput;
import kino.client.controls.DigitalInput;

public class DigitalIncrementalAnalogControlBinding extends ControlBinding {
	public DigitalInput input;
	public AnalogOutput output;
	public double value;
	public boolean invert;
	@Override
	public void forceInvoke() {
		if(input.get()==!invert)
			output.set(value);
	}
}
