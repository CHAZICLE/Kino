package kino.cache;

import org.lwjgl.Sys;

public class CacheManager extends Thread {
	public static final int ticksPerSecond = 40;
	public static final int miliSecondPerTick = 1000/ticksPerSecond;
	
	
	private World[] worlds = new World[0];
	public boolean running = true;
	public void addWorld(World newWorld)
	{
		World[] temp = new World[worlds.length+1];
		for(int i=0;i<worlds.length;i++)
			temp[i] = worlds[i];
		temp[temp.length-1] = newWorld;
		worlds = temp;
	}
	public void run()
	{
		while(running)
		{
			manageTickRate();
			for(World world : worlds)
			{
				if(world!=null)
					world.tick();
			}
		}
	}
	// Tick rate management
	long lastTime = -1;
	public void manageTickRate()
	{
		long milisDuringLastTick = -lastTime+(lastTime=getMiliseconds());
		if(milisDuringLastTick==miliSecondPerTick)
		{
			
		}
		else if(milisDuringLastTick>miliSecondPerTick)
		{
			
		}
		else
		{
			delay((int)(miliSecondPerTick-milisDuringLastTick));
		}
	}
	
	public static long getMiliseconds()
	{
		return (Sys.getTime()*1000)/Sys.getTimerResolution();
	}
	private static void delay(int delay)
	{
		try
		{
			Thread.currentThread();
			Thread.sleep(delay);
		}
		catch(Exception e)
		{
			
		}
	}
}
