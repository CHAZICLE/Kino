package kino.test;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import kino.cache.Entity;
import kino.client.GLSLProgram;
import kino.client.Model;
import kino.util.BMPLoader;
import kino.util.RenderUtils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class MTestTexturedQuad extends Model
{
	BMPLoader texture = null;
	int vertexBufferID;
	int uvBufferID;
	@Override
	public void preload() {
		try {
			texture = new BMPLoader("./res/texture/uvtemplate.bmp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Vertices
		vertexBufferID = GL15.glGenBuffers();
		FloatBuffer tempBuffer = (FloatBuffer)BufferUtils.createFloatBuffer(4*2);
		tempBuffer.put( 1.0f); tempBuffer.put( 1.0f);
		tempBuffer.put( 1.0f); tempBuffer.put(-1.0f);
		tempBuffer.put(-1.0f); tempBuffer.put(-1.0f);
		tempBuffer.put(-1.0f); tempBuffer.put( 1.0f);
		tempBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, tempBuffer, GL15.GL_STATIC_DRAW);
		// Texture Coordinates
		uvBufferID = GL15.glGenBuffers();
		FloatBuffer textureBuffer = (FloatBuffer)BufferUtils.createFloatBuffer(4*2);
		textureBuffer.put( 0.0f); textureBuffer.put( 0.0f);
		textureBuffer.put( 0.0f); textureBuffer.put( 1.0f);
		textureBuffer.put( 1.0f); textureBuffer.put( 1.0f);
		textureBuffer.put( 1.0f); textureBuffer.put( 0.0f);
		textureBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uvBufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureBuffer, GL15.GL_STATIC_DRAW);
	}
	
	@Override
	public void draw(Entity entity) {
		GLSLProgram prog = RenderUtils.useTextureTestShader();
		
		RenderUtils.setProjection_2D(-2f, 2f, -2f,2f);
		RenderUtils.sendProjection_2D();
		RenderUtils.sendViewParameters_Normal();
		
		texture.useTexture();
		
		GL20.glEnableVertexAttribArray(prog.texturePosition);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uvBufferID);
		GL20.glVertexAttribPointer(prog.texturePosition, 2, GL11.GL_FLOAT, false, 4*2*4, 0);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferID);
		GL11.glVertexPointer(2,GL11.GL_FLOAT,0,0);
		
		GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
		
	}

	@Override
	public void unload() {
		
	}
	
}
