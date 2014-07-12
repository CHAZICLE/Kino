package kino.client;

import kino.cache.EKino;
import kino.client.controls.io.AnalogOutput;
import kino.client.controls.io.COutputHolder;
import kino.client.controls.io.DigitalOutput;
import kino.client.gui.GUIGameView;

public class KinoControls implements COutputHolder {
	private EKino kino;
	private int playerID;
	private GUIGameView gv;
	public KinoControls(EKino pKino, GUIGameView gameView, int playerID) {
		kino = pKino;
		this.playerID = playerID;
		gv = gameView;
	}
	
	@Override
	public String getName() {
		return "Kino "+playerID;
	}

	@Override
	public int digitalOutputSize() {
		return 0;
	}

	@Override
	public int analogOutputSize() {
		return 2;
	}

	@Override
	public DigitalOutput getDigitalOutput(int i) {
		return null;
	}

	@Override
	public AnalogOutput getAnalogOutput(final int i) {
		final KinoControls holder = this;
		return new AnalogOutput() {
			
			@Override
			public String getName() {
				switch(i)
				{
				case 0://North
					return "North Motion";
				case 1://East
					return "East Motion";
				case 2://Sky
					return "Sky Motion";
				default:
					return null;
				}
			}
			
			@Override
			public int getID() {
				return i;
			}
			
			@Override
			public COutputHolder getOutputHolder() {
				return holder;
			}
			
			@Override
			public void post(double value) {
				switch(i)
				{
				case 0://North
					kino.position.add(value,0,0);
					break;
				case 1://East
					kino.position.add(0,0,value);
					break;
				case 2://Sky
					kino.position.add(0,value,0);
					break;
				}
				gv.doRecalculate();
			}
		};
	}

}
