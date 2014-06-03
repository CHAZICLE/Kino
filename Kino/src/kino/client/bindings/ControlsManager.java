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
	
	/**
	 * Called by platform specific init to register input listener
	 */
	public static void registerInputScanner(ControlInputScanner scanner) {
		synchronized (scanners) {
			scanners.add(scanner);
		}
	}
	
	/**
	 * Scans the registered input scanners for any new ControlInputHolders
	 */
	public static void scanForNewInputHolders() {
		synchronized (scanners) {
			Iterator<ControlInputScanner> it = scanners.iterator();
			while (it.hasNext()) {
				ControlInputHolder controlInputHolder = it.next().scan();
				if (controlInputHolder != null) {
					synchronized (inputHolders) {
						inputHolders.put(controlInputHolder.getName(), controlInputHolder);
					}
					Iterator<ControlProfile> api = inactiveProfiles.iterator();
					while (api.hasNext()) {
						ControlProfile cp = api.next();
						if (cp.hasInputDependency(controlInputHolder.getName()) && cp.checkDependencies()) {
							cp.setActive(true);
							api.remove();
							activeProfiles.add(cp);
						}
					}
				}
			}
		}
	}

	/**
	 * Ensures that all the control input holders are still active
	 */
	public static void verifyInputHolders() {
		synchronized (inputHolders) {
			Iterator<ControlInputHolder> it = inputHolders.values().iterator();
			while (it.hasNext()) {
				ControlInputHolder controlInputHolder = it.next();
				if (!controlInputHolder.isStillActive()) {
					it.remove();
					Iterator<ControlProfile> api = activeProfiles.iterator();
					while (api.hasNext()) {
						ControlProfile cp = api.next();
						if (cp.hasInputDependency(controlInputHolder.getName())) {
							api.remove();
							inactiveProfiles.add(cp);
							cp.setActive(false);
						}
					}
				}
			}
		}
	}

	public static void registerOutputHolder(ControlOutputHolder controlOutputHolder) {
		synchronized (outputHolders) {
			outputHolders.put(controlOutputHolder.getName(), controlOutputHolder);
		}
		Iterator<ControlProfile> api = inactiveProfiles.iterator();
		while (api.hasNext()) {
			ControlProfile cp = api.next();
			if (cp.hasOutputDependency(controlOutputHolder.getName()) && cp.checkDependencies()) {
				cp.setActive(true);
				api.remove();
				activeProfiles.add(cp);
			}
		}
	}

	public static void unregisterOutputHolder(ControlOutputHolder controlOutputHolder) {
		synchronized (outputHolders) {
			outputHolders.remove(controlOutputHolder);
		}
		Iterator<ControlProfile> api = activeProfiles.iterator();
		while (api.hasNext()) {
			ControlProfile cp = api.next();
			if (cp.hasOutputDependency(controlOutputHolder.getName())) {
				api.remove();
				inactiveProfiles.add(cp);
				cp.setActive(false);
			}
		}
	}

	public static ControlInputHolder getInputHolder(String name) {
		return inputHolders.get(name);
	}

	public static ControlOutputHolder getOutputHolder(String name) {
		return outputHolders.get(name);
	}

	private static int tick;

	public static void doControlLoop() {
		if (tick % 10 == 0) {
			verifyInputHolders();
			scanForNewInputHolders();
		}
		tick++;
		for (ControlProfile cp : activeProfiles) {
			cp.tickRaw();
		}
		// TODO: Events
		/*
		 * for(ControlInputHolder ci : inputHolders) { Input input =
		 * ci.pullEvent(); }
		 */
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static List<ControlProfile> inactiveProfiles = new LinkedList<>();
	private static List<ControlProfile> activeProfiles = new LinkedList<>();

	public static void loadProfiles() {
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
