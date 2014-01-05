package kino.client.gui;

import java.util.ArrayList;
import java.util.List;

import kino.client.controls.Controls;

public abstract class GUI {
	private ScreenGUIHolder holder;
	public GUI previous;
	public GUI next;
	private boolean isOpen = false;
	private boolean isExposed = false;
	public GUI(ScreenGUIHolder paramHolder) {
		holder = paramHolder;
	}
	// Essentials
	public void draw(double interpolation)
	{
		drawElements(interpolation);
	}
	public ScreenGUIHolder getHolder()
	{
		return holder;
	}
	// Events
	public void onOpen()
	{
		isOpen = true;
		for(Element el : elements) el.onOpen();
	}
	public void onClose()
	{
		isExposed = false;
		isOpen = false;
		for(Element el : elements) el.onClose();
	}
	public void onExpose()
	{
		isExposed = true;
	}
	public void onCover()
	{
		isExposed = false;
	}
	public void onResize()
	{
		
	}
	public boolean onPress(byte index, int x, int y, kino.client.controls.Controls.ControlAction action)
	{
		boolean consumed = false;
		for(Element el : elements)
		{
			if(el.pointIsInside(x, y))
			{
				el.onPress(index, x, y);
				consumed = true;
			}
		}
		return consumed;
	}
	public boolean onMove(byte index, int x, int y, Controls.ControlAction action)
	{
		boolean consumed = false;
		for(Element el : elements)
		{
			if(el.pointIsInside(x, y))
			{
				el.onMove(index, x, y);
				consumed = true;
			}
		}
		return consumed;
	}
	public boolean onRelease(byte index, int x, int y, Controls.ControlAction action)
	{
		boolean consumed = false;
		for(Element el : elements)
		{
			if(el.pointIsInside(x, y))
			{
				el.onRelease(index, x, y);
				consumed = true;
			}
		}
		return consumed;
	}
	public boolean onControlDown(Controls.ControlAction type)
	{
		if(focusElement!=null)
		{
			focusElement.onControlDown(type);
			return true;
		}
		return false;
	}
	public boolean onControlUp(Controls.ControlAction type)
	{
		if(focusElement!=null)
		{
			focusElement.onControlUp(type);
			return true;
		}
		return false;
	}
	// States
	public boolean isOpen()
	{
		return isOpen;
	}
	public boolean isExposed()
	{
		return isExposed;
	}
	// Elements
	private List<Element> elements = new ArrayList<Element>();
	Element focusElement;
	public final void addElement(Element el)
	{
		elements.add(el);
		el.onAdded();
		if(isOpen)
			el.onOpen();
		
	}
	public final void removeElement(Element el)
	{
		elements.remove(el);
		if(focusElement==el)
			blurElement();
		el.onClose();
		el.onRemoved();
	}
	public final void drawElements(double interpolation)
	{
		for(Element e : elements)
			e.draw(interpolation);
	}
	public final void focusElement(Element el)
	{
		blurElement();
		focusElement = el;
		focusElement.onFocus();
	}
	public final void blurElement()
	{
		if(focusElement!=null)
		{
			focusElement.onBlur();
			focusElement = null;
		}
	}
	
}