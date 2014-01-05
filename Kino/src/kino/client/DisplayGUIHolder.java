package kino.client;

import kino.client.controls.Controls;
import kino.client.gui.GUI;
import kino.client.gui.GUIMainMenu;
import kino.client.gui.ScreenGUIHolder;
import kino.util.RenderUtils;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class DisplayGUIHolder extends Thread implements ScreenGUIHolder {
	public DisplayGUIHolder()
	{
		openRootGUI(new GUIMainMenu(this));
	}
	@Override
	public void run()
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
		
		RenderUtils.preload();
		WorldRenderer.preload();
		while(!Thread.interrupted() && !Display.isCloseRequested())
		{
			if(firstGUI==null || lastGUI==null)
				break;
			// Controls
			while(Controls.next())
			{
				for(GUI current=lastGUI;current.previous!=null;current=current.previous)
				{
					switch(Controls.type)
					{
					case LOCATION_PRESS:
						current.onPress(Controls.index, Controls.x, Controls.y, Controls.action);
						break;
					case LOCATION_RELEASE:
						current.onRelease(Controls.index, Controls.x, Controls.y, Controls.action);
						break;
					case MOVE:
						current.onMove(Controls.index, Controls.x, Controls.y, Controls.action);
						break;
					case PRESS:
						current.onControlDown(Controls.action);
						break;
					case RELEASE:
						current.onControlUp(Controls.action);
						break;
					}
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
		openRootGUI(null);
		WorldRenderer.unload();
		RenderUtils.unload();
		try { Display.destroy(); } catch(Exception e) { }
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
	
	@Override
	public int getWidth() {
		return Display.getWidth();
	}
	@Override
	public int getHeight() {
		return Display.getHeight();
	}
	
	// GUI
	protected GUI firstGUI = null;
	protected GUI lastGUI = null;
	@Override
	public void closeGUI(GUI gui)
	{
		if(gui==firstGUI)
			firstGUI = gui.next;
		if(gui==lastGUI)
		{
			lastGUI = gui.previous;
			if(lastGUI!=null)
				lastGUI.onExpose();
		}
		if(gui.previous!=null)
			gui.previous.next = gui.next;
		if(gui.next!=null)
			gui.next.previous = gui.previous;
		gui.onCover();
		gui.onClose();
	}
	@Override
	public void openGUI(GUI gui)
	{
		if(gui==null) return;
		lastGUI.next = gui;
		gui.previous = lastGUI;
		lastGUI = gui;
		gui.onOpen();
		gui.previous.onCover();
		gui.onExpose();
	}
	@Override
	public void openRootGUI(GUI gui)
	{
		GUI current = firstGUI;
		while(current!=null)
		{
			current.onCover();
			current.onClose();
			current = current.next;
		}
		firstGUI = lastGUI = gui;
		if(gui!=null)
		{
			gui.onOpen();
			gui.onExpose();
		}
	}
	@Override
	public int getOffsetX() {
		return 0;
	}
	@Override
	public int getOffsetY() {
		return 0;
	}
	@Override
	public boolean isSurfaceGUI(GUI gui) {
		return false;
	}
}
