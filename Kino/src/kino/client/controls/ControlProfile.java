package kino.client.controls;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import kino.client.controls.io.AnalogInput;
import kino.client.controls.io.AnalogOutput;
import kino.client.controls.io.CInputHolder;
import kino.client.controls.io.COutputHolder;
import kino.client.controls.io.Input;
import kino.client.controls.io.Output;
import kino.client.controls.io.Put;
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
		name = "("+profileFile.getName()+")";
		loadMeta();
	}

	public ControlProfile(String name) {
		this.name = name;
		rawBindings = new ControlBinding[0];
		inputHolders = new HashSet<String>();
		outputHolders = new HashSet<String>();
		profileFile = new File("./res/profiles/"+name+".knp");
		metaLoaded = true;
		dataLoaded = true;
	}
	private String readString(DataInputStream dis, int len) throws IOException
	{
		byte[] data = new byte[len*2];
		dis.readFully(data);
		String s = new String(data,"UTF-16LE");
		return s;
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
			if (PROFILE_MAGIC != dis.readInt()) {
				// Unknown file type - Abandon Ship.
				dis.close();
				throw new IllegalArgumentException("Unknown profile filetype!");
			}
			if (CURRENT_PROFILE_VERSION != dis.readInt()) {
				// Unknown version - Abandon Ship.
				dis.close();
				throw new IllegalArgumentException("Unknown version! (TODO: Legacy version loader code)");
			}
			
			// Read the name
			name = readString(dis, dis.readInt());
			
			fileMetaEndPosition = 4+4+4 + name.length() * 2;
			// Read the input dependencies
			int depSize = dis.readInt();
			int len;
			inputHolders = new LinkedHashSet<String>();
			fileMetaEndPosition += 4;
			for (int i = 0; i < depSize; i++) {
				len = dis.readInt();
				inputHolders.add(readString(dis, len));
				fileMetaEndPosition += 4 + len * 2;

			}
			// Read the output dependencies
			depSize = dis.readInt();
			outputHolders = new LinkedHashSet<String>();
			fileMetaEndPosition += 4;
			for (int i = 0; i < depSize; i++) {
				len = dis.readInt();
				outputHolders.add(readString(dis, len));
				fileMetaEndPosition += 4 + len * 2;
			}
		} catch (FileNotFoundException e) {
			/* Shouldn't happen */
		} catch (IOException e) {
			e.printStackTrace();
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
			long temp = fileMetaEndPosition;
			while(temp>0)
				temp -= fis.skip(temp);
			
			
			DataInputStream dis = new DataInputStream(fis);
			rawBindings = new ControlBinding[dis.readInt()];
			for (int i = 0; i < rawBindings.length; i++) {
				rawBindings[i] = ControlBinding.createBinding(dis);
				// Read PUTS
				Put[] puts = new Put[dis.readInt()];
				for(int j=0;j<puts.length;j++)
				{
					int holderID = dis.readByte();
					int putID = dis.readInt();
					if(holderID<0)//Input
					{
						CInputHolder cih = ControlsManager.getInputHolder(((String)inputHolders.toArray()[-holderID-1]));
						if(putID<0)//Analog
							puts[j] = cih.getAnalogInput(-putID-1);
						else//Digital
							puts[j] = cih.getDigitalInput(putID);
					}
					else//Output
					{
						COutputHolder coh = ControlsManager.getOutputHolder(((String)outputHolders.toArray()[holderID]));
						if(putID<0)//Analog
							puts[j] = coh.getAnalogOutput(-putID-1);
						else//Digital
							puts[j] = coh.getDigitalOutput(putID);
					}
				}
				rawBindings[i].setPuts(puts);
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
			OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-16LE");
			// Write the META
			dos.writeInt(PROFILE_MAGIC);
			dos.writeInt(CURRENT_PROFILE_VERSION);
			dos.flush();
			// Write name
			dos.writeInt(name.length());
			osr.write(name);
			osr.flush();
			dos.writeInt(inputHolders.size());
			for (String ih : inputHolders) {
				dos.writeInt(ih.length());
				dos.flush();
				osr.write(ih);
				osr.flush();
			}
			dos.writeInt(outputHolders.size());
			for (String oh : outputHolders) {
				dos.writeInt(oh.length());
				dos.flush();
				osr.write(oh);
				osr.flush();
			}
			dos.writeInt(rawBindings.length);
			for (ControlBinding cb : rawBindings) {
				ControlBinding.writeBinding(dos, cb);
				Put[] puts = cb.getPuts();
				dos.writeInt(puts.length);
				for(Put put : puts)
				{
					if(put instanceof Input)
					{
						dos.writeByte(-getIndexOf(inputHolders, ((Input)put).getInputHolder().getName())-1);
						if(put instanceof AnalogInput)
							dos.writeInt(-put.getID()-1);
						else
							dos.writeInt(put.getID());
					}
					else if(put instanceof Output)
					{
						dos.writeByte(getIndexOf(outputHolders, ((Output)put).getOutputHolder().getName()));
						if(put instanceof AnalogOutput)
							dos.writeInt(-put.getID()-1);
						else
							dos.writeInt(put.getID());
					}
				}
			}
			dos.flush();
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private int getIndexOf(Set<String> someSet, String oj)
	{
		int c=0;
		for(Object ok : someSet)
		{
			if(ok.equals(oj))
				return c;
			c++;
		}
		return -1;
	}
	
	public void addBinding(ControlBinding binding)
	{
		rawBindings = Arrays.copyOf(rawBindings, rawBindings.length+1);
		rawBindings[rawBindings.length-1] = binding;
		for(Put put : binding.getPuts())
		{
			if(put instanceof Input)
				inputHolders.add(((Input)put).getInputHolder().getName());
			if(put instanceof Output)
				outputHolders.add(((Output)put).getOutputHolder().getName());
		}
	}
}
