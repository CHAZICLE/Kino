package kino.client.bindings;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ControlsManager {
	public static final File profilesFolder = new File("./res/controls/");
	static {
		if (!profilesFolder.exists())
			profilesFolder.mkdirs();
	}
	private static ArrayList<ControlInputHolder> inputHolders = new ArrayList<ControlInputHolder>();
	private static ArrayList<ControlOutputHolder> outputHolders = new ArrayList<ControlOutputHolder>();
	private static ArrayList<ControlProfile> loadedProfiles = new ArrayList<ControlProfile>();

	/**
	 * Scans for inputs that might have appeared
	 */
	public static void scanInputs() {

	}

	/**
	 * Initialize the inputs
	 */
	public static void init() {

	}

	/**
	 * Clears and reloads the profiles based on the current inputs/output
	 * holders
	 */
	public static void reloadProfiles() {

	}

	/**
	 * Called when the GUI structure adds an element that can be controlled
	 * 
	 * @param newOutputHolder
	 */
	public static void registerOutputHolder(ControlOutputHolder newOutputHolder) {

	}

	public static void tick() {
		// Process Events
		for (ControlInputHolder inputHolder : inputHolders) {
			Input event;
			while ((event = inputHolder.fetchEvent()) != null)
				event.invokeEvent();
		}
		// Process Raw
		for (ControlProfile controlProfile : loadedProfiles) {
			controlProfile.tick();
		}
	}

	public static ControlBinding[] readBindings(FileInputStream fis) {
		return null;
	}
}
