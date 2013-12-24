package kino.client.gui;

import kino.util.RenderUtils;

import org.lwjgl.opengl.GL11;

public class EGUIBox extends Element implements ScreenGUIHolder {
	public EGUIBox(GUI paramHolder) {
		super(paramHolder);
		
	}

	@Override
	public void draw(double interpolation) {
		RenderUtils.sendProjection_2D();
		RenderUtils.sendViewParameters_Normal();
		GL11.glLoadIdentity();
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(0.0f, 1.0f, 0.0f);
		GL11.glVertex2d(x+width, y+height);
		GL11.glVertex2d(x, y+height);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x+width, y);
		GL11.glEnd();
		
		GL11.glViewport(x, y, width, height);
		if(firstGUI!=null)
			firstGUI.draw(interpolation);
	}
	@Override
	public boolean onMouseDown(int button, int x, int y) {
		System.out.println("CLICK");
		return super.onMouseDown(button, x, y);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean wasResized() {
		return true;
	}

	// Gui Functions
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
				lastGUI.onUncover();
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
		gui.onUncover();
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
			gui.onUncover();
		}
	}
	@Override
	public boolean isSurfaceGUI(GUI gui)
	{
		return gui==lastGUI;
	}
}
