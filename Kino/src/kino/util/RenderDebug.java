package kino.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class RenderDebug {
	public static void renderAxisMarks()
	{
		GL11.glLineWidth(10.0f);
		GL11.glBegin(GL11.GL_LINES);
		
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		GL11.glVertex3f( 0.0f, 0.0f, 0.0f);
		GL11.glVertex3f(100.0f, 0.0f, 0.0f);
		GL11.glColor3f(0.0f, 1.0f, 0.0f);
		GL11.glVertex3f( 0.0f, 0.0f, 0.0f);
		GL11.glVertex3f( 0.0f,100.0f, 0.0f);
		GL11.glColor3f(0.0f, 0.0f, 1.0f);
		GL11.glVertex3f( 0.0f, 0.0f, 0.0f);
		GL11.glVertex3f( 0.0f, 0.0f,100.0f);
		
		GL11.glEnd();
	}
	public static void renderFloor()
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		GL11.glColor3f(0.4f,0.4f,0.4f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(-10.0f,-3.1f,+10.0f);
		GL11.glVertex3f(+10.0f,-3.1f,+10.0f);
		GL11.glVertex3f(+10.0f,-3.1f,-10.0f);
		GL11.glVertex3f(-10.0f,-3.1f,-10.0f);
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	public static void renderCube()
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(10f, 0f, 0f);
		GL11.glBegin(GL11.GL_QUADS);
		
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(texID);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		
		GL11.glColor3f(1.0f,0.0f,1.0f);
		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex3f( 3.0f, -3.0f,-3.0f);
		GL11.glTexCoord2f(0f, 1f);
		GL11.glVertex3f( 3.0f, -3.0f, 3.0f);
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex3f(-3.0f, -3.0f, 3.0f);
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex3f(-3.0f, -3.0f,-3.0f);
		
		GL11.glColor3f(0.0f,1.0f,0.0f);
		GL11.glVertex3f( 3.0f, 3.0f,-3.0f);
		GL11.glVertex3f( 3.0f, 3.0f, 3.0f);
		GL11.glVertex3f(-3.0f, 3.0f, 3.0f);
		GL11.glVertex3f(-3.0f, 3.0f,-3.0f);
		
		GL11.glColor3f(1.0f,0.0f,0.0f);
		GL11.glVertex3f( 3.0f, 3.0f,-3.0f);
		GL11.glVertex3f( 3.0f, 3.0f, 3.0f);
		GL11.glVertex3f( 3.0f,-3.0f, 3.0f);
		GL11.glVertex3f( 3.0f,-3.0f,-3.0f);
		
		GL11.glColor3f(0.0f,1.0f,1.0f);
		GL11.glVertex3f(-3.0f, 3.0f,-3.0f);
		GL11.glVertex3f(-3.0f, 3.0f, 3.0f);
		GL11.glVertex3f(-3.0f,-3.0f, 3.0f);
		GL11.glVertex3f(-3.0f,-3.0f,-3.0f);
		
		GL11.glColor3f(0.0f,0.0f,1.0f);
		GL11.glVertex3f( 3.0f,-3.0f, 3.0f);
		GL11.glVertex3f( 3.0f, 3.0f, 3.0f);
		GL11.glVertex3f(-3.0f, 3.0f, 3.0f);
		GL11.glVertex3f(-3.0f,-3.0f, 3.0f);
		
		GL11.glColor3f(1.0f,1.0f,0.0f);
		GL11.glVertex3f( 3.0f,-3.0f, -3.0f);
		GL11.glVertex3f( 3.0f, 3.0f, -3.0f);
		GL11.glVertex3f(-3.0f, 3.0f, -3.0f);
		GL11.glVertex3f(-3.0f,-3.0f, -3.0f);
		
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	public static void renderAxisGrid()
	{
		GL11.glLineWidth(1.0f);
		GL11.glBegin(GL11.GL_LINES);
		int mag = 10;
		int step = 1;
		for(int a=-mag;a<=mag;a+=step)
		{
			for(int b=-mag;b<=mag;b+=step)
			{
				GL11.glColor3f(1.0f, 0.0f, 0.0f);
				GL11.glVertex3f(-mag, a, b);
				GL11.glVertex3f(mag, a, b);
				
				GL11.glColor3f(0.0f, 1.0f, 0.0f);
				GL11.glVertex3f(a, -mag, b);
				GL11.glVertex3f(a, mag, b);
				
				GL11.glColor3f(0.0f, 0.0f, 1.0f);
				GL11.glVertex3f(a, b, -mag);
				GL11.glVertex3f(a, b, mag);
			}
		}
		GL11.glEnd();
	}
	public static void renderCrossHair()
	{
		GL11.glColor3f(0.0f,0.0f,1.0f);
		GL11.glLineWidth(1.0f);
		
		GL11.glBegin(GL11.GL_LINES);
		
		GL11.glVertex2f(+5f,+5f);
		GL11.glVertex2f(-5f,-5f);
		GL11.glVertex2f(-5f,+5f);
		GL11.glVertex2f(+5f,-5f);
		
		GL11.glEnd();
	}
}
