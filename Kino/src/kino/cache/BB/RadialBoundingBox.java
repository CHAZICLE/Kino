package kino.cache.BB;

import kino.cache.Entity;
import kino.util.Vector3d;


public class RadialBoundingBox extends BoundingBox {
	
	public RadialBoundingBox(Entity ent, double rad) {
		super(ent);
		radius = rad;
	}
	@Override
	public void update(double x, double y, double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public double x = 0;
	public double y = 0;
	public double z = 0;
	public double radius = 0;
	@Override
	public boolean doesCollideWith(BoundingBox bb)
	{
		if(bb instanceof RadialBoundingBox)
		{
			RadialBoundingBox rbb = (RadialBoundingBox)bb;
			double dx = Math.abs(x-rbb.x);
			double dy = Math.abs(y-rbb.y);
			double dz = Math.abs(z-rbb.z);
			return
				dx*dx+dy*dy+dz*dz
				<=
				(radius+rbb.radius)*(radius+rbb.radius);
		}
		else if(bb instanceof AxisAlignedBoundingBox)
		{
			AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox)bb;
			/*double dx = (aabb.minX+aabb.maxX)/2-x;
			double dy = (aabb.minY+aabb.maxY)/2-y;
			double dz = (aabb.minZ+aabb.maxZ)/2-z;
			
			// dxyz: Sphere -> Cube
			double mag = Math.sqrt(dx*dx+dy*dy+dz*dz);
			dx = x+(dx*radius/mag);
			dy = y+(dy*radius/mag);
			dz = z+(dz*radius/mag);
			
			return
				dx>=aabb.minX && dx<=aabb.maxX &&
				dy>=aabb.minY && dy<=aabb.maxY &&
				dz>=aabb.minZ && dz<=aabb.maxZ;
				
			// METHOD 2:
			
			double radius = this.radius+0.5;
			float dist_squared = (float) (radius*radius);
		    if(x < aabb.minX)
		    	dist_squared -= (x - aabb.minX)*(x - aabb.minX);
		    else if(x > aabb.maxX)
		    	dist_squared -= (x - aabb.maxX)*(x - aabb.maxX);
		    if(y < aabb.minY)
		    	dist_squared -= (y - aabb.minY)*(y - aabb.minY);
		    else if(y > aabb.maxY)
		    	dist_squared -= (y - aabb.maxY)*(y - aabb.maxY);
		    if (z < aabb.minZ)
		    	dist_squared -= (z - aabb.minZ)*(z - aabb.minZ);
		    else if (z > aabb.maxZ)
		    	dist_squared -= (z - aabb.maxZ)*(z - aabb.maxZ);
		    return dist_squared > 0;*/
		    
		    double dmin = 0;

		    if (x < aabb.minX) {
		        dmin += Math.pow(x - aabb.minX, 2);
		    } else if (x > aabb.maxX) {
		        dmin += Math.pow(x - aabb.maxX, 2);
		    }

		    if (y < aabb.minY) {
		        dmin += Math.pow(y - aabb.minY, 2);
		    } else if (y > aabb.maxY) {
		        dmin += Math.pow(y - aabb.maxY, 2);
		    }

		    if (z < aabb.minZ) {
		        dmin += Math.pow(z - aabb.minZ, 2);
		    } else if (z > aabb.maxZ) {
		        dmin += Math.pow(z - aabb.maxZ, 2);
		    }

		    return dmin <= Math.pow(radius, 2);
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
		if(bb instanceof RadialBoundingBox)
		{
			RadialBoundingBox rbb = (RadialBoundingBox)bb;
			vectorCache.setX(x-rbb.x);
			vectorCache.setY(y-rbb.y);
			vectorCache.setZ(z-rbb.z);
			return vectorCache;
		}
		else if(bb instanceof AxisAlignedBoundingBox)
		{
			AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox)bb;
			
			double dx = ((aabb.minX+aabb.maxX)/2-x);
			double dy = ((aabb.minY+aabb.maxY)/2-y);
			double dz = ((aabb.minZ+aabb.maxZ)/2-z);
			double adx = Math.abs(dx);
			double ady = Math.abs(dy);
			double adz = Math.abs(dz);
			
			if(adx>=ady*aabb.width/aabb.height && adx>=adz*aabb.width/aabb.length)
			{
				vectorCache.setX(dx<0 ? 1 : -1);
				vectorCache.setY(0);
				vectorCache.setZ(0);
				return vectorCache;
			}
			if(ady>=adx*aabb.height/aabb.width && ady>=adz*aabb.height/aabb.length)
			{
				vectorCache.setX(0);
				vectorCache.setY(dy<0 ? 1 : -1);
				vectorCache.setZ(0);
				return vectorCache;
			}
			if(adz>=adx*aabb.length/aabb.width && adz>=ady*aabb.length/aabb.height)
			{
				vectorCache.setX(0);
				vectorCache.setY(0);
				vectorCache.setZ(dz<0 ? 1 : -1);
				return vectorCache;
			}
		}
		return null;
	}
	@Override
	public Vector3d getCollisionBoundaryVector(BoundingBox bb) {
		if(bb instanceof RadialBoundingBox)
		{
			RadialBoundingBox rbb = (RadialBoundingBox)bb;
			return vectorCache.setXYZ(
				x-rbb.x,
				y-rbb.y,
				z-rbb.z
			).setMagnitude(radius+rbb.radius).add(rbb.x,rbb.y,rbb.z);
		}
		return null;
	}
	
}
