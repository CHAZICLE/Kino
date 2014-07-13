package kino.client.controls.mappings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import kino.client.controls.io.AnalogOutput;
import kino.client.controls.io.DigitalInput;
import kino.client.controls.io.Put;

public class DigitalIncrementalAnalogControlBinding extends ControlBinding {
	public DigitalIncrementalAnalogControlBinding(DataInputStream dis)
			throws IOException {
		inverted = dis.readBoolean();
		value = dis.readDouble();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeBoolean(inverted);
		dos.writeDouble(value);
	}
	
	public DigitalIncrementalAnalogControlBinding(boolean inverted, double value, DigitalInput input, AnalogOutput output) {
		this.inverted = inverted;
		this.value = value;
		this.input = input;
		this.output = output;
	}

	private boolean inverted;
	private double value;
	private DigitalInput input;
	private AnalogOutput output;

	@Override
	public Put[] getPuts() {
		return new Put[]{input,output};
	}

	@Override
	public void setPuts(Put[] puts) {
		input = (DigitalInput)puts[0];
		output = (AnalogOutput)puts[1];
	}
	
	@Override
	public void tickRaw() {
		if(inverted!=input.getCurrentState())
		{
			output.post(value);
		}
	}

}
