package com.SE.onvif.util;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

import com.SE.onvif.api.core.DBService;
import com.SE.onvif.service.JdbcService;

public class ServiceFactory {
	private static final int WAIT_TIME = 3*1000;
	
//	Hashtable<String, ServiceTracker> servTrack;
	
	
	public static DBService getDBService() {
		
		return new JdbcService();
	}
	
	public static Object getParserService(String category) {
		Object obj = null;
		String name = "com.sec.vc.core.parser." + category + "." + category.toUpperCase() + "Parser";
		try {	
			Class cls = Class.forName(name);
			obj = cls.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static Object getEncodeService(String category) {
		Object obj = null;
		String name = "com.sec.vc.core.parser." + category + "." + category.toUpperCase() + "Encode";
		try {	
			Class cls = Class.forName(name);
			obj = cls.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static Object getDVXService(String servName) {
		
		Object obj = null;
		String name = "com.sec.vc.core.service." + servName.substring(0, 1).toUpperCase() +
								servName.substring(1) + "Service";
		try {	
			Class cls = Class.forName(name);
			Method m = cls.getMethod("newInstance", null);
			obj = m.invoke(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	//报警服务------start-------
	
	
	public Object getAlarmService(String servName) {
		
		return null;
	}
	//报警服务------end------
	
	public void trackConfigService() {
//		trackService(TrackType.CONFIGURE, "(dvx.conf=*)");
	}
	
	public static Object getXmlConfigService() {
		
		return null;
	}
	//-----------XML配置--end---------
	
	class TrackType {
		static final String PARSER = "parser";
		static final String SERVICE = "service";
		static final String CONFIGURE = "configure";
		//报警服务-------------
		static final String ALARMSERVICE = "alarmService";
		//-----------XML配置
		static final String CONFIGURE_XML = "configure_xml";
	}
}
