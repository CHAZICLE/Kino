package kino.client;

import kino.client.controls.MenuControls;
import kino.client.controls.MenuControls.State;
import kino.client.gui.Element;
import kino.client.gui.GUI;
import kino.client.gui.GUIMainMenu;
import kino.client.gui.ScreenGUIHolder;
import kino.util.RenderUtils;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
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
			while(MenuControls.next())
			{
				if(focusElement!=null && ((MenuControls.type==State.PRESS && focusElement.onControlDown(MenuControls.action)) || (MenuControls.type==State.RELEASE && focusElement.onControlUp(MenuControls.action))))
					continue;
				for(GUI current=lastGUI;current!=null;current=current.previous)
				{
					switch(MenuControls.type)
					{
					case PRESS_TARGET:
						current.doPress(MenuControls.index, MenuControls.x, MenuControls.y, MenuControls.action);
						break;
					case LOCATION_RELEASE:
						current.doRelease(MenuControls.index, MenuControls.x, MenuControls.y, MenuControls.action);
						break;
					case MOVE:
						current.doMove(MenuControls.index, MenuControls.x, MenuControls.y, MenuControls.action);
						break;
					case PRESS:
						current.onControlDown(MenuControls.action);
						break;
					case RELEASE:
						current.onControlUp(MenuControls.action);
						break;
					}
				}
			}
			// Game Controls
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			double interpolation = getDelta();
			RenderUtils.useColorShader();
			GL11.glViewport(getOffsetX(),getOffsetY(),getWidth(),getHeight());
			for(GUI current=firstGUI;current!=null;current=current.next)
			{
				current.draw(interpolation);
			}
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
		gui.doOpen();
		gui.previous.doCover();
		gui.doExpose();
	}
	@Override
	public void openRootGUI(GUI gui)
	{
		GUI current = firstGUI;
		while(current!=null)
		{
			current.doCover();
			current.doClose();
			current = current.next;
		}
		firstGUI = lastGUI = gui;
		if(gui!=null)
		{
			gui.doOpen();
			gui.doExpose();
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
	public int getWidth() {
		return Display.getWidth();
	}
	@Override
	public int getHeight() {
		return Display.getHeight();
	}
	@Override
	public boolean isSurfaceGUI(GUI gui) {
		return false;
	}
	private Element focusElement;
	@Override
	public Element blurElement() {
		if(focusElement!=null)
		{
			Element e = focusElement;
			focusElement.onBlur();
			focusElement = null;
			return e;
		}
		return null;
	}
	@Override
	public Element getFocusElement() {
		return focusElement;
	}
	@Override
	public Element focusElement(Element e) {
		Element e2 = blurElement();
		focusElement = e;
		return e2;
	}
}
