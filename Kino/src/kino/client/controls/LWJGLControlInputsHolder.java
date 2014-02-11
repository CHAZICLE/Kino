package kino.client.controls;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class LWJGLControlInputsHolder extends ControlInputHolder {
	class LWJGLKeyboardDigitalInput extends DigitalInput {
		int key;
		@Override
		public boolean get() {
			return Keyboard.isKeyDown(key);
		}
		@Override
		public String getName() {
			return Keyboard.getKeyName(key);
		}
	}
	class LWJGLMouseButtonDigitalInput extends DigitalInput {
		int button;
		@Override
		public boolean get() {
			return Mouse.isButtonDown(button);
		}
		@Override
		public String getName() {
			return Mouse.getButtonName(button);
		}
	}
	private HashMap<Integer,DigitalInput> digitalInputCache = new HashMap<Integer,DigitalInput>();
	private AnalogInput[] analogInputCache = new AnalogInput[5];
	
	// Methods
	@Override
	public String getName() {
		return "Keyboard & Mouse";
	}
	@Override
	public int digitalSize() {
		return Keyboard.getKeyCount()+Mouse.getButtonCount();
	}
	@Override
	public int analogSize() {
		return 3;
	}
	@Override
	public String getDigitalName(int i) {
		if(0<=i && i<Keyboard.getKeyCount())
			return Keyboard.getKeyName(i);
		else
			return Mouse.getButtonName(i-Keyboard.getKeyCount());
	}
	@Override
	public String getAnalogName(int i) {
		switch(i)
		{
		case 0:
			return "Mouse X-Axis position";
		case 1:
			return "Mouse Y-Axis position";
		case 2:
			return "Mouse Relative Scroll";
		case 3:
			return "Mouse X-Axis speed";
		case 4:
			return "Mouse Y-Axis speed";
		}
		return null;
	}
	@Override
	public DigitalInput getDigital(int i) {
		DigitalInput di = null;
		if(0<=i && i<Keyboard.getKeyCount())
		{
			Map<Integer,Object> somefink = null;
			di = new LWJGLKeyboardDigitalInput();
			((LWJGLKeyboardDigitalInput)di).key = i;
		}
		else
		{
			di = new LWJGLMouseButtonDigitalInput();
			((LWJGLMouseButtonDigitalInput)di).button = i-Keyboard.getKeyCount();
		}
		return null;
	}
	@Override
	public AnalogInput getAnalog(final int i) {
		try
		{
			if(analogInputCache[i]!=null)
				return analogInputCache[i];
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		}
		switch(i)
		{
		case 0:
			return analogInputCache[0]=new AnalogInput(){
				@Override
				public double get() { return Mouse.getX(); }
				@Override
				public String getName() { return "Mouse X-Axis position"; }
			};
		case 1:
			return analogInputCache[1]=new AnalogInput(){
				@Override
				public double get() { return Mouse.getY(); }
				@Override
				public String getName() { return "Mouse Y-Axis position"; }
			};
		case 2:
			return analogInputCache[2]=new AnalogInput(){
				@Override
				public double get() { return Mouse.getDWheel(); }
				@Override
				public String getName() { return "Mouse Relative Scroll"; }
			};
		case 3:
			return analogInputCache[3]=new AnalogInput(){
				@Override
				public double get() { return Mouse.getX()-Display.getWidth()/2; }
				@Override
				public String getName() { return "Mouse X-Axis speed"; }
			};
		case 4:
			return analogInputCache[4]=new AnalogInput(){
				@Override
				public double get() { return Mouse.getY()-Display.getHeight()/2; }
				@Override
				public String getName() { return "Mouse Y-Axis speed"; }
			};
		default:
			return null;
		}
	}
	@Override
	public void processEvents(long tick) {
		while(Mouse.next())
		{
			
		}
	}
	@Override
	public DigitalInput readNewDigitalInput() {
		return null;
	}
	
	
}
