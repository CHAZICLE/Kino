package kino.client.controls;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kino.client.controls.io.CInputHolder;
import kino.client.controls.io.CInputScanner;
import kino.client.controls.io.COutputHolder;
import kino.client.controls.io.DigitalInput;
import kino.client.controls.io.DigitalOutput;
import kino.client.controls.mappings.BasicDigitalControlBinding;

public class ControlsManager {
	private static List<CInputScanner> scanners = new ArrayList<>();
	private static Map<String, COutputHolder> outputHolders = new HashMap<>();
	private static Map<String, CInputHolder> inputHolders = new HashMap<>();
	
	/**
	 * Called by platform specific init to register input listener
	 */
	public static void registerInputScanner(CInputScanner scanner) {
		synchronized (scanners) {
			scanners.add(scanner);
		}
	}
	
	/**
	 * Scans the registered input scanners for any new ControlInputHolders
	 */
	public static void scanForNewInputHolders() {
		synchronized (scanners) {
			Iterator<CInputScanner> it = scanners.iterator();
			while (it.hasNext()) {
				CInputHolder controlInputHolder = it.next().scan();
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
			Iterator<CInputHolder> it = inputHolders.values().iterator();
			while (it.hasNext()) {
				CInputHolder controlInputHolder = it.next();
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

	public static void registerOutputHolder(COutputHolder controlOutputHolder) {
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

	public static void unregisterOutputHolder(COutputHolder controlOutputHolder) {
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

	public static CInputHolder getInputHolder(String name) {
		return inputHolders.get(name);
	}

	public static COutputHolder getOutputHolder(String name) {
		return outputHolders.get(name);
	}

	private static int tick;

	public static void doControls() {
		if(tick%100==0)
		{
			System.out.println("-= Controls Debug "+tick+" =-");
			System.out.println("#activeProfiles="+activeProfiles.size());
			System.out.println("#inactiveProfiles="+inactiveProfiles.size());
			System.out.println("#scanners"+scanners.size());
			System.out.println("#inputHolders"+inputHolders.size());
			System.out.println("#outputHolders"+outputHolders.size());
		}
		if (tick % 100 == 0) {
			verifyInputHolders();
			scanForNewInputHolders();
			checkProfiles();
		}
		for (ControlProfile cp : activeProfiles) {
			cp.tickRaw();
		}
		// TODO: Events
		/*
		 * for(ControlInputHolder ci : inputHolders) { Input input =
		 * ci.pullEvent(); }
		 */
		tick++;
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
		//TODO: Spread this out over control ticks a bit
		for(int i=0;i<inactiveProfiles.size();i++)
		{
			ControlProfile cp = inactiveProfiles.get(i);
			if(cp.checkDependencies())
			{
				inactiveProfiles.remove(cp);
				activeProfiles.add(cp);
				cp.loadBindings();
				continue;
			}
		}
		for(int i=0;i<activeProfiles.size();i++)
		{
			ControlProfile cp = activeProfiles.get(i);
			if(!cp.checkDependencies())
			{
				activeProfiles.remove(cp);
				inactiveProfiles.add(cp);
				continue;
			}
		}
	}

	public static void clearProfiles() {
		inactiveProfiles.clear();
		activeProfiles.clear();
	}

	public static void debugInit() {
		scanForNewInputHolders();
		
		ControlProfile cp = new ControlProfile("Testing Menu Bindings");
		CInputHolder cih = ControlsManager.getInputHolder("Mouse");
		COutputHolder coh = ControlsManager.getOutputHolder("Game Menu");
		
		System.out.println("CInputHolder="+cih.getName());
		System.out.println("COutputHolder="+coh.getName());
		
		DigitalInput mouseLeftButton = cih.getDigitalInput(0);
		DigitalOutput someAction = coh.getDigitalOutput(0);
		
		System.out.println("mouseLeftButton="+mouseLeftButton.getName());
		System.out.println("someAction="+someAction.getName());
		
		
		cp.addBinding(new BasicDigitalControlBinding(false, mouseLeftButton, someAction));
		//inactiveProfiles.add(cp);
		
		//checkProfiles();
		cp.save();
	}
}
