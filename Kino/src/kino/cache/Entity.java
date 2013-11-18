package kino.cache;

import java.util.Iterator;

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
	
	private double E = 0;
	/*
	 * Collision Stages:
	 * Va*Ma+Vb*Mb=Ua*Ma+Ub*Mb
	 * Vb=Va+E(Ub-Ua)
	 * 
	 * Find Va:
	 * Va=(Ua*Ma+Ub*Mb)/(Ma+Mb+E*Mb*(Ub-Ua))
	 * 
	 * Vb=(Ub*Mb+Ua*Ma)/(Mb+Ma+E*Ma*(Ua-Ub))
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
			if(gravity)
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
							clip.debugBoundingBox.update(clip.position);
							Vector3d n = debugBoundingBox.getCollisionNormalOf(clip.debugBoundingBox);
							position.swap();
							//System.out.println("REFLECTING: "+this+" hitting "+clip+" :: "+n);
							
							if(n!=null && n.dot(motion)<0)
							{
								/*if(motion.getMagnitudeSquared()>0.1)
									motion = motion.reflect(n);
								else
									motion = motion.flatten(n);
								if(clip.motion.getMagnitudeSquared()>0.1)
									clip.motion = clip.motion.reflect(n);
								else
									clip.motion = clip.motion.flatten(n);*/
								
								double Va = 0;
								double Ua = motion.getMagnitude();
								double Vb = 0;
								double Ub = clip.motion.getMagnitude();
								double Ma = mass;
								double Mb = clip.mass;
								Va=(Ua*Ma+Ub*Mb)/(Ma+Mb+E*Mb*(Ub-Ua));
								Vb=(Ub*Mb+Ua*Ma)/(Mb+Ma+E*Ma*(Ua-Ub));
								
								motion.setMagnitude(Va);
								motion.store();
								clip.motion = clip.motion.reflect(n).add(motion.multiply(-1)).setMagnitude(Vb);
								motion.load();
								//clip.motion.setMagnitude(Vb);
								
								/*motion.store();
								clip.motion.add(motion.multiply(-mass/clip.mass));
								motion.load();
								motion.multiply(clip.mass/mass);*/
								
								movement = !GUIGame.freezeme;
								/*n = debugBoundingBox.getCollisionBoundaryVector(clip.debugBoundingBox);
								if(n!=null)
									position.set(n);*/
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
						if(motion.getMagnitudeSquared()>0.005)
						{
							motion = motion.reflect(n).multiplex(0.7, 0.5, 0.7);
						}
						else
						{
							motion = motion.flatten(n).multiply(0.7);
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
