package kino.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import kino.cache.EKino;
import kino.cache.Entity;
import kino.cache.World;
import kino.test.MRandomTerrain;
import kino.util.SelfRenderingEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class WorldRenderer {
	public static void render(World world)
	{
		GL20.glUseProgram(shaderProgramID);
		// Render terrain
		GL11.glPushMatrix();
		GL11.glTranslated(0,-6,0);
		GL11.glScaled(5f,1f,5f);
		terrainModel.draw(null);
		GL11.glPopMatrix();
		
		Iterator<Entity> it = world.eList_render.iterator();
		while(it.hasNext())
		{
			Entity ent = it.next();
			if(ent.deleted || !ent.render)
			{
				it.remove();
				continue;
			}
			if(ent instanceof SelfRenderingEntity)
			{
				((SelfRenderingEntity)ent).render();
			}
			else if(ent instanceof EKino && kinoModel!=null)
			{
				GL11.glPushMatrix();
				GL11.glTranslated(ent.position.getX(), ent.position.getY(), ent.position.getZ());
				GL11.glRotated(270+ent.horzRot, 0.0f,1.0f,0.0f);
				GL11.glRotated(ent.vertRot, 0.0f,0.0f,1.0f);
				GL11.glScaled(0.1f, 0.1f, 0.1f);
				kinoModel.draw(ent);
				GL11.glPopMatrix();
			}
		}
		GL20.glUseProgram(0);
	}
	public static final Model kinoModel = Model.createModelOrNull("./res/model/kino_model.kbm");
	public static final Model terrainModel = new MRandomTerrain(1000,1000);
	
	
	private static int shaderProgramID = 0;
	private static int vertexShaderID = 0;
	private static int fragmentShaderID = 0;
	public static void buildShaders()
	{
		shaderProgramID = GL20.glCreateProgram();
		vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		
		GL20.glShaderSource(vertexShaderID, readFileAsString("./res/demoVertexShader"));
		GL20.glCompileShader(vertexShaderID);
		
		GL20.glShaderSource(fragmentShaderID, readFileAsString("./res/demoFragmentShader"));
		GL20.glCompileShader(fragmentShaderID);
		
		GL20.glAttachShader(shaderProgramID, vertexShaderID);
		GL20.glAttachShader(shaderProgramID, fragmentShaderID);
		
		GL20.glLinkProgram(shaderProgramID);
		GL20.glValidateProgram(shaderProgramID);
		
		terrainModel.preload();
	}
	public static void trashShaders()
	{
		GL20.glDeleteProgram(shaderProgramID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
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