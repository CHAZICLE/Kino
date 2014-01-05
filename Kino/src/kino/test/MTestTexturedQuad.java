package kino.test;

import java.io.IOException;

import kino.cache.Entity;
import kino.client.Model;
import kino.util.BMPLoader;
import kino.util.RenderUtils;

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
		RenderUtils.useColorShader();
		RenderUtils.setProjection_2D(-2f, 2f, -2f,2f);
		RenderUtils.sendProjection_2D();
		RenderUtils.sendViewParameters_Normal();
		
		texture.useTexture();
		GL11.glBegin(GL11.GL_QUADS);
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
