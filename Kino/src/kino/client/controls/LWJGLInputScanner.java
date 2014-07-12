package kino.client.controls;

import kino.client.controls.io.AnalogInput;
import kino.client.controls.io.CInputHolder;
import kino.client.controls.io.CInputScanner;
import kino.client.controls.io.DigitalInput;
import kino.client.controls.io.Input;

import org.lwjgl.input.Mouse;

public class LWJGLInputScanner implements CInputScanner {

	private MouseInputHolder holder;
	@Override
	public CInputHolder scan() {
		if(holder==null)
			return holder=new MouseInputHolder();
		return null;
	}
	public static class MouseInputHolder implements CInputHolder {

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
			final MouseInputHolder holder = this;
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

				@Override
				public int getID() {
					return i;
				}

				@Override
				public CInputHolder getInputHolder() {
					return holder;
				}
			};
		}

		@Override
		public AnalogInput getAnalogInput(final int i) {
			final MouseInputHolder holder = this;
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
				
				@Override
				public int getID() {
					return i;
				}

				@Override
				public CInputHolder getInputHolder() {
					return holder;
				}
			};
		}

		@Override
		public boolean isStillActive() {
			return true;
		}
		
	}

}
