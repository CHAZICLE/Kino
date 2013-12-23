package kino.client.gui;

public class Element {
	public Element(GUI paramHolder)
	{
		gui = paramHolder;
	}
	int x,y,width,height;
	GUI gui;
	public boolean onMouseDown(int button, int x, int y)
	{
		return true;
	}
	public boolean onMouseUp(int button, int x, int y)
	{
		return true;
	}
	public void draw()
	{
		
	}
}
