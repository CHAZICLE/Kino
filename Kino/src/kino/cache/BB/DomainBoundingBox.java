package kino.cache.BB;

import kino.cache.Entity;
import kino.util.Vector3d;

public class DomainBoundingBox extends BoundingBox {

	double minX,minY,minZ;
	double maxX,maxY,maxZ;
	public DomainBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		super(null);
		this.minX=minX;
		this.minY=minY;
		this.minZ=minZ;
		
		this.maxX=maxX;
		this.maxY=maxY;
		this.maxZ=maxZ;
	}
	@Override
	public void update(double x, double y, double z) {
		
	}
	@Override
	public boolean doesCollideWith(BoundingBox bb) {
		if(bb instanceof AxisAlignedBoundingBox)
		{
			AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox)bb;
			return aabb.minX<=minX || aabb.maxX>=maxX ||
				aabb.minY<=minY || aabb.maxY>=maxY ||
				aabb.minZ<=minZ || aabb.maxZ>=maxZ
			;
		}
		else if(bb instanceof RadialBoundingBox)
		{
			RadialBoundingBox rbb = (RadialBoundingBox)bb;
			return rbb.x-rbb.radius<=minX || rbb.x+rbb.radius>=maxX ||
				rbb.y-rbb.radius<=minY || rbb.y+rbb.radius>=maxY ||
				rbb.z-rbb.radius<=minZ || rbb.z+rbb.radius>=maxZ
			;
		}
		return false;
	}
	@Override
	public Vector3d getCollisionNormalOf(BoundingBox bb) {
		vectorCache.setXYZ(0, 0, 0);
		if(bb instanceof AxisAlignedBoundingBox)
		{
			AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox)bb;
			if(aabb.minX<=minX)
			{
				vectorCache.setX(1);
			}
			else if(aabb.maxX>=maxX)
			{
				vectorCache.setX(-1);
			}
			if(aabb.minY<=minY)
			{
				vectorCache.setY(1);
			}
			else if(aabb.maxY>=maxY)
			{
				vectorCache.setY(-1);
			}
			if(aabb.minZ<=minZ)
			{
				vectorCache.setZ(1);
			}
			else if(aabb.maxZ>=maxZ)
			{
				vectorCache.setZ(-1);
			}
			if(vectorCache.getMagnitudeSquared()>0)
				return vectorCache;
		}
		else if(bb instanceof RadialBoundingBox)
		{
			RadialBoundingBox rbb = (RadialBoundingBox)bb;
			if(rbb.x-rbb.radius<=minX)
			{
				vectorCache.setX(1);
			}
			else if(rbb.x+rbb.radius>=maxX)
			{
				vectorCache.setX(-1);
			}
			if(rbb.y-rbb.radius<=minY)
			{
				vectorCache.setY(1);
			}
			else if(rbb.y+rbb.radius>=maxY)
			{
				vectorCache.setY(-1);
			}
			if(rbb.z-rbb.radius<=minZ)
			{
				vectorCache.setZ(1);
			}
			else if(rbb.z+rbb.radius>=maxZ)
			{
				vectorCache.setZ(-1);
			}
			if(vectorCache.getMagnitudeSquared()!=0)
				return vectorCache;
		}
		return null;
	}
	@Override
	public Vector3d getCollisionBoundaryVector(BoundingBox bb)
	{
		if(bb instanceof AxisAlignedBoundingBox)
		{
			AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox)bb;
			vectorCache.set(aabb.trackingEntity.position);
			
			if(aabb.minX<=minX)
				vectorCache.setX(aabb.minX+aabb.hwidth);
			else if(aabb.maxX>=maxX)
				vectorCache.setX(aabb.maxX-aabb.hwidth);
			
			if(aabb.minY<=minY)
				vectorCache.setY(aabb.minY+aabb.hheight);
			else if(aabb.maxY>=maxY)
				vectorCache.setY(aabb.maxY-aabb.hheight);
			
			if(aabb.minZ<=minZ)
				vectorCache.setZ(aabb.minZ+aabb.hlength);
			else if(aabb.maxZ>=maxZ)
				vectorCache.setZ(aabb.maxZ-aabb.hlength);
			
			return vectorCache;
		}
		return null;
	}
	
}
