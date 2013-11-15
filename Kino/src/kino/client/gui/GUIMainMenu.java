package kino.client.gui;

import kino.client.ScreenGUIManager;
import kino.util.RenderUtils;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


public class GUIMainMenu extends GUI {
	public GUIMainMenu(ScreenGUIManager paramManager) {
		super(paramManager);
	}
	boolean main = false;
	
	@Override
	public boolean onKeyUp(int button) {
		if(button==Keyboard.KEY_RETURN)
		{
			main = true;
		}
		if(button==Keyboard.KEY_SPACE)
		{
			getManager().overwriteGUI(new GUIGame(getManager()));
		}
		return true;
	}
	@Override
	public void draw(double interpolation) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		RenderUtils.setup2DProjection(-10f,10f, -10f,10f);
		GL11.glLoadIdentity();
		GL11.glColor3f(0.0f,0.0f,1.0f);
		GL11.glLineWidth(1.0f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(+0.2f,+0.2f);
		GL11.glVertex2f(-0.2f,-0.2f);
		GL11.glVertex2f(-0.2f,+0.2f);
		GL11.glVertex2f(+0.2f,-0.2f);
		if(main)
		{
			GL11.glVertex2f(-4.0f,+0.0f);
			GL11.glVertex2f(+4.0f,+0.0f);
			GL11.glVertex2f(+0.0f,-4.0f);
			GL11.glVertex2f(+0.0f,+4.0f);
		}
		GL11.glEnd();
	}
}
