package kino.cache;

import java.util.Iterator;

import org.lwjgl.util.vector.Matrix;

import kino.cache.BB.BoundingBox;
import kino.client.gui.GUIGame;
import kino.util.Vector3d;


public class Entity {
	
	public BoundingBox debugBoundingBox = null;
	public void onBBUpdate(BoundingBox bb)
	{
		
	}
	public boolean deleted = false;
	
	public boolean render = false;
	public boolean collider = false;
	
	public boolean movement = false;
	public boolean noclip = false;
	public boolean gravity = false;
	
	public World world;
	public Vector3d position = new Vector3d();
	public Vector3d motion = new Vector3d();
	public double mass = 1;
	public double vertRot = 0;
	public double horzRot = 0;
	
	private double E = 0.9;
	/*
	 * Collision Stages:
	 * Va*Ma+Vb*Mb=Ua*Ma+Ub*Mb
	 * Vb=Va+E(Ub-Ua)
	 * 
	 * Va*Ma+Vb*Mb=Ua*Ma+Ub*Mb
	 * Vb=Va+E(Ub-Ua)
	 * 
	 * E:
	 * 0-1 - Perfectly Elastic
	 *   0 - Stop
	 *-1-0 - Reverse Collision
	 * 
	 * 
	 * 
	 */
	/**
	 * Tick the entities position and motion values<br />
	 * - Adds gravity to momentum<br />
	 * - Adds momentum to position<br />
	 */
	public void tickMovement()
	{
		if(movement)
		{
			if(world.gravity!=null && world.enableGravity && gravity)
			{
				//motion.add(world.gravity.multiply(mass*mass));
				//world.gravity.load();
				motion.add(world.gravity);
			}
			if(!noclip && debugBoundingBox!=null)
			{
				debugBoundingBox.update(position);
				Iterator<Entity> it = world.eList_clips.iterator();
				while(it.hasNext())
				{
					Entity clip = it.next();
					if(clip!=this && clip.debugBoundingBox!=null)
					{
						clip.debugBoundingBox.update(clip.position);
						
						if(debugBoundingBox.doesCollideWith(clip.debugBoundingBox))
						{
							position.swap();
							debugBoundingBox.update(position);
							Vector3d n = debugBoundingBox.getCollisionNormalOf(clip.debugBoundingBox);
							position.swap();
							//System.out.println("REFLECTING: "+this+" hitting "+clip+" :: "+n);
							
							if(n!=null && n.dot(motion)<0)
							{
								double Ma = mass;
								double Mb = clip.mass;
								double Ua = motion.getMagnitude();
								double Ub = clip.motion.getMagnitude();
								double Va = (Ua*Ma+Ub*Mb)/(Ma+Mb+E*Mb*(Ub-Ua));
								double Vb = Va+E*(Ub-Ua);
								
								motion.reflect(n);
								clip.motion.reflect(n);
								
								clip.motion.add(motion);
								
								clip.motion.setMagnitude(Vb);
								motion.setMagnitude(Va);
								
								movement = !GUIGame.freezeme;
							}
						}
					}
				}
			}
			if(debugBoundingBox!=null)
			{
				debugBoundingBox.update(position);
				if(world.domain.doesCollideWith(debugBoundingBox))
				{
					Vector3d n;
					
					n = world.domain.getCollisionNormalOf(debugBoundingBox);
					if(n!=null && n.dot(motion)<0)
					{
						if(motion.getMagnitudeSquared()>0.01)
						{
							motion = motion.reflect(n).multiply(E);
						}
						else
						{
							motion = motion.flatten(n).multiply(E);
						}
					}
					
					/*n = world.domain.getCollisionBoundaryVector(debugBoundingBox);
					if(n!=null)
						position.set(n);*/
					
				}
			}
			position.store();
			position.add(motion);
			/*if(position.getY()<-3)
			{
				position.setY(-3);
				motion = motion.reflect(floorNormal);
				motion.multiply(0.6,1,0.6);
			}*/
		}
	}
	public void tick()
	{
		tickMovement();
	}
	public void teleport(Entity target)
	{
		position = target.position.multiplyMake(1);
	}
	public void stop()
	{
		motion.multiply(0);
	}
}
