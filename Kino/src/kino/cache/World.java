package kino.cache;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import kino.cache.BB.DomainBoundingBox;
import kino.util.Vector3d;

public class World {
	public World() {
		
	}
	private ConcurrentLinkedQueue<Entity> pendingEntities = new ConcurrentLinkedQueue<Entity>();
	public void addEntity(Entity ent) { pendingEntities.add(ent); }
	public boolean enableGravity = true;
	public Vector3d gravity = new Vector3d(0,-0.01,0);
	public DomainBoundingBox domain = new DomainBoundingBox(-10,-3.1,-10, 10,10,10);
	
	public LinkedList<Entity> eList_global = new LinkedList<Entity>();
	public LinkedList<Entity> eList_render = new LinkedList<Entity>();
	public LinkedList<Entity> eList_clips = new LinkedList<Entity>();
	
	public void addGlobalEntity(Entity ent)
	{
		eList_global.add(ent);
		if(ent.render)
		{
			synchronized(eList_render) { eList_render.add(ent); }
		}
		if(ent.collider)
		{
			synchronized(eList_clips) { eList_clips.add(ent); }
		}
	}
	public void tick()
	{
		while(!pendingEntities.isEmpty())
		{
			Entity pe = pendingEntities.poll();
			pe.world = this;
			addGlobalEntity(pe);
		}
		Iterator<Entity> it = eList_global.iterator();
		while(it.hasNext())
		{
			Entity ent = it.next();
			if(ent.deleted)
				it.remove();
			else
			{
				ent.tick();
				if(ent.deleted)
					it.remove();
			}
		}
	}
}
