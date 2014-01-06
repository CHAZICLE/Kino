package kino.client.controls;

import java.util.LinkedList;
import java.util.List;

import kino.cache.Entity;
import kino.util.Vector3d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GameControls {
	static interface AnalogOutput{
		String getDisplayName();
		String getDescription();
		void addToVector(Vector3d out, Entity controlEntity, double amount);
	}
	private static final AnalogOutput[] outputs = new AnalogOutput[]{
		new AnalogOutput() {
			@Override
			public String getDisplayName() { return "Kino Forwards/Backwards"; }
			@Override
			public String getDescription() { return "Moves the kino backwards/forwards along your line of sight"; }
			@Override
			public void addToVector(Vector3d out, Entity controlEntity, double amount) {
				out.add(
					(Math.cos(Math.toRadians(controlEntity.vertRot)) * Math.sin(Math.toRadians(controlEntity.horzRot)))*amount,
					(Math.sin(Math.toRadians(controlEntity.vertRot)))*amount,
					(Math.cos(Math.toRadians(controlEntity.vertRot)) * Math.cos(Math.toRadians(controlEntity.horzRot)))*amount
				);
			}
		},
		new AnalogOutput() {
			@Override
			public String getDisplayName() { return "Kino Up/Down"; }
			@Override
			public String getDescription() { return "Moves the kino up or down relative to where you're looking"; }
			@Override
			public void addToVector(Vector3d out, Entity controlEntity, double amount) {
				out.add(
					(Math.cos(Math.toRadians(controlEntity.vertRot+90)) * Math.sin(Math.toRadians(controlEntity.horzRot)))*amount,
					(Math.sin(Math.toRadians(controlEntity.vertRot+90)))*amount,
					(Math.cos(Math.toRadians(controlEntity.vertRot+90)) * Math.cos(Math.toRadians(controlEntity.horzRot)))*amount
				);
			}
		},
		new AnalogOutput() {
			@Override
			public String getDisplayName() { return "Kino Left/Right"; }
			@Override
			public String getDescription() { return "Moves the kino left or right"; }
			@Override
			public void addToVector(Vector3d out, Entity controlEntity, double amount) {
				out.add(
					(Math.cos(Math.toRadians(controlEntity.vertRot+90)) * Math.sin(Math.toRadians(controlEntity.horzRot)))*amount,
					(Math.sin(Math.toRadians(controlEntity.vertRot+90)))*amount,
					(Math.cos(Math.toRadians(controlEntity.vertRot+90)) * Math.cos(Math.toRadians(controlEntity.horzRot)))*amount
				);
			}
		},
		new AnalogOutput() {
			@Override
			public String getDisplayName() { return "Kino Forward/Back horizon"; }
			@Override
			public String getDescription() { return "Moves the kino towards or away from the horizon"; }
			@Override
			public void addToVector(Vector3d out, Entity controlEntity, double amount) {
				out.add(
					Math.sin(Math.toRadians(controlEntity.horzRot)),
					0,
					Math.cos(Math.toRadians(controlEntity.horzRot))
				);
			}
		},
		new AnalogOutput() {
			@Override
			public String getDisplayName() { return "World North/South"; }
			@Override
			public String getDescription() { return "Moves the kino towards north or south"; }
			@Override
			public void addToVector(Vector3d out, Entity controlEntity, double amount) { out.add(1,0,0); }
		},
		new AnalogOutput() {
			@Override
			public String getDisplayName() { return "World Up/Down"; }
			@Override
			public String getDescription() { return "Moves the kino towards the sky or the floor"; }
			@Override
			public void addToVector(Vector3d out, Entity controlEntity, double amount) { out.add(0,1,0); }
		},
		new AnalogOutput() {
			@Override
			public String getDisplayName() { return "World East/West"; }
			@Override
			public String getDescription() { return "Moves the kino towards east or west"; }
			@Override
			public void addToVector(Vector3d out, Entity controlEntity, double amount) { out.add(0,1,0); }
		},
		new AnalogOutput() {
			@Override
			public String getDisplayName() { return "Kino Pitch"; }
			@Override
			public String getDescription() { return "Tilts the kino up or down"; }
			@Override
			public void addToVector(Vector3d out, Entity controlEntity, double amount) { controlEntity.vertRot+=amount; }
		},
		new AnalogOutput() {
			@Override
			public String getDisplayName() { return "Kino Yaw"; }
			@Override
			public String getDescription() { return "Turns the kino around"; }
			@Override
			public void addToVector(Vector3d out, Entity controlEntity, double amount) { controlEntity.horzRot+=amount; }
		},
		new AnalogOutput() {
			@Override
			public String getDisplayName() { return "Kino FOV"; }
			@Override
			public String getDescription() { return "Increases or decreases the kinos viewing angle"; }
			@Override
			public void addToVector(Vector3d out, Entity controlEntity, double amount) {  }
		}
	};
	// Analog Inputs
	static interface AnalogInput{
		String getDisplayName();
		String getDescription();
		double getAmount();
	}
	private static final AnalogInput[] inputs = new AnalogInput[]{
		new AnalogInput() {
			@Override
			public String getDisplayName() { return "Mouse X Motion"; };
			@Override
			public String getDescription() { return "When the mouse moves left/right"; };
			public double getAmount() {
				double val = Mouse.getX()-Display.getWidth()/2;
				Mouse.setCursorPosition(Display.getWidth()/2, Mouse.getY());
				return val;
			};
		},
		new AnalogInput() {
			@Override
			public String getDisplayName() { return "Mouse Y Motion"; };
			@Override
			public String getDescription() { return "When the mouse moves up/down"; };
			public double getAmount() {
				double val = Mouse.getY()-Display.getHeight()/2;
				Mouse.setCursorPosition(Mouse.getX(),Display.getHeight()/2);
				return val;
			};
		}
	};
	// Control binding
	static class ControlBinding {
		GameControls.AnalogOutput output;
		GameControls.AnalogInput input;
		int condition;
		double modifier;
	}
	private static List<ControlBinding> analogControls = new LinkedList<ControlBinding>();
	private static Vector3d vectorCache = new Vector3d();
	public void doControls(Entity controlEntity)
	{
		for(ControlBinding cb : analogControls)
		{
			if(cb.condition==0 || Keyboard.isKeyDown(cb.condition))
			{
				double amount = cb.input.getAmount();
				if(amount!=0)
				{
					cb.output.addToVector(vectorCache, controlEntity, amount);
				}
			}
		}
		if(vectorCache.getMagnitudeSquared()!=0)
		{
			//TODO: Maybe normalise?
			controlEntity.position.add(vectorCache);
			vectorCache.nullify();
		}
	}
}
