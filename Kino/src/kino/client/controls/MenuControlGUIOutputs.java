package kino.client.controls;

import kino.client.controls.io.AnalogOutput;
import kino.client.controls.io.COutputHolder;
import kino.client.controls.io.DigitalOutput;
import kino.client.gui.ScreenGUIHolder;

public class MenuControlGUIOutputs implements COutputHolder {
	
	private ScreenGUIHolder holder;
	private String name;
	public static enum Action {
		SELECT, BACK, UP, DOWN, LEFT, RIGHT, MENU;
	}
	

	public MenuControlGUIOutputs(ScreenGUIHolder holder, String name) {
		this.holder = holder;
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
		return new DigitalActionSender(Action.values()[i]);
	}

	@Override
	public AnalogOutput getAnalogOutput(int i) {
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
					holder.setTargetX((int)value);
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
					holder.setTargetY((int)value);
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
					holder.setTargetX(holder.getTargetX()+(int)value);
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
					holder.setTargetY(holder.getTargetY()+(int)value);
				}
			};
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	class DigitalActionSender extends DigitalOutput {

		private Action action;
		
		public DigitalActionSender(Action action) {
			this.action = action;
		}

		@Override
		public String getName() {
			return "GUI Output:"+action.name();
		}

		@Override
		public void onStateChange(boolean state) {
			holder.onAction(action, state);
		}
	}
}
