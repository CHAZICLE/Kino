package kino.client.controls;


public class MenuControlOutputsHolder implements ControlOutputHolder {
	@Override
	public String getName() {
		return "Menu Controls";
	}
	class ActionDigitalOutput extends DigitalOutput {
		Action act;
		@Override
		public void onPress() {
			
		}
		@Override
		public void onRelease() {
			
		}
	}
	public static enum State { PRESS,RELEASE,PRESS_TARGET,RELEASE_TARGET,MOVE; }
	public static enum Action {
		SELECT,//Left click/Enter
		MENU,//Right click/context menu
		BACK,//Escape/backspace
		
		NEXT,//Down,Right,Space
		PREVIOUS,//Up,Left
		;
	}
	private ActionDigitalOutput[] controls = new ActionDigitalOutput[Action.values().length];
	@Override
	public int digitalSize() {
		return 5;
	}

	@Override
	public int analogSize() {
		return 0;
	}

	@Override
	public String getDigitalName(int i) {
		Action[] a = Action.values();
		try
		{
			return Character.toUpperCase(a[i].name().charAt(0))+a[i].name().substring(1).toLowerCase();
		}
		catch(IndexOutOfBoundsException ex)
		{
			return null;
		}
	}

	@Override
	public String getAnalogName(int i) {
		return null;
	}
	
	@Override
	public DigitalOutput getDigital(int i) {
		Action[] a = Action.values();
		try
		{
			ActionDigitalOutput ado = controls[i];
			if(ado==null)
			{
				controls[i] = ado = new ActionDigitalOutput();
				ado.act = a[i];
			}
			return ado;
		}
		catch(IndexOutOfBoundsException ex)
		{
			return null;
		}
	}

	@Override
	public AnalogOutput getAnalog(int i) {
		return null;
	}
	
}
