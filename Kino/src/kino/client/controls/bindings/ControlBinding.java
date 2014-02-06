package kino.client.controls.bindings;


public abstract class ControlBinding {
	private long lastTick = -1;
	public void invoke(long tick)
	{
		if(tick==lastTick)
			return;
		lastTick = tick;
		forceInvoke();
	}
	public abstract void forceInvoke();
}
