package kino.client.controls.bindings;


public abstract class ControlBinding {
	public ControlBinding next;
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
