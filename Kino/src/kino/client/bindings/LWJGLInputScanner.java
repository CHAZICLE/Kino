package kino.client.bindings;

import org.lwjgl.input.Mouse;

public class LWJGLInputScanner implements ControlInputScanner {

	private MouseInputHolder holder;
	@Override
	public ControlInputHolder scan() {
		if(holder==null)
			return holder=new MouseInputHolder();
		return null;
	}
	public static class MouseInputHolder implements ControlInputHolder {

		@Override
		public String getName() {
			return "Mouse";
		}

		@Override
		public DigitalInput fetchDigitalInput() {
			return null;
		}

		@Override
		public AnalogInput fetchAnalogInput() {
			return null;
		}

		@Override
		public Input pullEvent() {
			return null;
		}

		@Override
		public int digitalInputSize() {
			return Mouse.getButtonCount();
		}

		@Override
		public int analogInputSize() {
			return 2;
		}

		@Override
		public DigitalInput getDigitalInput(final int i) {
			return new DigitalInput() {
				
				@Override
				public String getName() {
					return Mouse.getButtonName(i);
				}
				
				@Override
				public boolean getEventState() {
					return Mouse.getEventButtonState();
				}
				
				@Override
				public boolean getCurrentState() {
					return Mouse.isButtonDown(i);
				}
			};
		}

		@Override
		public AnalogInput getAnalogInput(final int i) {
			return new AnalogInput() {
				
				@Override
				public String getName() {
					switch(i)
					{
					case 0:
						return "Mouse X";
					case 1:
						return "Mouse Y";
					}
					return null;
				}
				
				@Override
				public double getEventValue() {
					switch(i)
					{
					case 0:
						return Mouse.getEventX();
					case 1:
						return Mouse.getEventY();
					}
					return 0;
				}
				
				@Override
				public double getCurrentValue() {
					switch(i)
					{
					case 0:
						return Mouse.getX();
					case 1:
						return Mouse.getY();
					}
					return 0;
				}
			};
		}

		@Override
		public boolean isStillActive() {
			return true;
		}
		
	}

}
