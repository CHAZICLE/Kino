package kino.client;

import java.io.File;

import kino.cache.Entity;


public abstract class Model {
	/*
	 * kbm - Kino Basic Model
	 * 
	 */
	public static Model createModelOrNull(String modelFilename){try{return createModel(modelFilename);}catch(Exception e){e.printStackTrace();return null;}}
	public static Model createModel(String modelFilename) throws Exception
	{
		File file = new File(modelFilename);
		if(!file.exists() || file.isDirectory() || !file.canRead())
			return null;
		
		switch(modelFilename.charAt(modelFilename.length()-2))
		{
			case 'b': return new MBasic(file);
		}
		return null;
	}
	/**
	 * Preloads the model
	 */
	public abstract void preload();
	/**
	 * Draws the model in place of the model
	 * @param entity
	 */
	public abstract void draw(Entity entity);
	/**
	 * Deletes the model
	 */
	public abstract void unload();
}