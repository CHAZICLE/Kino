package kino.client.gui;

import kino.client.controls.MenuControlGUIOutputs;



public abstract class Element {
	int x,y,width,height;
	private GUI gui;
	private boolean isDisabled;
	public Element(GUI paramHolder)
	{
		gui = paramHolder;
	}
	// Essentials
	public void draw(double interpolation)
	{
		
	}
	public GUI getHolder()
	{
		return gui;
	}
	public boolean shouldFocusOnPress()
	{
		return false;
	}
	public boolean isDisabled()
	{
		return isDisabled;
	}
	public void setDisabled(boolean disabled)
	{
		if(isDisabled!=disabled)
		{
			isDisabled = disabled;
			onDisableStateChanged();
		}
	}
	boolean pointIsInside(int x, int y)
	{
		return x>=this.x && y>=this.y && x<=this.x+this.width && y<=this.y+this.height;
	}
	
	// Events
	public void onOpen(){}
	public void onClose(){}
	public void onAdded(){}
	public void onRemoved(){}
	public void onFocus(){}
	public void onFocus(int x, int y){}
	public void onBlur(){}
	public void onResize(){}
	public void onPress(int x, int y){}
	public void onMove(int x, int y){}
	public void onRelease(int x, int y){getHolder().getHolder().focusElement(this);}
	public boolean onControlDown(MenuControlGUIOutputs.Action type){return false;}
	public boolean onControlUp(MenuControlGUIOutputs.Action type){return false;}
	public void onDisableStateChanged(){}
}
