package com.SE.onvif.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;

import com.SE.onvif.api.core.DBService;
import com.SE.onvif.beans.IpcBeanImpl;
import com.SE.onvif.persistence.IpcBean;
import com.SE.onvif.soap.OnvifDevice;
import com.SE.onvif.util.ServiceFactory;
import com.SE.onvif.util.StaticHelper;
import com.SE.onvif.util.UDPReceiveData;
import com.ms.wsdiscovery.WsDiscoveryFinder;
import com.ms.wsdiscovery.exception.WsDiscoveryException;
import com.ms.wsdiscovery.network.exception.WsDiscoveryNetworkException;
import com.ms.wsdiscovery.servicedirectory.WsDiscoveryService;
import com.ms.wsdiscovery.servicedirectory.interfaces.IWsDiscoveryServiceCollection;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class OnvifDevFinderService {
	WsDiscoveryFinder finder;
	DBService dbService;
	
	private ArrayList<OnvifDevice> OnvifDevices = new ArrayList<>();
	
	public ArrayList<OnvifDevice> getOnvifDevices(){
		return OnvifDevices;
	}
	

	public OnvifDevFinderService() throws WsDiscoveryNetworkException  {

		dbService = ServiceFactory.getDBService();
	}

	public void findDevice() throws Exception {
		finder = new WsDiscoveryFinder();
		/**
		 * Search for a specific service
		 *
		 */

		// Describe the port type of the service we are looking for. Namespace is optional.
		QName myPortType = new QName("http://www.onvif.org/ver10/network/wsdl", "NetworkVideoTransmitter");

		// Search for with 5 second timeout...
		System.out.println("Searching for service with port type \"" + myPortType.toString() + "\"");
		IWsDiscoveryServiceCollection result = finder.find(myPortType, 5000);

		// Display the results.
		System.out.println("** Discovered services: **");

		for (WsDiscoveryService service : result) {
			// Print service info
			System.out.println(service.toString());
			java.util.List<String> xaddrList = service.getXAddrs();
			if(xaddrList != null && xaddrList.size() > 0) {
				String xaddr = xaddrList.get(0);
				int sIndex = xaddr.indexOf("http://");
				String devIP =xaddr.substring(sIndex+7, xaddr.indexOf(":", sIndex+7));
				String onvifServIPAndPort = xaddr.substring(sIndex+7, xaddr.indexOf("/", sIndex+7));
				IpcBean ipc = dbService.getIpcByIP(devIP);
				if(ipc == null) { //find new IPC device, add it!
					ipc = new IpcBeanImpl();
					ipc.setOnvifServIPAndPort(onvifServIPAndPort);
					ipc.setXAddr(xaddr);
					dbService.insertIpc(ipc);
				} else { //update the IPC device info
					boolean needUpdate = false;
					if(ipc.getOnvifServIPAndPort() == null || ! ipc.getOnvifServIPAndPort().equals(onvifServIPAndPort)) {
						ipc.setOnvifServIPAndPort(onvifServIPAndPort);
						needUpdate = true;
					}
					if(ipc.getXAddr() == null || ! ipc.getXAddr().equals(xaddr)) {
						ipc.setXAddr(xaddr);
						needUpdate = true;
					}
					if(needUpdate) {
						dbService.updateIpcByID(ipc);
					}
				}
			}
			System.out.println("-----------------------------------------");
		}
		// Stop finder 
		finder.done();
	}

	public void findDevice2() throws Exception {
		finder = new WsDiscoveryFinder();
		/** 
		 * Search for any service
		 */	
		System.out.println("Searching for all services (2 sec).");
		IWsDiscoveryServiceCollection result = finder.findAll(2*1000);

		// Display the results.
		System.out.println("** Discovered services: **");

		for (WsDiscoveryService service : result) {
			// Print service info
			System.out.println(service.toString());
			System.out.println("---");
		}

		// Stop finder 
		finder.done();
	}
	
	public void findDevice3(){
		
		ArrayList<String> data = new ArrayList<>();
		
		Runnable receivetask = new UDPReceiveData(data);
		
		Thread t = new Thread(receivetask);
		
		t.start();
		
		File probe = new File(StaticHelper.probeFile);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(probe));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		String tem = null;
		try {
			while((tem = reader.readLine()) != null){
				sb.append(tem);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String probeProtocol = sb.toString();
		
		InetAddress address = null;
		try {
			address = InetAddress.getByName("239.255.255.250");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] senddata =probeProtocol.getBytes();

		DatagramPacket dp = new DatagramPacket(senddata,senddata.length,address,3702);
		
		MulticastSocket ms;
		try {
			ms = new MulticastSocket(3702);
			ms.setTimeToLive(32);
			ms.joinGroup(address);
			ms.setLoopbackMode(false);
			ms.send(dp);
			ms.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			Thread.sleep(2*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(data.size() > 1){
			for(int i = 1; i < data.size(); i++){
				try {
					Document document = DocumentHelper.parseText(probeProtocol);//data.get(i));
					Element teme = document.getRootElement();//.elementByID("<tns:Scopes>");
					createIPC(teme);
					//getNode(teme);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		

		//data.clear();
			//System.out.println("jjj"+receivedata.get(10, TimeUnit.SECONDS));

		
		//OnvifDevice oo = new OnvifDevice("192.168.0.13:80","admin","admin");
		//System.out.println(oo.getDate().getDate().getDay());
		

	}
	
	public void createIPC(Element node){  
		//System.out.println("--------------------");
		
		if(node.getName().equals("XAddrs")){
			Pattern pat = Pattern.compile("[0-9]+.[0-9]+.[0-9]+.[0-9]+:[0-9]+");
			Matcher mat = pat.matcher(node.getTextTrim());
			if(mat.find()){
				OnvifDevice od = null;
				try {
					od = new OnvifDevice(mat.group(),"admin","admin");
				} catch (ConnectException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SOAPException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.OnvifDevices.add(od);
				//System.out.println(mat.group());
			}

/*			String[] tt= node.getTextTrim().split("");
			for(String t : tt){
				System.out.println(t);
			}*/
			//System.out.println(node.getText());
		}
		
		//当前节点的名称、文本内容和属性  
		//System.out.println("当前节点名称："+node.getName());//当前节点名称  
		//System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称  
		//List<Attribute> listAttr = node.attributes();//当前节点的所有属性的list  
		/*	    for(Attribute attr : listAttr){//遍历当前节点的所有属性  
			        String name=attr.getName();//属性名称  
			        String value=attr.getValue();//属性的值  
			        System.out.println("属性名称："+name+"属性值："+value);  
			    }*/
		  
		//递归遍历当前节点所有的子节点  
		List<Element> listElement=node.elements();//所有一级子节点的list  
		for(Element e:listElement){//遍历所有一级子节点  
			createIPC(e);//递归  
		}  
	}
	
	public static void getNode(Element node){  
		System.out.println("--------------------");
		//当前节点的名称、文本内容和属性  
		System.out.println("当前节点名称："+node.getName());//当前节点名称  
		System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称  
		List<Attribute> listAttr = node.attributes();//当前节点的所有属性的list  
			    for(Attribute attr : listAttr){//遍历当前节点的所有属性  
			        String name=attr.getName();//属性名称  
			        String value=attr.getValue();//属性的值  
			        System.out.println("属性名称："+name+"属性值："+value);  
			    }
		  
		//递归遍历当前节点所有的子节点  
		List<Element> listElement=node.elements();//所有一级子节点的list  
		for(Element e:listElement){//遍历所有一级子节点  
			getNode(e);//递归  
		}  
	}
	
}
