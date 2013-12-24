package kino.client.gui;

import java.util.ArrayList;
import java.util.List;

public class GUI {
	private ScreenGUIHolder holder;
	public ScreenGUIHolder getHolder() { return holder; }
	public GUI(ScreenGUIHolder paramManager) {
		holder = paramManager;
	}
	public GUI previous;
	public GUI next;
	List<Element> elements = new ArrayList<Element>();
	Element focusElement;
	
	/**
	 * Called when the GUI opens
	 */
	public void onOpen()
	{
		for(Element e : elements)
			e.onOpen();
	}
	/**
	 * Called when the GUI closes
	 */
	public void onClose()
	{
		for(Element e : elements)
			e.onClose();
		elements = null;
		previous = null;
		next = null;
		focusElement = null;
	}
	/**
	 * Called when the GUI becomes the top most GUI
	 */
	public void onUncover()
	{
		
	}
	/**
	 * Called when another GUI opens over the top of this
	 */
	public void onCover()
	{
		if(focusElement!=null)
			focusElement.onBlur();
		focusElement = null;
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
			if(hasClickedOn(el,x,y) && el.onMouseUp(button, x, y))
				return true;
		}
		return false;
	}
	public boolean hasClickedOn(Element el, int x, int y)
	{
		return (x>=el.x && x<=el.x+el.width) && (y>=el.y && y<=el.y+el.height);
	}
	public boolean onKeyDown(int key)
	{
		if(focusElement!=null && focusElement.onKeyDown(key))
			return true;
		return false;
	}
	public boolean onKeyUp(int key)
	{
		if(focusElement!=null && focusElement.onKeyUp(key))
			return true;
		return false;
	}
	/**
	 * Called by the holder when it resizes
	 */
	public void onResize()
	{
		
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