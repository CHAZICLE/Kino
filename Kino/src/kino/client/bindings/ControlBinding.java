package kino.client.bindings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Set;

public abstract class ControlBinding {

	public static ControlBinding load(DataInputStream dis) {
		//TODO
		return null;
	}
	public static void save(ControlBinding cb, DataOutputStream dos) {
		//TODO
	}

	public abstract void addInputs(Set<String> inputs);
	public abstract void addOutputs(Set<String> outputs);
	public void tickRaw() {}
}
