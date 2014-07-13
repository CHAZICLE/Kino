package kino.client;

import kino.cache.EKino;
import kino.client.controls.io.AnalogOutput;
import kino.client.controls.io.COutputHolder;
import kino.client.controls.io.DigitalOutput;
import kino.client.gui.GUIGameView;
import kino.util.NumericalTools;

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
				case 0:
					return "North";
				case 1:
					return "East";
				case 2:
					return "Above";
				case 3:
					return "Forward";
				case 4:
					return "Right";
				case 5:
					return "Up";
				case 6:
					return "Horizon";
				case 7:
					return "Yaw";
				case 8:
					return "Pitch";
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
				case 3://Forward
					kino.position.add(
						(Math.cos(Math.toRadians(-kino.yaw)) * Math.cos(Math.toRadians(-kino.pitch)))*value,
		  				(Math.sin(Math.toRadians(kino.pitch)))*value,
						(Math.sin(Math.toRadians(-kino.yaw)) * Math.cos(Math.toRadians(-kino.pitch)))*value
					);
					break;
				case 4://Right
					kino.position.add(
						Math.sin(Math.toRadians(kino.yaw))*value,
		  				0,
		  				Math.cos(Math.toRadians(kino.yaw))*value
					);
					break;
				case 5://Up
					kino.position.add(
						(Math.cos(Math.toRadians(-kino.yaw)) * Math.sin(Math.toRadians(-kino.pitch)))*value,
		  				(Math.cos(Math.toRadians(kino.pitch)))*value,
		  				(Math.sin(Math.toRadians(-kino.yaw)) * Math.sin(Math.toRadians(-kino.pitch)))*value
					);
					break;
				case 6://Horizon
					kino.position.add(
						(Math.cos(Math.toRadians(-kino.yaw)))*value,
		  				0,
						(Math.sin(Math.toRadians(-kino.yaw)))*value
					);
					break;
				case 7://Yaw
					kino.yaw = NumericalTools.wrapTo(-180, kino.yaw+value, 180);
					break;
				case 8://Pitch
					kino.pitch = NumericalTools.capTo(-90, kino.pitch+value, 90);
					break;
				}
				gv.doRecalculate();
			}
		};
	}

}
