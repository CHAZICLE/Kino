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
import java.util.Iterator;
import java.util.Set;

public class ControlProfile implements Iterable<String> {
	class BindingsNotLoadedException extends IllegalStateException {
		private static final long serialVersionUID = -238891899449980600L;
	}

	private long fileMetaEndPosition = 0;
	private File profileFile;

	// META
	private boolean metaLoaded;
	private String name;
	private Set<String> dependencies = null;
	// DATA
	private boolean dataLoaded;
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
			char[] cbuf = new char[dis.readInt()];
			isr.read(cbuf);
			name = new String(cbuf);
			// Read the dependencies
			int depSize = dis.readInt();
			dependencies = new HashSet<String>();
			fileMetaEndPosition = 0;
			for (int i = 0; i < depSize; i++) {
				cbuf = new char[dis.readInt()];
				dependencies.add(new String(cbuf));
				fileMetaEndPosition += 4 + cbuf.length * 2;
			}
			fileMetaEndPosition += 4 + name.length() * 2;
		} catch (FileNotFoundException e) {
			/* Shouldn't happen */
		} catch (IOException e) {

		}
	}

	/**
	 * Gets the display name of this profile
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the number of dependencies this profile has
	 */
	public int getDependencySize() {
		return dependencies.size();
	}

	/**
	 * Gets the dependency iterator
	 */
	public Iterator<String> iterator() {
		return dependencies.iterator();
	}

	/**
	 * Determines if this control profile has a dependency
	 * 
	 * @param dep
	 *            The dependecy
	 * @return True if this profile has the specified dependency
	 */
	public boolean hasDependency(String dep) {
		return dependencies.contains(dep);
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
		for(ControlBinding cb : rawBindings)
		{
			
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
