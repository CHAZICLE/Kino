package kino.client.controls;

import java.util.LinkedList;

import kino.client.controls.bindings.ControlBinding;
/**
 * Holds the key bindings, stores the input managers
 * THERE CAN BE ONLY ONE
 */
public class ControlBindingManager {
	/**
	 * The currently registered output holders
	 */
	private static LinkedList<ControlOutputHolder> outputHolders = new LinkedList<ControlOutputHolder>();
	/**
	 * The currently detected output holders
	 */
	private static LinkedList<ControlInputHolder> inputHolders = new LinkedList<ControlInputHolder>();
	/**
	 * The currently loaded profiles
	 */
	private static LinkedList<ControlProfile> profiles = new LinkedList<ControlProfile>();
	private static long tick = 1;
	private static int inputScanDelay = 1000;
	private static boolean processEvents = false;
	/**
	 * Ticks the controls
	 */
	public void tick()
	{
		for(ControlProfile controlProfile : profiles)
		{
			for(ControlBinding controlBinding : controlProfile)
			{
				controlBinding.invoke(tick);
			}
		}
		if(tick%inputScanDelay==0)
		{
			scanForInputHolders();
		}
		tick++;
	}
	public void registerOutputHolder(ControlOutputHolder coh)
	{
		outputHolders.add(coh);
	}
	public void scanForInputHolders() { scanForInputHolders(true); }
	public void scanForInputHolders(boolean refreshAfter)
	{
		if(refreshAfter)
			reloadProfiles();
	}
	/**
	 * Loads any control profiles if their dependencies are met
	 */
	public void reloadProfiles()
	{
		
	}
	/**
	 * Returns a output holder given a name if it's loaded
	 */
	public ControlOutputHolder getOutputHolder(String name)
	{
		
	}
	/**
	 * Returns an input holder given a name if it's loaded
	 */
	public ControlInputHolder getInputHolder(String name)
	{
		
	}
}
