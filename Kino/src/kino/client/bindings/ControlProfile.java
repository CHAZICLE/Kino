package kino.client.bindings;

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

public class ControlProfile {

	private long fileMetaEndPosition = 0;
	private File profileFile;
	// STATE
	private boolean metaLoaded;
	private boolean dataLoaded;
	private boolean active;
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
			DataInputStream dis = new DataInputStream(fis);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-16LE");
			// Read the name
			char[] cbuf = new char[dis.readInt()];
			isr.read(cbuf);
			name = new String(cbuf);
			fileMetaEndPosition += 4 + name.length() * 2;
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
			InputStreamReader isr = new InputStreamReader(fis, "UTF-16LE");
			int numOfBindings = dis.readInt();
			rawBindings = new ControlBinding[numOfBindings];
			for (int i = 0; i < numOfBindings; i++) {
				rawBindings[i] = ControlBinding.load(dis);
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
	public int getBindingSize() throws BindingsNotLoadedException {
		if (!dataLoaded)
			throw new BindingsNotLoadedException();
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
	public void tick() {
		for (ControlBinding cb : rawBindings) {

		}
	}

	/**
	 * 
	 */
	public void save() {
		try (FileOutputStream fos = new FileOutputStream(profileFile)) {
			DataOutputStream dos = new DataOutputStream(fos);
			OutputStreamWriter osr = new OutputStreamWriter(fos);
			// Write the META
			dos.writeInt(name.length());
			osr.write(name);
			dos.writeInt(dependencies.size());
			for (String deps : dependencies) {
				dos.writeInt(deps.length());
				osr.write(deps);
			}
			// Write the DATA
			for (ControlBinding cb : rawBindings) {

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
