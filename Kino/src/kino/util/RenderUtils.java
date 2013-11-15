package kino.util;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class RenderUtils {
	public static void setup3DProjection(float FOV)
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(FOV,(float)Display.getWidth()/Display.getHeight(),0.1f,1000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	public static void setup2DProjection(float minX, float maxX, float minY, float maxY)
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(minX,maxX,  minY,maxY, -1.0f,1.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
}
