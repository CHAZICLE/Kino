package kino.cache;

import java.util.HashMap;
import java.util.Map;


public class CacheProfiler {
	static int lastCount = 0;
	static Map<String,Long> data = new HashMap<String,Long>(5);
	static String lastSection = null;
	static long lastTime = 0;
	public static void section(String section)
	{
		if(lastSection!=null)
		{
			lastCount++;
			data.put(lastSection, 0-lastTime+(lastTime=CacheManager.getMiliseconds()));
		}
		else
			lastTime=CacheManager.getMiliseconds();
		lastSection = section;
	}
	public static Map<String,Long> reset(HashMap<String,Long> newDataBuffer)
	{
		lastSection = null;
		if(newDataBuffer==null)
		{
			Map<String,Long> data2 = data;
			data = new HashMap<String,Long>(lastCount);
			lastCount = 0;
			return data2;
		}
		else
		{
			Map<String,Long> data2 = data;
			data=newDataBuffer;
			lastCount = 0;
			return data2;
		}
	}
}
