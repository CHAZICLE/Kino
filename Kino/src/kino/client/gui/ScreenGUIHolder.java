package kino.client.gui;

import kino.client.bindings.ControlGUIOutputs.Action;


public interface ScreenGUIHolder {
	
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract int getOffsetX();
	public abstract int getOffsetY();
	
	Element blurElement();
	Element getFocusElement();
	Element focusElement(Element e);
	
	public void closeGUI(GUI gui);
	public void openGUI(GUI gui);
	public void openRootGUI(GUI gui);
	public boolean isSurfaceGUI(GUI gui);
	
	public void onAction(Action action, boolean press);
}
