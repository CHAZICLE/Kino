package kino.client.controls;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;

import kino.client.controls.mappings.ControlBinding;

public class ControlProfile {

	public static final int PROFILE_MAGIC = 2117645231;
	public static final int CURRENT_PROFILE_VERSION = 1;

	private long fileMetaEndPosition = 0;
	private File profileFile;
	// STATE
	private boolean metaLoaded;
	private boolean dataLoaded;
	private boolean active;
	public void setActive(boolean b){active = b;}
	public boolean isActive(){return active;}
	// META
	private String name;
	private HashSet<String> inputHolders;
	private HashSet<String> outputHolders;
	// DATA
	private ControlBinding[] rawBindings;

	public ControlProfile(File profileFile) {
		this.profileFile = profileFile;
		loadMeta();
	}

	public ControlProfile(String name) {
		this.name = name;
	}

	/**
	 * Loads the meta from the file
	 */
	public void loadMeta() {
		if (metaLoaded)
			return;
		metaLoaded = true;
		try (FileInputStream fis = new FileInputStream(profileFile)) {
			@SuppressWarnings("resource")
			DataInputStream dis = new DataInputStream(fis);
			if (PROFILE_MAGIC != dis.readInt()) {
				// Unknown file type - Abandon Ship.
				throw new IllegalArgumentException("Unknown profile filetype!");
			}
			if (CURRENT_PROFILE_VERSION != dis.readInt()) {
				// Unknown version - Abandon Ship.
				throw new IllegalArgumentException("Unknown version! (TODO: Legacy version loader code)");
			}
			@SuppressWarnings("resource")
			InputStreamReader isr = new InputStreamReader(fis, "UTF-16LE");
			// Read the name
			char[] cbuf = new char[dis.readInt()];
			isr.read(cbuf);
			name = new String(cbuf);
			fileMetaEndPosition += 12 + name.length() * 2;
			// Read the input dependencies
			int depSize = dis.read();
			inputHolders = new HashSet<String>();
			fileMetaEndPosition = 0;
			for (int i = 0; i < depSize; i++) {
				cbuf = new char[dis.readInt()];
				inputHolders.add(new String(cbuf));
				isr.read(cbuf);
				fileMetaEndPosition += 4 + cbuf.length * 2;
			}
			// Read the output dependencies
			depSize = dis.read();
			outputHolders = new HashSet<String>();
			fileMetaEndPosition = 0;
			for (int i = 0; i < depSize; i++) {
				cbuf = new char[dis.readInt()];
				outputHolders.add(new String(cbuf));
				isr.read(cbuf);
				fileMetaEndPosition += 4 + cbuf.length * 2;
			}
		} catch (FileNotFoundException e) {
			/* Shouldn't happen */
		} catch (IOException e) {

		}
	}

	public boolean checkDependencies() {
		for (String dep : inputHolders) {
			if (ControlsManager.getInputHolder(dep) == null)
				return false;
		}
		for (String dep : outputHolders) {
			if (ControlsManager.getOutputHolder(dep) == null)
				return false;
		}
		return true;
	}
	
	public boolean hasOutputDependency(String outputName) {
		return outputHolders.contains(outputName);
	}
	
	public boolean hasInputDependency(String inputName) {
		return inputHolders.contains(inputName);
	}

	/**
	 * Loads the bindings from the file NOTE: This methods MUST be called before
	 * any binding information is requested.
	 */
	public void loadBindings() {
		if (dataLoaded)
			return;
		try (FileInputStream fis = new FileInputStream(profileFile)) {
			fis.skip(fileMetaEndPosition);
			DataInputStream dis = new DataInputStream(fis);
			rawBindings = new ControlBinding[dis.readInt()];
			for (int i = 0; i < rawBindings.length; i++) {
				rawBindings[i] = ControlBinding.createBinding(dis);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the number of bindings
	 * 
	 * @return The number of bindings
	 * @throws BindingsNotLoadedException
	 *             If the bindings haven't been loaded
	 * @see ControlProfile#loadBindings()
	 */
	public int getBindingSize() {
		if (!dataLoaded)
			return -1;
		if (rawBindings == null)
			return 0;
		return rawBindings.length;
	}

	/**
	 * Gets the control binding at this point
	 */
	public ControlBinding getBinding(int index) {
		return rawBindings[index];
	}

	/**
	 * Called by ControlsManager to update raw values (Events are between the
	 * manager and the input)
	 */
	public void tickRaw() {
		for (ControlBinding cb : rawBindings) {
			cb.tickRaw();
		}
	}

	/**
	 * Rebuilds the dependency list and saves both the meta and control bindings
	 * to the file
	 */
	public void save() {
		try (FileOutputStream fos = new FileOutputStream(profileFile)) {
			DataOutputStream dos = new DataOutputStream(fos);
			OutputStreamWriter osr = new OutputStreamWriter(fos);
			// Write the META
			dos.writeInt(PROFILE_MAGIC);
			dos.writeInt(CURRENT_PROFILE_VERSION);
			inputHolders.clear();
			outputHolders.clear();
			for (ControlBinding cb : rawBindings) {
				cb.addInputsToSet(inputHolders);
				cb.addOutputsToSet(outputHolders);
			}
			dos.writeInt(inputHolders.size());
			for (String ih : inputHolders) {
				dos.writeInt(ih.length());
				osr.write(ih);
			}
			dos.writeInt(outputHolders.size());
			for (String oh : outputHolders) {
				dos.writeInt(oh.length());
				osr.write(oh);
			}
			dos.writeInt(rawBindings.length);
			for (ControlBinding cb : rawBindings) {
				ControlBinding.save(cb, dos);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

}
