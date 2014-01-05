package kino.client.gui;


public interface ScreenGUIHolder {
	
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract int getOffsetX();
	public abstract int getOffsetY();
	
	public void closeGUI(GUI gui);
	public void openGUI(GUI gui);
	public void openRootGUI(GUI gui);
	public boolean isSurfaceGUI(GUI gui);
}
