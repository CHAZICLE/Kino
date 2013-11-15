package kino.cache.BB;

import kino.cache.Entity;
import kino.util.Vector3d;

public class AxisAlignedBoundingBox extends BoundingBox {
	public AxisAlignedBoundingBox(Entity ent, double w, double h, double l) {
		super(ent);
		width = w;
		height = h;
		length = l;
		hwidth = width/2;
		hheight = height/2;
		hlength = length/2;
	}
	@Override
	public void update(double x, double y, double z)
	{
		minX = x-hwidth;
		minY = y-hheight;
		minZ = z-hlength;
		maxX = x+hwidth;
		maxY = y+hheight;
		maxZ = z+hlength;
	}

	double width,height,length;
	double hwidth,hheight,hlength;
	
	double minX,minY,minZ;
	double maxX,maxY,maxZ;
	
	@Override
	public boolean doesCollideWith(BoundingBox bb) {
		if(bb instanceof AxisAlignedBoundingBox)
		{
			AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox)bb;
			if(	this.maxX < aabb.minX ||
				this.minX > aabb.maxX ||
				
				this.maxY < aabb.minY || 
				this.minY > aabb.maxY ||
				
				this.maxZ < aabb.minZ || 
				this.minZ > aabb.maxZ
				)
			{
				return false;
			}
			return true;
		}
		else if(bb instanceof RadialBoundingBox)
		{
			RadialBoundingBox rbb = (RadialBoundingBox)bb;
			return rbb.doesCollideWith(this);
		}
		else if(bb instanceof DomainBoundingBox)
		{
			DomainBoundingBox dbb = (DomainBoundingBox)bb;
			return dbb.doesCollideWith(this);
		}
		return false;
	}
	@Override
	public Vector3d getCollisionNormalOf(BoundingBox bb)
	{
		if(bb instanceof AxisAlignedBoundingBox)
		{
			AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox)bb;
			
			double dx = ((minX+maxX)/2-(aabb.minX+aabb.maxX)/2);
			double dy = ((minY+maxY)/2-(aabb.minY+aabb.maxY)/2);
			double dz = ((minZ+maxZ)/2-(aabb.minZ+aabb.maxZ)/2);
			double adx = Math.abs(dx);
			double ady = Math.abs(dy);
			double adz = Math.abs(dz);
			
			if(adx>=ady*aabb.width/aabb.height && adx>=adz*aabb.width/aabb.length)
			{
				vectorCache.setX(dx>0 ? 1 : -1);
				vectorCache.setY(0);
				vectorCache.setZ(0);
				return vectorCache;
			}
			if(ady>=adx*aabb.height/aabb.width && ady>=adz*aabb.height/aabb.length)
			{
				vectorCache.setX(0);
				vectorCache.setY(dy>0 ? 1 : -1);
				vectorCache.setZ(0);
				return vectorCache;
			}
			if(adz>=adx*aabb.length/aabb.width && adz>=ady*aabb.length/aabb.height)
			{
				vectorCache.setX(0);
				vectorCache.setY(0);
				vectorCache.setZ(dz>0 ? 1 : -1);
				return vectorCache;
			}
		}
		else if(bb instanceof RadialBoundingBox)
		{
			// Cube crashing into a Sphere
			RadialBoundingBox rbb = (RadialBoundingBox)bb;
			vectorCache.setX((minX+maxX)/2-rbb.x);
			vectorCache.setY((minY+maxY)/2-rbb.y);
			vectorCache.setZ((minZ+maxZ)/2-rbb.z);
			return vectorCache;
		}
		else if(bb instanceof DomainBoundingBox)
		{
			DomainBoundingBox dbb = (DomainBoundingBox)bb;
			return dbb.getCollisionNormalOf(this);
		}
		return null;
	}
	@Override
	public Vector3d getCollisionBoundaryVector(BoundingBox bb)
	{
		if(bb instanceof AxisAlignedBoundingBox)
		{
			AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox)bb;
			
			double dx = ((minX+maxX)/2-(aabb.minX+aabb.maxX)/2);
			double dy = ((minY+maxY)/2-(aabb.minY+aabb.maxY)/2);
			double dz = ((minZ+maxZ)/2-(aabb.minZ+aabb.maxZ)/2);
			double adx = Math.abs(dx);
			double ady = Math.abs(dy);
			double adz = Math.abs(dz);
			
			if(adx>=ady*aabb.width/aabb.height && adx>=adz*aabb.width/aabb.length)
			{
				vectorCache.setX(dx>0 ? maxX+(aabb.minX+aabb.maxX)/2 : minX-(aabb.minX+aabb.maxX)/2);
				vectorCache.setY((minY+maxY)/2);
				vectorCache.setZ((minZ+maxZ)/2);
				return vectorCache;
			}
			if(ady>=adx*aabb.height/aabb.width && ady>=adz*aabb.height/aabb.length)
			{
				vectorCache.setX((minX+maxX)/2);
				vectorCache.setY(dy>0 ? maxY+(aabb.minY+aabb.maxY)/2 : minY-(aabb.minY+aabb.maxY)/2);
				vectorCache.setZ((minZ+maxZ)/2);
				return vectorCache;
			}
			if(adz>=adx*aabb.length/aabb.width && adz>=ady*aabb.length/aabb.height)
			{
				vectorCache.setX((minX+maxX)/2);
				vectorCache.setY((minY+maxY)/2);
				vectorCache.setZ(dz>0 ? maxZ+(aabb.minZ+aabb.maxZ)/2 : minZ-(aabb.minZ+aabb.maxZ)/2);
				return vectorCache;
			}
		}
		return null;
	}
}
