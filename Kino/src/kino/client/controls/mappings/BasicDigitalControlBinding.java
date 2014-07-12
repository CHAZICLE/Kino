package kino.client.controls.mappings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import kino.client.controls.io.DigitalInput;
import kino.client.controls.io.DigitalOutput;
import kino.client.controls.io.Put;

public class BasicDigitalControlBinding extends ControlBinding {
	public BasicDigitalControlBinding(DataInputStream dis) throws IOException {
		inverted = dis.readBoolean();
	}
	public void write(DataOutputStream dos) throws IOException
	{
		dos.writeBoolean(inverted);
	}
	public boolean inverted = false;
	public DigitalInput input;
	public DigitalOutput output;
	@Override
	public Put[] getPuts() {
		return new Put[]{input,output};
	}
	@Override
	public void setPuts(Put[] puts) {
		input = (DigitalInput)puts[0];
		output = (DigitalOutput)puts[1];
	}
	@Override
	public void tickRaw() {
		output.post(inverted ? !input.getCurrentState() : input.getCurrentState());
	}
}
