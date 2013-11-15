package kino.applet;

import kino.client.ScreenGUIManager;


public class Main {
	private static boolean appletRunning = true;
	public static boolean isRunning() { return appletRunning; }
	public static void main(String[] args)
	{
		new ScreenGUIManager().run();
	}
	/*
	 * T0D0 LIST 0F THINGS T0 D0
	 * - Some kind of texturing system
	 * - Populate world with terrain
	 * - World terrain renderer
	 * - Improved wavefront converter/render
	 * - Collision detection between Radial and Axis aligned bounding boxes
	 * - 
	 */
}
