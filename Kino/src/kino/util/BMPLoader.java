package kino.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class BMPLoader {
	byte[] header = new byte[54];
	int dataPos;
	int width,height;
	int imageSize;
	byte[] data;
	public BMPLoader(File bmpFile) throws FileNotFoundException,IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(bmpFile));
		dis.readFully(header);
		
		for(byte b : header)
			System.out.print(b+".");
		System.out.println();
		
		System.out.println(this);
		if(header[0]!='B' || header[1]!='M')
			return;
		int temp = 0x07;
		dataPos = ((header[temp] & 0xFF)<<24) | ((header[temp+1] & 0xFF)<<16) | ((header[temp+2] & 0xFF)<<8) | ((header[temp+3] & 0xFF)<<0);
		temp = 0x03;
		imageSize = ((header[temp] & 0xFF)<<24) | ((header[temp+1] & 0xFF)<<16) | ((header[temp+2] & 0xFF)<<8) | ((header[temp+3] & 0xFF)<<0);
		temp = 0x11;
		width = ((header[temp] & 0xFF)<<24) | ((header[temp+1] & 0xFF)<<16) | ((header[temp+2] & 0xFF)<<8) | ((header[temp+3] & 0xFF)<<0);
		temp = 0x15;
		height = ((header[temp] & 0xFF)<<24) | ((header[temp+1] & 0xFF)<<16) | ((header[temp+2] & 0xFF)<<8) | ((header[temp+3] & 0xFF)<<0);
		
		
		if(imageSize==0)
			imageSize = width*height*3;
		if(dataPos==0)
			dataPos = 54;
		
		data = new byte[imageSize];
		dis.readFully(data);
		
		System.out.println(this);
		
		dis.close();
		
		ByteBuffer dataBuffer = BufferUtils.createByteBuffer(data.length);
		dataBuffer.put(data);
		dataBuffer.rewind();
		
		int texID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0,GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, dataBuffer);
	}
	@Override
	public String toString() {
		return dataPos+":"+width+"x"+height+"->"+imageSize;
	}
	public void useTexture()
	{
		
	}
}
