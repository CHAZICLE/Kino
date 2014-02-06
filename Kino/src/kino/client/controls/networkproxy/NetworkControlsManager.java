package kino.client.controls.networkproxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import kino.client.controls.ControlInputHolder;
import kino.client.controls.DigitalInput;

public class NetworkControlsManager implements ControlInputHolder,Runnable {
	private Thread networkControlServerThread;
	private ServerSocket server;
	public void enable()
	{
		if(networkControlServerThread==null)
		{
			networkControlServerThread = new Thread(this);
			networkControlServerThread.setName("Network Control Server");
			networkControlServerThread.start();
		}
	}
	public void disable()
	{
		if(networkControlServerThread!=null)
		{
			networkControlServerThread.interrupt();
			networkControlServerThread = null;
		}
	}
	@Override
	public void tickEvents() {
		
	}

	@Override
	public DigitalInput readDigitalInput() {
		return null;
	}
	@Override
	public void run() {
		server = new ServerSocket(27546);
		while(!Thread.interrupted())
		{
			Socket client = server.accept();
		}
		try { server.close(); } catch(IOException e) { }
	}
}
