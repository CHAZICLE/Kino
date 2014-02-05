package kino.client.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kino.client.controls.bindings.ControlBinding;

public abstract class ControlBindingManager {
	public int tick = 0;
	private ControlInputManager[] _controlinputs = new ControlInputManager[] {
		new LWJGLControlInputsManager()
	};
	public List<ControlBinding> bindings = new ArrayList<ControlBinding>();
	public List<ControlInputManager> inputsHolders = Arrays.asList(_controlinputs);
	boolean rawInput = false;
	/**
	 * Called by the root gui manager to update the controls
	 */
	public void tickControls()
	{
		tick++;
		if(!rawInput)
		{
			for(ControlInputManager cih : inputsHolders)
			{
				cih.pollEvents();
			}
		}
		
	}
}
