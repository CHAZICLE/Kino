package kino.client.bindings;

import kino.client.gui.ScreenGUIHolder;

public class ControlGUIOutputs implements ControlOutputHolder {

	private ScreenGUIHolder holder;
	private String name;

	public ControlGUIOutputs(ScreenGUIHolder holder, String name) {
		this.holder = holder;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public static enum Action {
		SELECT, BACK, UP, DOWN, LEFT, RIGHT, MENU;
	}

	@Override
	public int digitalOutputSize() {
		// SELECT,BACK,UP,DOWN,LEFT,RIGHT,MENU
		return Action.values().length;
	}

	@Override
	public int analogOutputSize() {
		// TARGET_X,TARGET_Y,TARGET_DX,TARGET_DY
		return 4;
	}

	@Override
	public DigitalOutput getDigitalOutput(int i) {
		return null;
	}

	@Override
	public AnalogOutput getAnalogOutput(int i) {
		return null;
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
