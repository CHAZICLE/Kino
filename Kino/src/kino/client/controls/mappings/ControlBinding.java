package kino.client.controls.mappings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import kino.client.controls.io.Put;

public abstract class ControlBinding {
	
	public static ControlBinding createBinding(DataInputStream dis) throws IOException
	{
		switch(dis.readByte())
		{
		case 0:
			return new BasicDigitalControlBinding(dis);
		case 1:
			return new BasicAnalogControlBinding(dis);
		}
		return null;
	}
	public static void writeBinding(DataOutputStream dos, ControlBinding binding) throws IOException
	{
		if(binding instanceof BasicDigitalControlBinding)
			dos.writeByte(0);
		if(binding instanceof BasicAnalogControlBinding)
			dos.writeByte(1);
		binding.write(dos);
	}
	public void write(DataOutputStream dos) throws IOException{}
	public abstract Put[] getPuts();
	public abstract void setPuts(Put[] puts);
	public void tickRaw() {}
}
