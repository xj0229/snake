package com.SE.onvif.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class UDPReceiveData implements Runnable{
	
	private MulticastSocket ds;
	
	private String host = "239.255.255.250";
	
	private InetAddress receiveAddress;
	
	private byte[] buff;
	
	private DatagramPacket dp;
	
	private ArrayList<String> data;
	
	public UDPReceiveData(ArrayList<String> data){
		try {
			ds = new MulticastSocket(3702);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			receiveAddress = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ds.joinGroup(receiveAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ds.setLoopbackMode(false);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buff = new byte[10240];
		dp = new DatagramPacket(buff,10240);
		this.data = data;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				ds.receive(dp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data.add(new String(buff,0,dp.getLength()));
		}
	}

}
