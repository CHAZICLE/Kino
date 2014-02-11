package kino.client.controls;

import org.lwjgl.input.Controller;


public class JoystickControlInputHolder implements ControlInputHolder {
	public JoystickControlInputHolder(Controller paramController) {
		controller = paramController;
	}
	class JoystickDigitalInput extends DigitalInput {
		private Controller controller;
		private int index;
		public JoystickDigitalInput(Controller paramController, int paramIndex) {
			controller = paramController;
			index = paramIndex;
		}
		@Override
		public boolean get() {
			return controller.isButtonPressed(index);
		}
		@Override
		public String getName() {
			return controller.getButtonName(index);
		}
	}
	class JoystickAnalogInput extends AnalogInput {
		private Controller controller;
		private int index;
		public JoystickAnalogInput(Controller paramController, int paramIndex) {
			controller = paramController;
			index = paramIndex;
		}
		@Override
		public double get() {
			return controller.getAxisValue(index);
		}
		@Override
		public String getName() {
			return controller.getAxisName(index);
		}
	}
	private Controller controller;
	@Override
	public String getName() {
		return "Joystick: "+controller.getName();
	}

	@Override
	public int digitalSize() {
		return controller.getButtonCount();
	}

	@Override
	public int analogSize() {
		return controller.getAxisCount();
	}

	@Override
	public String getDigitalName(int i) {
		return controller.getButtonName(i);
	}

	@Override
	public String getAnalogName(int i) {
		return controller.getAxisName(i);
	}

	@Override
	public DigitalOutput getDigital(int i) {
		return null;
	}

	@Override
	public AnalogOutput getAnalog(int i) {
		return null;
	}
	
	@Override
	public void tickEvents(long tick) {
		
	}

	@Override
	public DigitalInput readNewDigitalInput() {
		return null;
	}
}
