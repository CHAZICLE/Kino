package kino.cache.BB;

import kino.cache.Entity;
import kino.util.Vector3d;


public abstract class BoundingBox {
	Vector3d vectorCache = new Vector3d();
	Entity trackingEntity;
	public BoundingBox(Entity ent) {
		trackingEntity = ent;
	}
	public abstract void update(double x, double y, double z);
	public void update(Vector3d vec){update(vec.getX(), vec.getY(), vec.getZ());}
	/**
	 * Checks if the specified bounding box collides
	 * 
	 * @param bb The other bounding box to check this with
	 * @return True if there is a collision
	 */
	public abstract boolean doesCollideWith(BoundingBox bb);
	/**
	 * Returns true if <br />
	 * <br />
	 * <b>NOT Commutative:</b>
	 * <i>THIS</i> crashes into <i>bb</i>
	 * 
	 * @param bb The bounding box to check
	 * @return The normal vector
	 */
	public abstract Vector3d getCollisionNormalOf(BoundingBox bb);
	/**
	 * Gets the position of the entity such that it is on the edge of the clip
	 * 
	 * @param bb 
	 * @return The new entity position
	 */
	public Vector3d getCollisionBoundaryVector(BoundingBox bb) { return null; }
}
