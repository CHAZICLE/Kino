package kino.client.controls;

import java.util.Arrays;
import java.util.List;

import kino.cache.Entity;
import kino.client.gui.GUIGameView;
import kino.util.NumericalTools;
import kino.util.Vector3d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GameControls extends ControlBindingManager {
	public static Vector3d controlVectorMotionCache;
	public static Entity controlEntity;
	public static GUIGameView gameView;
	public static final AnalogOutput KinoForwardsBackwards = new AnalogOutput() {
		@Override
		public void post(double value) {
			controlVectorMotionCache.add(
				(Math.cos(Math.toRadians(controlEntity.vertRot)) * Math.sin(Math.toRadians(controlEntity.horzRot)))*value,
				(Math.sin(Math.toRadians(controlEntity.vertRot)))*value,
				(Math.cos(Math.toRadians(controlEntity.vertRot)) * Math.cos(Math.toRadians(controlEntity.horzRot)))*value
			);
		}
	};
	public static final AnalogOutput KinoUpDown = new AnalogOutput() {
		@Override
		public void post(double value) {
			controlVectorMotionCache.add(
				(Math.cos(Math.toRadians(controlEntity.vertRot+90)) * Math.sin(Math.toRadians(controlEntity.horzRot)))*value,
				(Math.sin(Math.toRadians(controlEntity.vertRot+90)))*value,
				(Math.cos(Math.toRadians(controlEntity.vertRot+90)) * Math.cos(Math.toRadians(controlEntity.horzRot)))*value
			);
		}
	};
	public static final AnalogOutput KinoLeftRight = new AnalogOutput() {
		@Override
		public void post(double value) {
			controlVectorMotionCache.add(
				Math.sin(Math.toRadians(controlEntity.horzRot+90))*value,
				0,
				Math.cos(Math.toRadians(controlEntity.horzRot+90))*value
			);
		}
	};
	public static final AnalogOutput KinoForwardsBackwardsHorizon = new AnalogOutput() {
		@Override
		public void post(double value) {
			controlVectorMotionCache.add(
				Math.sin(Math.toRadians(controlEntity.horzRot)),
				0,
				Math.cos(Math.toRadians(controlEntity.horzRot))
			);
		}
	};
	public static final AnalogOutput WorldNorthSouth = new AnalogOutput() {
		@Override
		public void post(double value) { controlVectorMotionCache.add(1,0,0); }
	};
	public static final AnalogOutput WorldUpDown = new AnalogOutput() {
		@Override
		public void post(double value) { controlVectorMotionCache.add(0,1,0); }
	};
	public static final AnalogOutput WorldEastWest = new AnalogOutput() {
		@Override
		public void post(double value) { controlVectorMotionCache.add(0,1,0); }
	};
	public static final AnalogOutput KinoPitch = new AnalogOutput() {
		@Override
		public void post(double value) {
			controlEntity.vertRot=NumericalTools.capTo(-90, controlEntity.vertRot+value, 90);
			gameView.doRecalculate();
		}
	};
	public static final AnalogOutput KinoYaw = new AnalogOutput() {
		@Override
		public void post(double value) {
			controlEntity.horzRot=NumericalTools.wrapTo(0, controlEntity.horzRot+value, 360);
			gameView.doRecalculate();
		}
	};
	public static final AnalogOutput KinoFOV = new AnalogOutput() {
		@Override
		public void post(double value) {
			gameView.FOV += value;
			gameView.doRecalculate();
		}
	};
	// Analog Inputs
	static interface AnalogInput{
		String getDisplayName();
		String getDescription();
		double getAmount();
	}
	private static final AnalogInput MouseMotionX = new AnalogInput()
	{
		@Override
		public String getDisplayName() { return "Mouse X Motion"; };
		@Override
		public String getDescription() { return "When the mouse moves left/right"; };
		public double getAmount() {
			double val = Mouse.getX()-Display.getWidth()/2;
			Mouse.setCursorPosition(Display.getWidth()/2, Mouse.getY());
			return val;
		};
	};
	private static final AnalogInput MouseMotionY = new AnalogInput()
	{
		@Override
		public String getDisplayName() { return "Mouse Y Motion"; };
		@Override
		public String getDescription() { return "When the mouse moves up/down"; };
		public double getAmount() {
			double val = Mouse.getY()-Display.getHeight()/2;
			Mouse.setCursorPosition(Mouse.getX(),Display.getHeight()/2);
			return val;
		};
	};
	private static final AnalogInput MouseScroll = new AnalogInput()
	{
		@Override
		public String getDisplayName() { return "Mouse Scroll Wheel"; };
		@Override
		public String getDescription() { return "It should be fairly self-explanatory"; };
		public double getAmount() {
			return Mouse.getDWheel();
		};
	};
	// Control binding
	static class ControlBinding {
		public ControlBinding(GameControls.AnalogOutput paramOutput, GameControls.AnalogInput paramInput, int[] paramConditions, double paramModifier) {
			output = paramOutput;
			input = paramInput;
			conditions = paramConditions;
			modifier = paramModifier;
		}
		GameControls.AnalogOutput output;
		GameControls.AnalogInput input;
		int conditions[];
		double modifier = 1;
	}
	private static ControlBinding[] DEFAULT_CONTROLS = new ControlBinding[]
	{
		//// WASD
		// Basic Speed
		new ControlBinding(KinoForwardsBackwards,null,new int[]{Keyboard.KEY_W,-Keyboard.KEY_LSHIFT},0.01),
		new ControlBinding(KinoForwardsBackwards,null,new int[]{Keyboard.KEY_S,-Keyboard.KEY_LSHIFT},-0.01),
		new ControlBinding(KinoLeftRight,null,new int[]{Keyboard.KEY_A,-Keyboard.KEY_LSHIFT},0.01),
		new ControlBinding(KinoLeftRight,null,new int[]{Keyboard.KEY_D,-Keyboard.KEY_LSHIFT},-0.01),
		new ControlBinding(KinoUpDown,null,new int[]{Keyboard.KEY_E,-Keyboard.KEY_LSHIFT},0.01),
		new ControlBinding(KinoUpDown,null,new int[]{Keyboard.KEY_Q,-Keyboard.KEY_LSHIFT},-0.01),
		// Extra Speed
		new ControlBinding(KinoForwardsBackwards,null,new int[]{Keyboard.KEY_W,Keyboard.KEY_LSHIFT},0.2),
		new ControlBinding(KinoForwardsBackwards,null,new int[]{Keyboard.KEY_S,Keyboard.KEY_LSHIFT},-0.2),
		new ControlBinding(KinoLeftRight,null,new int[]{Keyboard.KEY_A,Keyboard.KEY_LSHIFT},0.2),
		new ControlBinding(KinoLeftRight,null,new int[]{Keyboard.KEY_D,Keyboard.KEY_LSHIFT},-0.2),
		new ControlBinding(KinoUpDown,null,new int[]{Keyboard.KEY_E,Keyboard.KEY_LSHIFT},0.2),
		new ControlBinding(KinoUpDown,null,new int[]{Keyboard.KEY_Q,Keyboard.KEY_LSHIFT},-0.2),
		//// Arrow Keys
		new ControlBinding(KinoForwardsBackwards,null,new int[]{Keyboard.KEY_UP},0.01),
		new ControlBinding(KinoForwardsBackwards,null,new int[]{Keyboard.KEY_DOWN},-0.01),
		new ControlBinding(KinoLeftRight,null,new int[]{Keyboard.KEY_LEFT},1),
		new ControlBinding(KinoLeftRight,null,new int[]{Keyboard.KEY_RIGHT},-1),
		
		//// Mouse
		// Rotation
		new ControlBinding(KinoYaw,MouseMotionX,new int[]{-Keyboard.KEY_SPACE},-0.1),
		new ControlBinding(KinoPitch,MouseMotionY,new int[]{-Keyboard.KEY_SPACE},0.1),
		// Position
		new ControlBinding(KinoLeftRight,MouseMotionX,new int[]{Keyboard.KEY_SPACE},-0.01),
		new ControlBinding(KinoForwardsBackwards,MouseMotionY,new int[]{Keyboard.KEY_SPACE},0.01),
		// FOV Wheel
		new ControlBinding(KinoFOV, MouseScroll, new int[]{-Keyboard.KEY_LSHIFT}, 0.01),
	};
	private static List<ControlBinding> analogControls = Arrays.asList(DEFAULT_CONTROLS);//new LinkedList<ControlBinding>();
	private static Vector3d vectorCache = new Vector3d();
	public static void doControls(GUIGameView gameView)
	{
		for(ControlBinding cb : analogControls)
		{
			if(cb.conditions==null || validateKeyConditions(cb.conditions))
			{
				double amount = cb.modifier;
				if(cb.input!=null)
					amount *= cb.input.getAmount();
				if(amount!=0)
					cb.output.addToVector(vectorCache, gameView, amount);
			}
		}
		if(vectorCache.getMagnitudeSquared()!=0)
		{
			//TODO: Maybe normalise?
			gameView.controlEntity.position = gameView.controlEntity.position.add(vectorCache);
			//gameView.controlEntity.position.setXYZ(0, 10,0);
			vectorCache.nullify();
			gameView.doRecalculate();
		}
	}
	private static boolean validateKeyConditions(int[] keys)
	{
		for(int key : keys)
		{
			try
			{
				if(key<0 && Keyboard.isKeyDown(-key))
					return false;
				else if(key>0 && !Keyboard.isKeyDown(key))
					return false;
			}
			catch(IndexOutOfBoundsException e)
			{
				System.out.println(key);
				return false;
			}
		}
		return true;
	}
}
