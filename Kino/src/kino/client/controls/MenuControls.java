package kino.client.controls;

import kino.client.gui.GUI;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public abstract class MenuControls extends ControlBindingManager {
	public static GUI rootGUI;
	public static final DigitalOutput SELECT = new DigitalOutput()
	{
		@Override
		public void onPress() { rootGUI.onControlDown(Action.SELECT); }
		@Override
		public void onRelease() { rootGUI.onControlUp(Action.SELECT); }
	};
	public static final DigitalOutput MENU = new DigitalOutput()
	{
		@Override
		public void onPress() { rootGUI.onControlDown(Action.MENU); }
		@Override
		public void onRelease() { rootGUI.onControlUp(Action.MENU); }
	};
	public static final DigitalOutput BACK = new DigitalOutput()
	{
		@Override
		public void onPress() { rootGUI.onControlDown(Action.BACK); }
		@Override
		public void onRelease() { rootGUI.onControlUp(Action.BACK); }
	};
	public static final DigitalOutput NEXT = new DigitalOutput()
	{
		@Override
		public void onPress() { rootGUI.onControlDown(Action.NEXT); }
		@Override
		public void onRelease() { rootGUI.onControlUp(Action.NEXT); }
	};
	public static final DigitalOutput PREVIOUS = new DigitalOutput()
	{
		@Override
		public void onPress() { rootGUI.onControlDown(Action.PREVIOUS); }
		@Override
		public void onRelease() { rootGUI.onControlUp(Action.PREVIOUS); }
	};
	public static enum State {
		PRESS,RELEASE,PRESS_TARGET,RELEASE_TARGET,MOVE;
	}
	public static enum Action {
		SELECT,//Left click/Enter
		MENU,//Right click/context menu
		BACK,//Escape/backspace
		
		NEXT,//Down,Right,Space
		PREVIOUS,//Up,Left
		;
	}
	public static Action action;
	public static State type;
	public static int x;
	public static int y;
	public static byte index;
	public static boolean next()
	{
		if(Keyboard.next())
		{
			if(Keyboard.getEventKey()==Keyboard.KEY_RETURN)
				action = Action.SELECT;
			else if(Keyboard.getEventKey()==Keyboard.KEY_ESCAPE || Keyboard.getEventKey()==Keyboard.KEY_BACK)
				action = Action.BACK;
			else
				return false;
			
			if(Keyboard.getEventKeyState())
				type = State.PRESS;
			else
				type = State.RELEASE;
			return true;
		}
		else if(Mouse.next())
		{
			if(Mouse.getEventButton()==-1)
				type = State.MOVE;
			else if(Mouse.getEventButtonState())
				type = State.PRESS_TARGET;
			else if(!Mouse.getEventButtonState())
				type = State.RELEASE_TARGET;
			x = Mouse.getEventX();
			y = Mouse.getEventY();
			//TODO: Translate buttons into appropriate actions
			action = Action.SELECT;
			index = 0;
		}
		return false;
	}
}
