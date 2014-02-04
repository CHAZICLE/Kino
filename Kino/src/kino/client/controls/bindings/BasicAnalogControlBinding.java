package kino.client.controls.bindings;

import kino.client.controls.AnalogInput;
import kino.client.controls.AnalogOutput;

public class BasicAnalogControlBinding extends ControlBinding {

	public AnalogInput input;
	public AnalogOutput output;
	public double multiplier;
	
	@Override
	public void forceInvoke() {
		output.set(input.get()*multiplier);
	}
	
}
