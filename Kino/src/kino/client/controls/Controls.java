package kino.client.controls;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Controls {
	public static enum ControlType {
		PRESS,RELEASE,LOCATION_PRESS,LOCATION_RELEASE,MOVE;
	}
	public static enum ControlAction {
		SELECT,//Left click/Enter
		MENU,//Right click/context menu
		BACK,//Escape/backspace
		NEXT,//Down,Right,Space
		PREVIOUS,//Up,Left
		;
	}
	public static ControlAction action;
	public static ControlType type;
	public static int x;
	public static int y;
	public static byte index;
	public static boolean next()
	{
		if(Keyboard.next())
		{
			if(Keyboard.getEventKey()==Keyboard.KEY_RETURN)
				action = ControlAction.SELECT;
			else if(Keyboard.getEventKey()==Keyboard.KEY_ESCAPE || Keyboard.getEventKey()==Keyboard.KEY_BACK)
				action = ControlAction.BACK;
			else
				return false;
			
			if(Keyboard.getEventKeyState())
				type = ControlType.PRESS;
			else
				type = ControlType.RELEASE;
			return true;
		}
		else if(Mouse.next())
		{
			if(Mouse.getEventButton()==-1)
				type = ControlType.MOVE;
			else if(Mouse.getEventButtonState())
				type = ControlType.LOCATION_PRESS;
			else if(!Mouse.getEventButtonState())
				type = ControlType.LOCATION_RELEASE;
			x = Mouse.getEventX();
			y = Mouse.getEventY();
			action = ControlAction.SELECT;
			index = 0;
		}
		return false;
	}
}
