package kino.client.controls.bindings;

import kino.client.controls.DigitalInput;
import kino.client.controls.DigitalOutput;

public class BasicDigitalControlBinding extends ControlBinding {

	@Override
	public void forceInvoke() {
		output.set(invert ? !input.get() : input.get());
	}
	public DigitalInput input;
	public DigitalOutput output;
	public boolean invert;
}
