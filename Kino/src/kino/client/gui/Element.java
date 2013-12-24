package kino.client.gui;


public class Element {
	public Element(GUI paramHolder)
	{
		gui = paramHolder;
	}
	int x,y,width,height;
	GUI gui;
	boolean hasFocus;
	
	/**
	 * Called when the GUI opens
	 */
	public void onOpen(){};
	/**
	 * Called when the GUI closes
	 */
	public void onClose(){};
	public final void focus()
	{
		if(gui.focusElement!=null)
			gui.focusElement.onBlur();
		gui.focusElement = this;
		hasFocus = true;
		onFocus();
	}
	public final void blur()
	{
		if(gui.focusElement==this)
		{
			gui.focusElement.onBlur();
			gui.focusElement = null;
			hasFocus = false;
		}
	}
	public void onFocus(){};
	public void onBlur(){};
	public void onResize(){}
	public boolean onMouseDown(int button, int x, int y)
	{
		return false;
	}
	public boolean onMouseUp(int button, int x, int y)
	{
		return false;
	}
	public boolean onKeyDown(int key)
	{
		return false;
	}
	public boolean onKeyUp(int key)
	{
		return false;
	}
	public void draw(double interpolation)
	{
		
	}
	public GUI getHolder()
	{
		return gui;
	}
}
