package com.SE.onvif.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.onvif.ver10.schema.DateTime;

import com.SE.onvif.soap.OnvifDevice;

public class OnvifObservable extends Observable{
	
	private List<String> tem = new ArrayList<>();
	
	private OnvifDevFinderService onvifDevFinderServer;
	
	public OnvifObservable(OnvifDevFinderService onvifDevFinderServer){
		super();
		
		this.onvifDevFinderServer = onvifDevFinderServer;
		
	}

	public List<String> getChanged(){
		return tem;
	}
	
	public void sendStateRequest(String toIPCip, String fromip,String sn){
		
		tem.clear();
		for(OnvifDevice od : this.onvifDevFinderServer.getOnvifDevices()){
			if(od.getIpNoPort().equals(toIPCip)){
				DateTime d = od.getDate();
				String s = new String("DeviceStatus"+","+"Online"+","+"OK"+","+"Encode"+","
			+d.getDate().getYear()+"-"+d.getDate().getMonth()+"-"+d.getDate().getDay()
			+"T"
			+d.getTime().getHour()+":"+d.getTime().getMinute()+":"+d.getTime().getSecond());
				this.tem.add(s);
				this.tem.add("sip");
				this.tem.add(fromip);
				this.tem.add(sn);
			}
			
		}
		if(tem.size() > 0){
			setChanged();
			notifyObservers();
		}
		
	}
	
	public List<String> getContent(){
		return this.tem;
	}

}
