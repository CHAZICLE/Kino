package kino.client;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class DisplayHolder {
	public static void onCreate()
	{
		try {
			//Display.setDisplayMode(new DisplayMode(800, 600));
			Mouse.setGrabbed(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Display.setTitle("Kino");
		Display.setInitialBackground(1.0f, 1.0f, 1.0f);
	}
	public static void onDestroy()
	{
		Display.destroy();
	}
}
