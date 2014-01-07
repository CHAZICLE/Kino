package kino.client.gui;

import kino.util.RenderUtils;

import org.lwjgl.opengl.GL11;

public class EBasicButton extends Element {

	public EBasicButton(GUI paramHolder) {
		super(paramHolder);
	}
	@Override
	public void draw(double interpolation) {
		//System.out.println("DRAW BASIC BUTTON ("+x+","+y+","+width+","+height+")");
		RenderUtils.setProjection_2D(0,getHolder().getHolder().getWidth(),y,getHolder().getHolder().getHeight());
		RenderUtils.sendProjection_2D();
		RenderUtils.sendViewParameters_Normal();
		GL11.glColor3d(1.0f, 1.0f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x+width, y);
		GL11.glVertex2d(x+width, y+height);
		GL11.glVertex2d(x, y+height);
		GL11.glEnd();
	}
}
