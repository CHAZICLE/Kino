package kino.client.gui;

import kino.client.controls.MenuControlGUIOutputs;



public class GUIMainMenu extends GUI {
	public GUIMainMenu(ScreenGUIHolder paramHolder) {
		super(paramHolder);
		EBasicButton ebb = new EBasicButton(this);
		ebb.x = 10;
		ebb.y = 10;
		ebb.width = 100;
		ebb.height = 100;
		addElement(ebb);
	}
	@Override
	public void onControlUp(MenuControlGUIOutputs.Action action) {
		if(action==MenuControlGUIOutputs.Action.SELECT)
		{
			getHolder().openRootGUI(new GUIGameView(getHolder()));
		}
	}
	
}
