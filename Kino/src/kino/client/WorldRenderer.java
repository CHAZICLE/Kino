package kino.client;

import java.util.Iterator;

import kino.cache.EKino;
import kino.cache.Entity;
import kino.cache.World;
import kino.test.MRandomTerrain;
import kino.test.MTestTexturedQuad;
import kino.util.SelfRenderingEntity;

import org.lwjgl.opengl.GL11;

public class WorldRenderer {
	public static void renderWorld(World world)
	{
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
	}
	public static void renderHUD(World world)
	{
		//textureTest.draw(null);
	}
	public static final Model kinoModel = Model.createModelOrNull("./res/model/kino_model.kbm");
	public static final Model terrainModel = new MRandomTerrain(1000,1000);
	public static final Model textureTest = new MTestTexturedQuad();
	
	public static void preload() {
		terrainModel.preload();
		//textureTest.preload();
	}
	public static void unload() {
		
	}
	
	
	
	
	
}