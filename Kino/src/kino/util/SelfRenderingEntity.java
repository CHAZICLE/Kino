package kino.util;
/**
 * An entity that can handle its own rendering
 * 
 * @author charles
 *
 */
public interface SelfRenderingEntity {
	/**
	 * Render the entity with the current OpenGL settings
	 */
	public void render();
}
