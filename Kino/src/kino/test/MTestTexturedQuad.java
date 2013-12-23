package kino.test;

import java.io.IOException;

import kino.cache.Entity;
import kino.client.Model;
import kino.util.BMPLoader;

import org.lwjgl.opengl.GL11;

public class MTestTexturedQuad extends Model
{
	BMPLoader texture = null;
	@Override
	public void preload() {
		try {
			texture = new BMPLoader("./res/texture/uvtemplate.bmp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Entity entity) {
		GL11.glBegin(GL11.GL_QUADS);
		texture.useTexture();
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		
		GL11.glTexCoord2f(-1f, -1f);
		GL11.glVertex2d(-1, -1);
		GL11.glTexCoord2f(1f, -1f);
		GL11.glVertex2d(1, -1);
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2d(1, 1);
		GL11.glTexCoord2f(-1f, 1f);
		GL11.glVertex2d(-1, 1);
		GL11.glEnd();
	}

	@Override
	public void unload() {
		
	}
	
}
