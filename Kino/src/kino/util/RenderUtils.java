package kino.util;

import kino.client.GLSLProgram;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class RenderUtils {
	
	// Projection Matrix
	private static float proj_FOV = 45.0f;
	private static float proj_ratio = 1f;
	private static final float[] params = new float[4];
	public static void sendProjection_3D()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(proj_FOV,proj_ratio,0.1f,1000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	public static void setProjection_3D(float FOV, float ratio)
	{
		proj_FOV = FOV;
		proj_ratio = ratio;
	}
	public static void sendProjection_2D()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(params[0],params[1],  params[2],params[3], -1.0f,1.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	public static void setProjection_2D(float minX, float maxX, float minY, float maxY)
	{
		params[0] = minX;
		params[1] = maxX;
		params[2] = minY;
		params[3] = maxY;
	}
	
	public static final GLSLProgram colorShader = new GLSLProgram("basicColor");
	
	public static GLSLProgram activeShader = null;
	public static void useColorShader()
	{
		activeShader = colorShader;
		activeShader.sendView(view_RenderEntityCache);
		colorShader.activate();
	}
	public static void preload() {
		colorShader.build();
	}
	public static void unload()
	{
		
	}
	// View Matrix
	private static final Matrix4f view_RenderEntityCache = new Matrix4f();
	private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();
	private static final Vector3f X_AXIS = new Vector3f(1,0,0);
	private static final Vector3f Y_AXIS = new Vector3f(0,1,0);
	private static Vector3f temp = new Vector3f();
	public static void sendViewParameters_Normal()
	{
		activeShader.sendView(IDENTITY_MATRIX);
	}
	public static void sendViewParameters_RenderEntity()
	{
		activeShader.sendView(view_RenderEntityCache);
	}
	public static void setViewParameters_RenderEntity(double x, double y, double z, double vertRot, double horzRot)
	{
		view_RenderEntityCache.setIdentity();
		view_RenderEntityCache.rotate((float) Math.toRadians(-vertRot), X_AXIS);
		view_RenderEntityCache.rotate((float) Math.toRadians(180-horzRot), Y_AXIS);
		temp.setX((float)-x); temp.setY((float)-y); temp.setZ((float)-z);
		view_RenderEntityCache.translate(temp);
		
		
		/*GL11.glRotated(-vertRot, 1.0f,0.0f,0.0f);
		GL11.glRotated(180-renderEntity.horzRot, 0.0f,1.0f,0.0f);
		
		GL11.glTranslated(
			-renderEntity.position.getX(),
			-renderEntity.position.getY(),
			-renderEntity.position.getZ()
		);*/
	}
}
