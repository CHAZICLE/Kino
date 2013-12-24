package kino.client.gui;

import org.lwjgl.input.Keyboard;

import kino.util.RenderUtils;


public class GUIMainMenu extends GUI {
	Element gameView = null;
	public GUIMainMenu(ScreenGUIHolder paramHolder) {
		super(paramHolder);
		gameView = new EGameView(this);
		gameView.x = 10;
		gameView.y = 10;
		gameView.width = 100;
		gameView.height = 100;
		elements.add(gameView);
	}
	boolean expandGame = false;
	int sizePercentage = 0;
	@Override
	public boolean onKeyUp(int key) {
		if(key==Keyboard.KEY_ESCAPE)
		{
			expandGame = !expandGame;
			return true;
		}
		return false;
	}
	@Override
	public boolean onMouseDown(int button, int x, int y) {
		if(hasClickedOn(gameView, x, y))
		{
			if(!expandGame)
				expandGame = true;
			else
				gameView.onMouseDown(button, x, y);
		}
		return true;
	}
	@Override
	public void draw(double interpolation) {
		RenderUtils.setProjection_2D(0,getHolder().getWidth(), 0,getHolder().getHeight());
		if(sizePercentage<100 && expandGame)
		{
			gameView.x = (int)((float)(100-sizePercentage)/100*10);
			gameView.y = (int)((float)(100-sizePercentage)/100*10);
			gameView.width = 100+(int)((float)sizePercentage/100*(getHolder().getWidth()-100));
			gameView.height = 100+(int)((float)sizePercentage/100*(getHolder().getHeight()-100));
			sizePercentage++;
			if(sizePercentage==100)
			{
				gameView.x = 0;
				gameView.y = 0;
				gameView.width = getHolder().getWidth();
				gameView.height = getHolder().getHeight();
				gameView.focus();
			}
			else
				gameView.onResize();
		}
		else if(sizePercentage>0 && !expandGame)
		{
			gameView.x = (int)((float)(100-sizePercentage)/100*10);
			gameView.y = (int)((float)(100-sizePercentage)/100*10);
			gameView.width = 100+(int)((float)sizePercentage/100*(getHolder().getWidth()-100));
			gameView.height = 100+(int)((float)sizePercentage/100*(getHolder().getHeight()-100));
			if(sizePercentage==100)
				gameView.blur();
			else
				gameView.onResize();
			sizePercentage--;
		}
		for(Element e : elements)
		{
			e.draw(interpolation);
		}
	}
	@Override
	public void onResize() {
		super.onResize();
	}
}
