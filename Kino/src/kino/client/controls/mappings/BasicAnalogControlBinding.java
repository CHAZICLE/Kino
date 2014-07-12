package kino.client.controls.mappings;

import java.io.DataInputStream;
import java.io.IOException;

import kino.client.controls.io.AnalogInput;
import kino.client.controls.io.AnalogOutput;
import kino.client.controls.io.Put;

public class BasicAnalogControlBinding extends ControlBinding {

	public BasicAnalogControlBinding(DataInputStream dis) throws IOException {
		offset_prescale = dis.readDouble();
		scaler = dis.readDouble();
		offset_postscale = dis.readDouble();
	}
	public BasicAnalogControlBinding(double offset_prescale, double scaler, double offset_postscale, AnalogInput input, AnalogOutput output) {
		this.offset_prescale = offset_prescale;
		this.scaler = scaler;
		this.offset_postscale = offset_postscale;
		this.input = input;
		this.output = output;
	}
	public AnalogInput input;
	public AnalogOutput output;
	public double offset_prescale = 0;
	private double scaler = 1;
	public double offset_postscale = 0;
	@Override
	public void tickRaw() {
		output.post(offset_postscale+(offset_prescale+input.getCurrentValue())*scaler);
	}
	@Override
	public Put[] getPuts() {
		return new Put[]{input,output};
	}

	@Override
	public void setPuts(Put[] puts) {
		input = (AnalogInput)puts[0];
		output = (AnalogOutput)puts[1];
	}
	
}
