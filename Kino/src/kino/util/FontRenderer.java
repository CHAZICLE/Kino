package kino.util;

import org.lwjgl.opengl.GL11;

public class FontRenderer
{
	public static void drawText(String text, float charWidth, float charHeight, int maxWidth, int maxHeight, char delimiter)
	{
		GL11.glPushMatrix();
		for(int i=0;i<text.length();i++)
		{
			char c = text.charAt(i);
			if(c==delimiter)
			{
				
			}
			else
				GL11.glTranslatef(charWidth,0f,0f);
			
		}
		GL11.glPopMatrix();
	}
}
