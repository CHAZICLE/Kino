package kino.client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import kino.cache.Entity;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class MBasic extends Model {
	public int vertexBufferID = 0;
	public int indexBufferID = 0;
	public int colorBufferID = 0;
	public int indicesCount = 0;
	public MBasic() { }
	public MBasic(File theFile) throws IOException
	{
		DataInputStream dis;
		try
		{
			dis = new DataInputStream(new FileInputStream(theFile));
		}
		catch(Exception e)
		{
			return;
		}
		dis.readByte();
		// Read vertices
		int vertexCount = dis.readInt()*3;
		vertexBufferID = GL15.glGenBuffers();
		FloatBuffer vertexBuffer = (FloatBuffer)BufferUtils.createFloatBuffer(vertexCount);
		for(int i=0;i<vertexCount;i++)
			vertexBuffer.put(dis.readFloat());
		vertexBuffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		
		// Read indices
		indicesCount = dis.readInt()*3;
		indexBufferID = GL15.glGenBuffers();
		IntBuffer indexBuffer = (IntBuffer)BufferUtils.createIntBuffer(indicesCount);
		for(int i=0;i<indicesCount;i++)
			indexBuffer.put(dis.readInt());
		indexBuffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, indexBufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
		
		// Colour Buffer data
		colorBufferID = GL15.glGenBuffers();
		FloatBuffer colorBuffer = (FloatBuffer)BufferUtils.createFloatBuffer(vertexCount);
		for(int i=0;i<vertexCount;i++)
			colorBuffer.put(new Random().nextFloat());
		colorBuffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
	}
	@Override
	public void draw(Entity entity) {
		GL11.glPushMatrix();
		GL11.glScalef(16.0f, 16.0f, 16.0f);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferID);
		GL11.glVertexPointer(3,GL11.GL_FLOAT,0,0);
		
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferID);
		GL11.glColorPointer(3, GL11.GL_FLOAT,0,0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
		
		GL11.glPopMatrix();
	}
	@Override
	public void preload()
	{
		
	}
	@Override
	public void unload()
	{
		
	}
}