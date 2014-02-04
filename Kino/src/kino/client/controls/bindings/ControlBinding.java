package kino.client.controls.bindings;

import kino.client.controls.ControlBindingManager;

public abstract class ControlBinding {
	private long lastTick = -1;
	public void invoke(ControlBindingManager manager)
	{
		if(manager.tick==lastTick)
			return;
		lastTick = manager.tick;
		forceInvoke();
	}
	public abstract void forceInvoke();
}
