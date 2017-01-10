package com.SE.onvif.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.xml.namespace.QName;

import com.SE.onvif.api.core.DBService;
import com.SE.onvif.beans.IpcBeanImpl;
import com.SE.onvif.persistence.IpcBean;
import com.SE.onvif.util.ServiceFactory;
import com.SE.onvif.util.StaticHelper;
import com.ms.wsdiscovery.WsDiscoveryFinder;
import com.ms.wsdiscovery.exception.WsDiscoveryException;
import com.ms.wsdiscovery.network.exception.WsDiscoveryNetworkException;
import com.ms.wsdiscovery.servicedirectory.WsDiscoveryService;
import com.ms.wsdiscovery.servicedirectory.interfaces.IWsDiscoveryServiceCollection;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class OnvifDevFinderService {
	WsDiscoveryFinder finder;
	DBService dbService;

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
	
	public static void main(String[] args) throws IOException{
		File probe = new File(StaticHelper.probeFile);
		BufferedReader reader = new BufferedReader(new FileReader(probe));
		StringBuilder sb = new StringBuilder();
		String tem = null;
		while((tem = reader.readLine()) != null){
			sb.append(tem);
		}
		
		String probeProtocol = sb.toString();
		
		InetAddress address = InetAddress.getByName("239.255.255.250");
		MulticastSocket ms = new MulticastSocket(3702);
		ms.setTimeToLive(32);
		byte[] data =probeProtocol.getBytes();
		ms.joinGroup(address);
		ms.setLoopbackMode(false);
		DatagramPacket dp = new DatagramPacket(data,data.length,address,3702);
		ms.send(dp);
		ms.close();
		
		//System.out.println(sb.toString());
	}
}
