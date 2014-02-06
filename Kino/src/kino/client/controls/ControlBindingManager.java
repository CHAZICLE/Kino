package kino.client.controls;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kino.client.controls.bindings.ControlBinding;
/**
 * Holds the key bindings, stores the input managers
 * THERE CAN BE ONLY ONE
 */
public class ControlBindingManager {
	private static long tick = 0;
	
	private static LinkedList<ControlOutputHolder> outputHolders = new LinkedList<ControlOutputHolder>();
	private static LinkedList<ControlInputHolder> inputHolders = new LinkedList<ControlInputHolder>();
	static {
		inputHolders.add(new LWJGLControlInputsHolder());
	}
	private List<ControlBinding> bindings = new ArrayList<ControlBinding>();
	boolean rawInput = false;
	/**
	 * Called by the root GUI manager to update the controls
	 */
	public void tickControls()
	{
		tick++;
		if(!rawInput)
		{
			for(ControlInputHolder cih : inputHolders)
			{
				cih.tickEvents(tick);
			}
		}
		for(ControlBinding cb : bindings)
		{
			cb.invoke(tick);
		}
	}
}
