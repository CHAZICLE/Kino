package kino.client.controls;

public abstract class DigitalOutput {
	private boolean previousState = false;
	public void set(boolean state)
	{
		if(previousState!=state)
		{
			if(state)
				onPress();
			else
				onRelease();
			previousState = state;
		}
	}
	public abstract void onPress();
	public abstract void onRelease();
}
