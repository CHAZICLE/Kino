package kino.cache;

import java.util.HashMap;
import java.util.Map;


public class CacheProfiler {
	static int lastCount = 0;
	static HashMap<String,Long> data = new HashMap<String,Long>(5);
	static HashMap<String,Long> pendingBuffer = new HashMap<String,Long>(5);
	static String lastSection = null;
	static long lastTime = 0;
	/**
	 * Starts recording the time for this section until the next one or stopSection() is called
	 * @param section The name of the section to start
	 */
	public static void setSection(String section)
	{
		if(lastSection!=null)
		{
			lastCount++;
			data.put(lastSection, 0-lastTime+(lastTime=CacheManager.getMiliseconds()));
		}
		else
			lastTime = CacheManager.getMiliseconds();
		lastSection = section;
	}
	public static void stopSection()
	{
		data.put(lastSection, CacheManager.getMiliseconds()-lastTime);
		lastSection = null;
	}
	public static Map<String,Long> swap()
	{
		if(pendingBuffer==null)
		{
			pendingBuffer = data;
			data = new HashMap<String,Long>(lastCount);
		}
		else
		{
			HashMap<String,Long> data2 = data;
			data = pendingBuffer;
			pendingBuffer = data2;
		}
		return pendingBuffer;
	}
	public static Map<String,Long> swap(HashMap<String,Long> newDataBuffer)
	{
		lastSection = null;
		if(newDataBuffer==null)
		{
			newDataBuffer = data;
			data = new HashMap<String,Long>(lastCount);
			lastCount = 0;
			return newDataBuffer;
		}
		else
		{
			HashMap<String,Long> data2 = data;
			data=newDataBuffer;
			lastCount = 0;
			return data2;
		}
	}
}
