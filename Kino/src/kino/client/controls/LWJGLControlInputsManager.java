package kino.client.controls;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class LWJGLControlInputsManager implements ControlInputManager {
	public int storedKey = -1;
	public boolean keystate = false;
	public int mouseButton = -1;
	public boolean mouseButtonState = false;
	public int mouseX,mouseY,mouseDX,mouseDY,mouseDZ;
	
	/**
	 * Called at the start of every control thread tick to post keyboard/mouse events if enabled
	 */
	public boolean read()
	{
		if(Keyboard.next())
		{
			storedKey = Keyboard.getEventKey();
			keystate = Keyboard.getEventKeyState();
			return true;
		}
		else if(Mouse.next())
		{
			mouseButton = Mouse.getEventButton();
			mouseButtonState = Mouse.getEventButtonState();
			mouseX = Mouse.getEventX();
			mouseY = Mouse.getEventY();
			mouseDX = Mouse.getEventDX();
			mouseDY = Mouse.getEventDY();
			mouseDZ = Mouse.getEventDWheel();
			return true;
		}
		return false;
	}
	public String[] getDigitalInputNames()
	{
		LinkedList<String> names = new LinkedList<String>();
		Field[] keys = Keyboard.class.getFields();
		for(Field key : keys)
		{
			if(key.getName().startsWith("KEY_"))
				names.add(key.getName());
		}
		return (String[]) names.toArray();
	}
	public DigitalInput getDigitalInput(int index)
	{
		try
		{
			Field key = Keyboard.class.getFields()[index];
			LWJGLKeyboardDigitalInput ldi = new LWJGLKeyboardDigitalInput();
			ldi.key = key.getInt(null);
			return ldi;
		}
		catch(ArrayIndexOutOfBoundsException|IllegalArgumentException|IllegalAccessException e)
		{
			throw new IllegalArgumentException("Unknown input");
		}
	}
	class LWJGLKeyboardDigitalInput extends DigitalInput{
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
	class LWJGLMouseButtonDigitalInput extends DigitalInput{
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
	@Override
	public void pollEvents() {
		while(read())
		{
			
		}
	}
	Map<Integer,LWJGLKeyboardDigitalInput> keymapCache = new HashMap<Integer,LWJGLKeyboardDigitalInput>();
	LWJGLMouseButtonDigitalInput[] storedButtonInputs = new LWJGLMouseButtonDigitalInput[Mouse.getButtonCount()];
	@Override
	public DigitalInput readInput() {
		while(Keyboard.next())
		{
			if(Keyboard.getEventKeyState())
			{
				int key = Keyboard.getEventKey();
				LWJGLKeyboardDigitalInput ldi = keymapCache.get(Integer.valueOf(key));
				if(ldi==null)
				{
					keymapCache.put(Integer.valueOf(key),ldi = new LWJGLKeyboardDigitalInput());
					ldi.key = Keyboard.getEventKey();
				}
				return ldi;
			}
		}
		while(Mouse.next())
		{
			if(Mouse.getEventButton()>=0 && Mouse.getEventButtonState())//Some button => DigitalInput
			{
				int button = Mouse.getEventButton();
				LWJGLMouseButtonDigitalInput lmbdi = storedButtonInputs[button];
				if(lmbdi==null)
				{
					storedButtonInputs[button] = lmbdi = new LWJGLMouseButtonDigitalInput();
					lmbdi.button = button;
				}
				return lmbdi;
			}
		}
		return null;
	}
}
