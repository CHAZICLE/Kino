package kino.applet;

import java.io.File;

import kino.client.ScreenGUIManager;


public class Main {
	public static void main(String[] args)
	{
		byte requestflags = 0;
		File logFile = null;
		for(int i=0;i<args.length;i++)
		{
			String arg = args[i];
			if("--server".equalsIgnoreCase(arg) || hasFlag(arg, 's'))
				requestflags |= 1;
			else if("--gui".equalsIgnoreCase(arg) || hasFlag(arg, 'g'))
				requestflags |= 2;
			else if("--nogui".equalsIgnoreCase(arg) || hasFlag(arg, 'h'))
				requestflags |= 4;
			else if("--daemon".equalsIgnoreCase(arg) || "--silent".equalsIgnoreCase(arg) || hasFlag(arg, 'D') || hasFlag(arg, 'q'))
				requestflags |= 8;
			else if("--logfile".equalsIgnoreCase(arg) || hasFlag(arg, 'l'))
			{
				if(args.length==i+1)
					throw new IllegalArgumentException("logfile/l should have a parameter");
				logFile = new File(args[i+1]);
				File folder = logFile.getParentFile();
				if(!folder.exists())
				{
					if(!folder.mkdirs())
						throw new IllegalArgumentException("Couldn't make the folder to put the log file in");
				}
				else if(!folder.isDirectory())
					throw new IllegalArgumentException("The log file wasn't put in a directory");
				if(!logFile.canWrite())
					throw new IllegalArgumentException("Can't write to the log file");
			}
		}
		new ScreenGUIManager().run();
		
	}
	private static boolean hasFlag(String arg, char flag)
	{
		if(!arg.startsWith("-") || arg.startsWith("--"))
			return false;
		return arg.indexOf(flag)!=-1;
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
