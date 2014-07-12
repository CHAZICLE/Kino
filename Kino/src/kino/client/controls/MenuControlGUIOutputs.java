package kino.client.controls;

import kino.client.controls.io.AnalogOutput;
import kino.client.controls.io.COutputHolder;
import kino.client.controls.io.DigitalOutput;
import kino.client.gui.ScreenGUIHolder;

public class MenuControlGUIOutputs implements COutputHolder {
	
	private ScreenGUIHolder guiHolder;
	private String name;
	public static enum Action {
		SELECT, BACK, UP, DOWN, LEFT, RIGHT, MENU;
	}
	

	public MenuControlGUIOutputs(ScreenGUIHolder holder, String name) {
		this.guiHolder = holder;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int digitalOutputSize() {
		return Action.values().length;
	}

	@Override
	public int analogOutputSize() {
		// TARGET_X,TARGET_Y,TARGET_DX,TARGET_DY
		return 4;
	}

	@Override
	public DigitalOutput getDigitalOutput(int i) {
		return new DigitalActionSender(this,Action.values()[i]);
	}

	@Override
	public AnalogOutput getAnalogOutput(int i) {
		final MenuControlGUIOutputs holder = this;
		switch(i)
		{
		case 0://TARGET X
			return new AnalogOutput() {
				
				@Override
				public String getName() {
					return "Menu Mouse X";
				}
				
				@Override
				public void post(double value) {
					guiHolder.setTargetX((int)value);
				}

				@Override
				public COutputHolder getOutputHolder() {
					return holder;
				}

				@Override
				public int getID() {
					return 0;
				}
			};
		case 1://TARGET Y
			return new AnalogOutput() {
				
				@Override
				public String getName() {
					return "Menu Mouse Y";
				}
				
				@Override
				public void post(double value) {
					guiHolder.setTargetY((int)value);
				}

				@Override
				public COutputHolder getOutputHolder() {
					return holder;
				}

				@Override
				public int getID() {
					return 1;
				}
			};
		case 2://TARGET dX
			return new AnalogOutput() {
				
				@Override
				public String getName() {
					return "Menu Mouse movement X";
				}
				
				@Override
				public void post(double value) {
					guiHolder.setTargetX(guiHolder.getTargetX()+(int)value);
				}

				@Override
				public COutputHolder getOutputHolder() {
					return holder;
				}

				@Override
				public int getID() {
					return 2;
				}
			};
		case 3://TARGET dY
			return new AnalogOutput() {
				
				@Override
				public String getName() {
					return "Menu Mouse movement Y";
				}
				
				@Override
				public void post(double value) {
					guiHolder.setTargetY(guiHolder.getTargetY()+(int)value);
				}

				@Override
				public COutputHolder getOutputHolder() {
					return holder;
				}

				@Override
				public int getID() {
					return 3;
				}
			};
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	class DigitalActionSender extends DigitalOutput {

		private Action action;
		private MenuControlGUIOutputs holder;
		
		public DigitalActionSender(MenuControlGUIOutputs holder, Action action) {
			this.action = action;
			this.holder = holder;
		}

		@Override
		public String getName() {
			return "GUI Output:"+action.name();
		}

		@Override
		public void onStateChange(boolean state) {
			guiHolder.onAction(action, state);
		}

		@Override
		public COutputHolder getOutputHolder() {
			return holder;
		}

		@Override
		public int getID() {
			return 0;
		}
	}
}
