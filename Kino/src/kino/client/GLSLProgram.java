package kino.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class GLSLProgram {
	private int program = 0;
	private int vertexShaderID = 0;
	private int fragmentShaderID = 0;
	private String shaderProgram = null;
	private int viewMatrixPosition = 0;
	public int texturePosition = -1;
	public GLSLProgram(String shaderProgram, boolean hasTexture) {
		this(shaderProgram);
		if(hasTexture)
			texturePosition = 0;
	}
	public GLSLProgram(String shaderProgram) {
		if(shaderProgram==null)
			throw new IllegalArgumentException("Shader prefix can't be null");
		this.shaderProgram = shaderProgram;
	}
	public void build()
	{
		vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vertexShaderID, readFileAsString("./res/shaders/"+shaderProgram+".vertexshader").toString());
		GL20.glCompileShader(vertexShaderID);
		
		if(GL20.glGetShaderi(vertexShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			System.out.println("Vertex shader not compiled");
			System.exit(-1);
	    }
		
		fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fragmentShaderID, readFileAsString("./res/shaders/"+shaderProgram+".fragmentshader").toString());
		GL20.glCompileShader(fragmentShaderID);
		
		if(GL20.glGetShaderi(vertexShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			System.out.println("Fragment shader not compiled");
			System.exit(-1);
	    }

		program = GL20.glCreateProgram();
		
		GL20.glAttachShader(program, vertexShaderID);
		GL20.glAttachShader(program, fragmentShaderID);
		
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);
		
		viewMatrixPosition = GL20.glGetUniformLocation(program, "viewMatrix");
		if(texturePosition==0)
			texturePosition = GL20.glGetAttribLocation(program, "vertexUV");
		
		if(GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
		{
			System.out.println("Program not linked");
			System.exit(-1);
	    }
	}
	public void activate()
	{
		GL20.glUseProgram(program);
	}
	public void sendView(Matrix4f viewMatrix)
	{
		FloatBuffer mfb = BufferUtils.createFloatBuffer(16);
		viewMatrix.store(mfb);
		mfb.rewind();
		GL20.glUniformMatrix4(viewMatrixPosition, false, mfb);
	}
	@Override
	protected void finalize() throws Throwable {
		GL20.glDeleteProgram(program);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		super.finalize();
	}
	public static StringBuilder readFileAsString(String target)
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(new File(target)));
			String ln;
			while((ln=br.readLine())!=null)
				sb.append(ln);
			br.close();
			return sb;
		}
		catch(Exception e)
		{
			try { br.close(); } catch(Exception idc) {}
			return null;
		}
	}
}
