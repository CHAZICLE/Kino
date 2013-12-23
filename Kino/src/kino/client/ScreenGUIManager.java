package kino.client;

import kino.client.gui.GUI;
import kino.client.gui.GUIMainMenu;
import kino.util.RenderUtils;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class ScreenGUIManager extends Thread implements ScreenGUIHolder {
	public ScreenGUIManager()
	{
		overwriteGUI(new GUIMainMenu(this));
	}
	GUI firstGUI;
	GUI lastGUI;
	
	
	
	@Override
	public void run()
	{
		startDisplay();
		RenderUtils.preload();
		WorldRenderer.preload();
		while(!Thread.interrupted() && !Display.isCloseRequested())
		{
			if(firstGUI==null || lastGUI==null)
				break;
			// Key events
			while(Keyboard.next())
			{
				int key = Keyboard.getEventKey();
				boolean state = Keyboard.getEventKeyState();
				GUI current = lastGUI;
				while(current!=null)
				{
					if((state && current.onKeyDown(key)) || (!state && current.onKeyUp(key)))
						break;
					current = current.previous;
				}
			}
			// Mouse events
			while(Mouse.next())
			{
				int x=Mouse.getEventX(),y=Mouse.getEventY(),button=Mouse.getEventButton();
				boolean state = Mouse.getEventButtonState();
				GUI current = lastGUI;
				while(current!=null)
				{
					if((state && current.onMouseDown(button, x, y)) || (!state && current.onMouseUp(button, x, y)))
						break;
					current = current.previous;
				}
			}
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GUI current = firstGUI;
			double interpolation = getDelta();
			RenderUtils.useColorShader();
			do
			{
				GL11.glViewport(
					0,0,
					Display.getWidth(), Display.getHeight()
				);
				current.draw(interpolation);
			}
			while((current = current.next)!=null);
			Display.update();
		}
		overwriteGUI(null);
		WorldRenderer.unload();
		RenderUtils.unload();
		stopDisplay();
	}
	private static long lastTime = 0;
	private static double getDelta()
	{
		return ((double)(-lastTime+(lastTime=getTime())))/1000;
	}
	private static long getTime()
	{
		return (Sys.getTime()*1000)/Sys.getTimerResolution();
	}
	void startDisplay()
	{
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			return;
		}
		Display.setTitle("Kino");
		Display.setInitialBackground(1.0f, 1.0f, 1.0f);
	}
	void stopDisplay()
	{
		try { Display.destroy(); } catch(Exception e) { }
	}
	public void closeGUI(GUI gui)
	{
		if(gui==firstGUI)
			firstGUI = gui.next;
		if(gui==lastGUI)
		{
			lastGUI = gui.previous;
			if(lastGUI!=null)
				lastGUI.onFocus();
		}
		if(gui.previous!=null)
			gui.previous.next = gui.next;
		if(gui.next!=null)
			gui.next.previous = gui.previous;
		gui.onBlur();
		gui.onClose();
	}
	public void stackGUI(GUI gui)
	{
		if(gui==null) return;
		lastGUI.next = gui;
		gui.previous = lastGUI;
		lastGUI = gui;
		gui.onOpen();
		gui.previous.onBlur();
		gui.onFocus();
	}
	public void overwriteGUI(GUI gui)
	{
		GUI current = firstGUI;
		while(current!=null)
		{
			current.onBlur();
			current.onClose();
			current = current.next;
		}
		firstGUI = lastGUI = gui;
		if(gui!=null)
		{
			gui.onOpen();
			gui.onFocus();
		}
	}
	public boolean isSurfaceGUI(GUI gui)
	{
		return gui==lastGUI;
	}
	@Override
	public int getWidth() {
		return Display.getWidth();
	}
	@Override
	public int getHeight() {
		return Display.getHeight();
	}
}
