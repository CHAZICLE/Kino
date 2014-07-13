package kino.client.gui;

import kino.cache.CacheManager;
import kino.cache.EKino;
import kino.cache.Entity;
import kino.cache.World;
import kino.cache.BB.BBRenderDebug;
import kino.client.KinoControls;
import kino.client.WorldRenderer;
import kino.client.controls.ControlsManager;
import kino.util.RenderDebug;
import kino.util.RenderUtils;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GUIGameView extends GUI {
	public static boolean freezeme = false;
	public GUIGameView(ScreenGUIHolder holder) {
		super(holder);
		cache = new CacheManager();
		cache.addWorld(renderWorld=new World());
		
		kino=new EKino();
		ControlsManager.registerOutputHolder(new KinoControls((EKino)kino, this, 1));
		
		ControlsManager.debugInit();
		
		kino2=new EKino();
		kino2.render = true;
		
		double r = 1;
		
		smallSphere=new BBRenderDebug.Sphere(r*2);
		smallSphere.render = true;
		smallSphere.movement = true;
		smallSphere.gravity = true;
		smallSphere.collider = true;
		smallSphere.mass = 4/3*Math.PI*r*r*r;
		
		r = 2;
		smallCube = new BBRenderDebug.Sphere(r*2);
		smallCube.render = true;
		smallCube.movement = true;
		smallCube.gravity = true;
		smallCube.collider = true;
		smallCube.mass = 4/3*Math.PI*r*r*r;
		
		
		largeSphere=new BBRenderDebug.Sphere(10);
		largeSphere.render = true;
		largeSphere.collider = true;
		largeSphere.position.setXYZ(100, 0, 0);
		
		largeCube=new BBRenderDebug.Cube(10,10,10);
		largeCube.render = true;
		largeCube.collider = true;
		largeCube.position.setXYZ(-100, 0, 0);
		
		renderWorld.addEntity(kino);
		renderWorld.addEntity(kino2);
		renderWorld.addEntity(smallSphere);
		renderWorld.addEntity(largeSphere);
		renderWorld.addEntity(smallCube);
		renderWorld.addEntity(largeCube);
		
		controlEntity = renderEntity = kino;
		
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
	
	private boolean recalculate = true;
	public float FOV = 70;
	
	private boolean debugAxisGrid = false;
	private boolean debugAxisMarks = false;
	
	@Override
	public void onOpen() {
		super.onOpen();
		
	}
	@Override
	public void onClose() {
		cache.running = false;
	}
	@Override
	public void onExpose()
	{
		Mouse.setGrabbed(true);
		recalculate = true;
	}
	@Override
	public void onCover() {
		Mouse.setGrabbed(false);
	}
	@Override
	public void onResize() {
		RenderUtils.setProjection_3D(FOV, (float)getHolder().getWidth()/getHolder().getHeight());
	}
	
	/*public boolean onKeyUp(int key) {
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
		else if(key==Keyboard.KEY_F8)
		{
			kino.world.enableGravity = !kino.world.enableGravity;
			System.out.println("World gravity "+(kino.world.enableGravity ? "enabled" : "disabled"));
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
		{
			getHolder().openGUI(new GUIEsc(getHolder()));
		}
		return true;
	}*/
	@Override
	public void draw(double interpolation)
	{
		/* 3D */
		//if(isExposed())
			//GameControls.doControls(this);
		RenderUtils.setProjection_3D(FOV, (float)getHolder().getWidth()/getHolder().getHeight());
		RenderUtils.sendProjection_3D();
		if(controlEntity!=renderEntity || recalculate)
		{
			RenderUtils.setViewParameters_RenderEntity(
				renderEntity.position.getX(),
				renderEntity.position.getY(),
				renderEntity.position.getZ(),
				renderEntity.pitch,
				renderEntity.yaw
			);
		}
		RenderUtils.sendViewParameters_RenderEntity();
		
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		// Debug Rendering
//		if(debugAxisGrid)
			RenderDebug.renderAxisMarks();
		//RenderDebug.renderCube();
		RenderDebug.renderFloor();
		if(debugAxisMarks)
			RenderDebug.renderAxisGrid();
		// End Debug Rendering
		
		WorldRenderer.renderWorld(renderWorld);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		
		/* 2D */
		RenderUtils.setProjection_2D(0,getHolder().getWidth(),0,getHolder().getHeight());
		RenderUtils.sendViewParameters_Normal();
		GL11.glLoadIdentity();
		
		WorldRenderer.renderHUD(renderWorld);
		RenderDebug.renderCrossHair();
		
		if(recalculate) recalculate = false;
		
	}
	public void doRecalculate() {
		recalculate = true;
	}
}
