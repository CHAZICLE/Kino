package kino.client.controls;

import kino.client.controls.io.AnalogInput;
import kino.client.controls.io.CInputHolder;
import kino.client.controls.io.CInputScanner;
import kino.client.controls.io.DigitalInput;
import kino.client.controls.io.Input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class LWJGLInputScanner implements CInputScanner {

	private MouseInputHolder holder;
	private KeyboardInputHolder kholder;
	@Override
	public CInputHolder scan() {
		if(holder==null)
			return holder=new MouseInputHolder();
		if(kholder==null)
			return kholder = new KeyboardInputHolder();
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
					case 2:
						return "Mouse Delta X";
					case 3:
						return "Mouse Delta Y";
					case 4:
						return "Mouse Delta Wheel";
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
					case 2:
						return Mouse.getEventDX();
					case 3:
						return Mouse.getEventDY();
					case 4:
						return Mouse.getEventDWheel();
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
					case 2:
						return Mouse.getDX();
					case 3:
						return Mouse.getDY();
					case 4:
						return Mouse.getDWheel();
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
	public static class KeyboardInputHolder implements CInputHolder {

		@Override
		public String getName() {
			return "Keyboard";
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
			return Keyboard.getKeyCount();
		}

		@Override
		public int analogInputSize() {
			return 0;
		}

		@Override
		public DigitalInput getDigitalInput(int i) {
			return new KeyboardButtonInput(this,i);
		}

		@Override
		public AnalogInput getAnalogInput(int i) {
			return null;
		}

		@Override
		public boolean isStillActive() {
			return true;
		}
		
	}
	static class KeyboardButtonInput extends DigitalInput {
		
		private KeyboardInputHolder self;
		private int i;
		public KeyboardButtonInput(KeyboardInputHolder self, int index) {
			this.self = self;
			i = index;
		}

		@Override
		public boolean getCurrentState() {
			return Keyboard.isKeyDown(i);
		}

		@Override
		public boolean getEventState() {
			return Keyboard.getEventKeyState();
		}

		@Override
		public CInputHolder getInputHolder() {
			return self;
		}

		@Override
		public String getName() {
			return Keyboard.getKeyName(i);
		}

		@Override
		public int getID() {
			return i;
		}
		
	}

}
