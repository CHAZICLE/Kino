package kino.client.bindings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ControlsManager {
	private static List<ControlInputScanner> scanners = new ArrayList<>();
	private static Map<String, ControlOutputHolder> outputHolders = new HashMap<>();
	private static Map<String, ControlInputHolder> inputHolders = new HashMap<>();

	public static void registerInputScanner(ControlInputScanner scanner) {
		synchronized (scanners) {
			scanners.add(scanner);
		}
	}

	public static void scanInputs() {
		synchronized (scanners) {
			Iterator<ControlInputScanner> it = scanners.iterator();
			while (it.hasNext()) {
				ControlInputHolder controlInputHolder = it.next().scan();
				if (controlInputHolder != null) {
					synchronized (inputHolders) {
						inputHolders.put(controlInputHolder.getName(), controlInputHolder);
					}
				}
			}
		}
	}

	public static void verifyInputs() {
		synchronized (inputHolders) {
			Iterator<ControlInputHolder> it = inputHolders.values().iterator();
			while (it.hasNext()) {
				ControlInputHolder controlInputHolder = it.next();
				if (!controlInputHolder.isStillActive())
					it.remove();
			}
		}
	}

	public static void registerOutputHolder(ControlOutputHolder controlOutputHolder) {
		synchronized (outputHolders) {
			outputHolders.put(controlOutputHolder.getName(), controlOutputHolder);
		}
	}

	public static void unregisterOutputHolder(ControlOutputHolder controlOutputHolder) {
		controlOutputHolder.remove();
	}

	public static ControlInputHolder getInputHolder(String name) {
		return inputHolders.get(name);
	}

	public static ControlOutputHolder getOutputHolder(String name) {
		return outputHolders.get(name);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static List<ControlProfile> inactiveProfiles = new LinkedList<>();
	private static List<ControlProfile> activeProfiles = new LinkedList<>();

	public static void loadProfiles() {
		// @IMPLEMENTATION_FILES
		File files = new File("./res/profiles");
		for (File f : files.listFiles()) {
			synchronized (inactiveProfiles) {
				inactiveProfiles.add(new ControlProfile(f));
			}
		}
	}

	public static void checkProfiles() {

	}

	public static void clearProfiles() {
		inactiveProfiles.clear();
		activeProfiles.clear();
	}
}
