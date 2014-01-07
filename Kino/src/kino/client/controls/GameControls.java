package kino.client.controls;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import kino.cache.Entity;
import kino.client.gui.GUIGameView;
import kino.util.NumericalTools;
import kino.util.Vector3d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GameControls {
	static interface AnalogOutput{
		String getDisplayName();
		String getDescription();
		void addToVector(Vector3d out, GUIGameView gameView, double amount);
	}
	public static final AnalogOutput KinoForwardsBackwards = new AnalogOutput() {
		@Override
		public String getDisplayName() { return "Kino Forwards/Backwards"; }
		@Override
		public String getDescription() { return "Moves the kino backwards/forwards along your line of sight"; }
		@Override
		public void addToVector(Vector3d out, GUIGameView gameView, double amount) {
			out.add(
				(Math.cos(Math.toRadians(gameView.controlEntity.vertRot)) * Math.sin(Math.toRadians(gameView.controlEntity.horzRot)))*amount,
				(Math.sin(Math.toRadians(gameView.controlEntity.vertRot)))*amount,
				(Math.cos(Math.toRadians(gameView.controlEntity.vertRot)) * Math.cos(Math.toRadians(gameView.controlEntity.horzRot)))*amount
			);
		}
	};
	public static final AnalogOutput KinoUpDown = new AnalogOutput() {
		@Override
		public String getDisplayName() { return "Kino Up/Down"; }
		@Override
		public String getDescription() { return "Moves the kino up or down relative to where you're looking"; }
		@Override
		public void addToVector(Vector3d out, GUIGameView gameView, double amount) {
			out.add(
				(Math.cos(Math.toRadians(gameView.controlEntity.vertRot+90)) * Math.sin(Math.toRadians(gameView.controlEntity.horzRot)))*amount,
				(Math.sin(Math.toRadians(gameView.controlEntity.vertRot+90)))*amount,
				(Math.cos(Math.toRadians(gameView.controlEntity.vertRot+90)) * Math.cos(Math.toRadians(gameView.controlEntity.horzRot)))*amount
			);
		}
	};
	public static final AnalogOutput KinoLeftRight = new AnalogOutput() {
		@Override
		public String getDisplayName() { return "Kino Left/Right"; }
		@Override
		public String getDescription() { return "Moves the kino left or right"; }
		@Override
		public void addToVector(Vector3d out, GUIGameView gameView, double amount) {
			out.add(
				Math.sin(Math.toRadians(gameView.controlEntity.horzRot+90))*amount,
				0,
				Math.cos(Math.toRadians(gameView.controlEntity.horzRot+90))*amount
			);
		}
	};
	public static final AnalogOutput KinoForwardsBackwardsHorizon = new AnalogOutput() {
		@Override
		public String getDisplayName() { return "Kino Forward/Back horizon"; }
		@Override
		public String getDescription() { return "Moves the kino towards or away from the horizon"; }
		@Override
		public void addToVector(Vector3d out, GUIGameView gameView, double amount) {
			out.add(
				Math.sin(Math.toRadians(gameView.controlEntity.horzRot)),
				0,
				Math.cos(Math.toRadians(gameView.controlEntity.horzRot))
			);
		}
	};
	public static final AnalogOutput WorldNorthSouth = new AnalogOutput() {
		@Override
		public String getDisplayName() { return "World North/South"; }
		@Override
		public String getDescription() { return "Moves the kino towards north or south"; }
		@Override
		public void addToVector(Vector3d out, GUIGameView gameView, double amount) { out.add(1,0,0); }
	};
	public static final AnalogOutput WorldUpDown = new AnalogOutput() {
		@Override
		public String getDisplayName() { return "World Up/Down"; }
		@Override
		public String getDescription() { return "Moves the kino towards the sky or the floor"; }
		@Override
		public void addToVector(Vector3d out, GUIGameView gameView, double amount) { out.add(0,1,0); }
	};
	public static final AnalogOutput WorldEastWest = new AnalogOutput() {
		@Override
		public String getDisplayName() { return "World East/West"; }
		@Override
		public String getDescription() { return "Moves the kino towards east or west"; }
		@Override
		public void addToVector(Vector3d out, GUIGameView gameView, double amount) { out.add(0,1,0); }
	};
	public static final AnalogOutput KinoPitch = new AnalogOutput() {
		@Override
		public String getDisplayName() { return "Kino Pitch"; }
		@Override
		public String getDescription() { return "Tilts the kino up or down"; }
		@Override
		public void addToVector(Vector3d out, GUIGameView gameView, double amount) { gameView.controlEntity.vertRot=NumericalTools.capTo(-90, gameView.controlEntity.vertRot+amount, 90); gameView.doRecalculate(); }
	};
	public static final AnalogOutput KinoYaw = new AnalogOutput() {
		@Override
		public String getDisplayName() { return "Kino Yaw"; }
		@Override
		public String getDescription() { return "Turns the kino around"; }
		@Override
		public void addToVector(Vector3d out, GUIGameView gameView, double amount) { gameView.controlEntity.horzRot=NumericalTools.wrapTo(0, gameView.controlEntity.horzRot+amount, 360); gameView.doRecalculate(); }
	};
	public static final AnalogOutput KinoFOV = new AnalogOutput() {
		@Override
		public String getDisplayName() { return "Kino FOV"; }
		@Override
		public String getDescription() { return "Increases or decreases the kinos viewing angle"; }
		@Override
		public void addToVector(Vector3d out, GUIGameView gameView, double amount) {
			gameView.FOV += amount;
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
	private static ControlBinding[] DEFAULT_CONTROLS = new ControlBinding[]{
		new ControlBinding(KinoForwardsBackwards,null,new int[]{Keyboard.KEY_UP},0.01),
		new ControlBinding(KinoForwardsBackwards,null,new int[]{Keyboard.KEY_DOWN},-0.01),
		new ControlBinding(KinoLeftRight,null,new int[]{Keyboard.KEY_LEFT},1),
		new ControlBinding(KinoLeftRight,null,new int[]{Keyboard.KEY_RIGHT},-1),
		
		new ControlBinding(KinoForwardsBackwards,null,new int[]{Keyboard.KEY_W},0.01),
		new ControlBinding(KinoForwardsBackwards,null,new int[]{Keyboard.KEY_S},-0.01),
		
		new ControlBinding(KinoLeftRight,null,new int[]{Keyboard.KEY_A},0.01),
		new ControlBinding(KinoLeftRight,null,new int[]{Keyboard.KEY_D},-0.01),
		
		new ControlBinding(KinoUpDown,null,new int[]{Keyboard.KEY_E},0.01),
		new ControlBinding(KinoUpDown,null,new int[]{Keyboard.KEY_Q},-0.01),
		
		new ControlBinding(KinoYaw,MouseMotionX,null,-0.1),
		new ControlBinding(KinoPitch,MouseMotionY,null,0.1),
	};
	private static List<ControlBinding> analogControls = Arrays.asList(DEFAULT_CONTROLS);//new LinkedList<ControlBinding>();
	private static Vector3d vectorCache = new Vector3d();
	public static void doControls(GUIGameView gameView)
	{
		for(ControlBinding cb : analogControls)
		{
			if(cb.conditions==null || areKeysDown(cb.conditions))
			{
				double amount = cb.modifier;
				if(cb.input!=null)
					amount *= cb.input.getAmount();
				if(amount!=0)
				{
					cb.output.addToVector(vectorCache, gameView, amount);
				}
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
	private static boolean areKeysDown(int[] keys)
	{
		for(int key : keys)
		{
			if(!Keyboard.isKeyDown(key))
				return false;
		}
		return true;
	}
}
