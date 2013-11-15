package kino.client.gui;

import java.util.ArrayList;
import java.util.List;

import kino.client.ScreenGUIManager;

public class GUI {
	private ScreenGUIManager manager;
	public ScreenGUIManager getManager() { return manager; }
	public GUI(ScreenGUIManager paramManager) {
		manager = paramManager;
	}
	public GUI previous;
	public GUI next;
	List<Element> elements = new ArrayList<Element>();
	
	/**
	 * Called when the GUI opens
	 */
	public void onOpen()
	{
		
	}
	/**
	 * Called when the GUI closes
	 */
	public void onClose()
	{
		elements = null;
		previous = null;
		next = null;
	}
	/**
	 * Called when the GUI becomes the top most GUI
	 */
	public void onFocus()
	{
		
	}
	/**
	 * Called when another GUI opens over the top of this
	 */
	public void onBlur()
	{
		
	}
	public boolean onMouseDown(int button, int x, int y)
	{
		for(Element el : elements)
		{
			if((x>=el.x && x<=el.x+el.width) && (y>=el.y && y<=el.y+el.height) && el.onMouseDown(button, x, y))
				return true;
		}
		return false;
	}
	public boolean onMouseUp(int button, int x, int y)
	{
		for(Element el : elements)
		{
			if((x>=el.x && x<=el.x+el.width) && (y>=el.y && y<=el.y+el.height) && el.onMouseUp(button, x, y))
				return true;
		}
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
	/**
	 * Called by the render thread to draw the GUI
	 * 
	 * @param interpolation The fraction of a second the tick is through
	 */
	public void draw(double interpolation)
	{
		
	}
}