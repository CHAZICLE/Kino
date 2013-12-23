package kino.client.gui;

import org.lwjgl.opengl.GL11;

import kino.util.RenderUtils;

public class EDebugBox extends Element {
	public EDebugBox(GUI paramHolder) {
		super(paramHolder);
	}

	@Override
	public void draw() {
		RenderUtils.setProjection_2D(0, gui.getManager().getWidth(), 0, gui.getManager().getHeight());
		RenderUtils.sendProjection_2D();
		RenderUtils.sendViewParameters_Normal();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x+width, y+height);
		GL11.glVertex2d(x, y+height);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x+width, y);
		GL11.glEnd();
	}
}
