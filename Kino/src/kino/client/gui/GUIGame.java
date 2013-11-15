package kino.client.gui;

import kino.cache.CacheManager;
import kino.cache.EKino;
import kino.cache.Entity;
import kino.cache.World;
import kino.cache.BB.BBRenderDebug;
import kino.client.ScreenGUIManager;
import kino.client.WorldRenderer;
import kino.util.NumericalTools;
import kino.util.RenderDebug;
import kino.util.RenderUtils;
import kino.util.Vector3d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GUIGame extends GUI {
	public static boolean freezeme = false;
	public GUIGame(ScreenGUIManager paramManager) {
		super(paramManager);
		cache = new CacheManager();
		cache.addWorld(renderWorld=new World());
		
		kino=new EKino();
		kino2=new EKino();
		kino2.render = true;
		
		
		smallSphere=new BBRenderDebug.Sphere(1);
		smallSphere.render = true;
		smallSphere.movement = true;
		smallSphere.gravity = true;
		smallSphere.collider = true;
		smallSphere.mass = 1;
		
		smallCube = new BBRenderDebug.Cube(1,1,1);
		smallCube.render = true;
		smallCube.movement = true;
		smallCube.gravity = true;
		smallCube.collider = true;
		smallCube.mass = 3;
		
		
		largeSphere=new BBRenderDebug.Sphere(10);
		largeSphere.render = true;
		largeSphere.collider = true;
		largeSphere.position.setXYZ(10, 0, 0);
		
		largeCube=new BBRenderDebug.Cube(10,10,10);
		largeCube.render = true;
		largeCube.collider = true;
		largeCube.position.setXYZ(-10, 0, 0);
		
		renderWorld.addEntity(kino);
		renderWorld.addEntity(kino2);
		renderWorld.addEntity(smallSphere);
		renderWorld.addEntity(largeSphere);
		renderWorld.addEntity(smallCube);
		renderWorld.addEntity(largeCube);
		
		
		controlEntity = renderEntity = kino;
		
		middleX = Display.getWidth()/2;
		middleY = Display.getHeight()/2;
		
		cache.start();
	}
	public CacheManager cache;
	public World renderWorld;
	public Entity renderEntity;
	public Entity controlEntity;
	
	
	public Entity kino;
	public Entity kino2;
	
	public Entity smallSphere;
	public Entity largeSphere;
	
	public Entity smallCube;
	public Entity largeCube;
	
	private int middleX = 0;
	private int middleY = 0;
	private int storedX = 0;
	private int storedY = 0;
	private boolean recalculate = true;
	private static final double MOUSE_SPEED = 0.1f;
	public float FOV = 70;
	private float throwPower = 0.5f;
	
	private boolean debugAxisGrid = false;
	private boolean debugAxisMarks = false;
	private boolean debugMouseWheelControlsPower = true;
	
	@Override
	public boolean onKeyUp(int key) {
		if(key==Keyboard.KEY_Y)
		{
			freezeme = !freezeme;
			System.out.println("Set collision freeze: "+freezeme);
		}
		else if(key==Keyboard.KEY_F5)
		{
			debugAxisMarks = !debugAxisMarks;
			System.out.println("Set axis marks: "+debugAxisMarks);
		}
		else if(key==Keyboard.KEY_F6)
		{
			debugAxisGrid = !debugAxisGrid;
			System.out.println("Set axis grid: "+debugAxisGrid);
		}
		else if(key==Keyboard.KEY_F7)
		{
			debugMouseWheelControlsPower = !debugMouseWheelControlsPower;
			System.out.println("Mouse wheel controls "+(debugMouseWheelControlsPower ? "Throw power" : "FOV"));
		}
		// Throwing from kino
		else if(key==Keyboard.KEY_R)
		{
			System.out.println("Throwing the small sphere");
			smallSphere.teleport(controlEntity);
			smallSphere.movement = true;
			smallSphere.motion = vectorCache_relativeForward.makeMagnitude(throwPower);
		}
		else if(key==Keyboard.KEY_T)
		{
			System.out.println("Throwing the small cube");
			smallCube.teleport(controlEntity);
			smallCube.movement = true;
			smallCube.motion = vectorCache_relativeForward.makeMagnitude(throwPower);
		}
		// Teleporting large sphere
		else if(key==Keyboard.KEY_O)
		{
			System.out.println("Teleport the large sphere");
			largeSphere.teleport(controlEntity);
		}
		else if(key==Keyboard.KEY_P)
		{
			System.out.println("Teleport the large cube");
			largeCube.teleport(controlEntity);
		}
		if(key==Keyboard.KEY_ESCAPE)
			getManager().stackGUI(new GUIEsc(getManager()));
		return true;
	}
	@Override
	public void onClose() {
		cache.running = false;
	}
	@Override
	public void onFocus() {
		Mouse.setGrabbed(true);
		storedX = Mouse.getX();
		storedY = Mouse.getY();
		Mouse.setCursorPosition(middleX, middleY);
		recalculate = true;
		Display.setResizable(true);
	}
	@Override
	public void onBlur() {
		Mouse.setCursorPosition(storedX, storedY);
		Mouse.setGrabbed(false);
	}
	@Override
	public void draw(double interpolation)
	{
		if(getManager().isSurfaceGUI(this))
			controlLoop(interpolation);
		RenderUtils.setup3DProjection(FOV);
		
		GL11.glLoadIdentity();
		GL11.glRotated(-renderEntity.vertRot, 1.0f,0.0f,0.0f);
		GL11.glRotated(180-renderEntity.horzRot, 0.0f,1.0f,0.0f);
		
		GL11.glTranslated(
			-renderEntity.position.getX(),
			-renderEntity.position.getY(),
			-renderEntity.position.getZ()
		);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		// Debug Rendering
		if(debugAxisGrid)
			RenderDebug.renderAxisMarks();
		//RenderDebug.renderCube();
		RenderDebug.renderFloor();
		if(debugAxisMarks)
			RenderDebug.renderAxisGrid();
		
		
		WorldRenderer.render(renderWorld);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		RenderUtils.setup2DProjection(-1f,1f, -1f,1f);
		GL11.glLoadIdentity();
		GL11.glColor3f(0.0f,0.0f,1.0f);
		GL11.glLineWidth(1.0f);
		
		GL11.glBegin(GL11.GL_LINES);
		
		GL11.glVertex2f(+0.2f,+0.2f);
		GL11.glVertex2f(-0.2f,-0.2f);
		GL11.glVertex2f(-0.2f,+0.2f);
		GL11.glVertex2f(+0.2f,-0.2f);
		
		GL11.glEnd();
		
	}
	Vector3d vectorCache_relativeRight;
	Vector3d vectorCache_absoluteForward;
	Vector3d vectorCache_relativeForward;
	Vector3d vectorCache_absoluteUp = new Vector3d(0,1,0);
	Vector3d vectorCache_relativeUp;
	double multiplier = 0;
	void controlLoop(double interpolation)
	{
		// FOV Changes
		int dw = Mouse.getDWheel();
		if(dw>0)
		{
			if(debugMouseWheelControlsPower)
				throwPower += 0.1;
			else
			{
				FOV--;
				RenderUtils.setup3DProjection(FOV);
			}
		}
		else if(dw<0)
		{
			if(debugMouseWheelControlsPower)
				throwPower -= 0.1;
			else
			{
				FOV++;
				RenderUtils.setup3DProjection(FOV);
			}
		}
			
		boolean horzChange = recalculate;
		boolean vertChange = recalculate;
		boolean rightChanged = recalculate;
		boolean directionChanged = recalculate;
		if(!recalculate) recalculate = false;
		// Rotation changes
		if(Mouse.getX()!=middleX)
		{
			controlEntity.horzRot = NumericalTools.wrapTo(0, controlEntity.horzRot+MOUSE_SPEED*(middleX-Mouse.getX()), 360);
			horzChange = true;
		}
		if(Mouse.getY()!=middleY)
		{
			controlEntity.vertRot = NumericalTools.capTo(-90, controlEntity.vertRot+MOUSE_SPEED*(Mouse.getY()-middleY), 90);
			vertChange = true;
		}
		if(Mouse.getX()!=middleX || Mouse.getY()!=middleY)
			Mouse.setCursorPosition(middleX,middleY);
		
		
		if(horzChange)
		{
			vectorCache_relativeRight = new Vector3d(
				Math.sin(Math.toRadians(controlEntity.horzRot-90)),
				0,
				Math.cos(Math.toRadians(controlEntity.horzRot-90))
			);
			vectorCache_absoluteForward = new Vector3d(
				Math.sin(Math.toRadians(controlEntity.horzRot)),
				0,
				Math.cos(Math.toRadians(controlEntity.horzRot))
			);
			rightChanged = true;
		}
		if(horzChange || vertChange)
		{
			vectorCache_relativeForward = new Vector3d(
				Math.cos(Math.toRadians(controlEntity.vertRot)) * Math.sin(Math.toRadians(controlEntity.horzRot)),
				Math.sin(Math.toRadians(controlEntity.vertRot)),
				Math.cos(Math.toRadians(controlEntity.vertRot)) * Math.cos(Math.toRadians(controlEntity.horzRot))
			);
			directionChanged = true;
			
		}
		if(directionChanged || rightChanged)
			vectorCache_relativeUp = vectorCache_relativeRight.crossMake(vectorCache_relativeForward);
		
		// Key controls
		multiplier = (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 8 : 1)*interpolation;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			controlEntity.position.add(vectorCache_relativeRight.makeMagnitude(-multiplier));
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			controlEntity.position.add(vectorCache_relativeRight.makeMagnitude(multiplier));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_W))
				controlEntity.position.add(vectorCache_absoluteForward.makeMagnitude(multiplier));
			if(Keyboard.isKeyDown(Keyboard.KEY_S))
				controlEntity.position.add(vectorCache_absoluteForward.makeMagnitude(-multiplier));
			if(Keyboard.isKeyDown(Keyboard.KEY_Q))
				controlEntity.position.add(vectorCache_absoluteUp.makeMagnitude(-multiplier));
			if(Keyboard.isKeyDown(Keyboard.KEY_E))
				controlEntity.position.add(vectorCache_absoluteUp.makeMagnitude(multiplier));
		}
		else
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_W))
				controlEntity.position.add(vectorCache_relativeForward.makeMagnitude(multiplier));
			if(Keyboard.isKeyDown(Keyboard.KEY_S))
				controlEntity.position.add(vectorCache_relativeForward.makeMagnitude(-multiplier));
			if(Keyboard.isKeyDown(Keyboard.KEY_Q))
				controlEntity.position.add(vectorCache_relativeUp.makeMagnitude(-multiplier));
			if(Keyboard.isKeyDown(Keyboard.KEY_E))
				controlEntity.position.add(vectorCache_relativeUp.makeMagnitude(multiplier));
		}
	}
}
