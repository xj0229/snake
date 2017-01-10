package com.SE.onvif.service;

import java.util.List;

public class OnvifObservable {
	
	private static List<String> tem;
	
	public static void setChanged(List<String> val){
		tem = val;
	}
	public static List<String> getChanged(){
		return tem;
	}

}
