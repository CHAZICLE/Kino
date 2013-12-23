package kino.client.gui;

import kino.client.ScreenGUIManager;
import kino.util.RenderUtils;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GUIEsc extends GUI {

	public GUIEsc(ScreenGUIManager paramManager) {
		super(paramManager);
	}
	@Override
	public void draw(double interpolation)
	{
		RenderUtils.setProjection_2D(0f,100f, 0f,100f);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable (GL11.GL_BLEND);
		GL11.glLoadIdentity();
		
		GL11.glColor4f(0f,0f,0f, 0.6f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(0.0f, 0.0f);
		GL11.glVertex2f(100.0f, 0.0f);
		GL11.glVertex2f(100.0f, 100.0f);
		GL11.glVertex2f(0.0f, 100.0f);
		
		GL11.glEnd();
		
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(0.0f, 0.0f);
		GL11.glVertex2f(100.0f, 100.0f);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
	}
	@Override
	public boolean onKeyUp(int key) {
		if(key==Keyboard.KEY_ESCAPE)
		{
			getManager().closeGUI(this);
		}
		return true;
	}
	@Override
	public void onClose() {
		
	}
}
