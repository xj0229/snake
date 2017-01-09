package com.SE.onvif.service;

import javax.xml.namespace.QName;

import com.SE.onvif.api.core.DBService;
import com.SE.onvif.beans.IpcBeanImpl;
import com.SE.onvif.persistence.IpcBean;
import com.SE.onvif.util.ServiceFactory;
import com.ms.wsdiscovery.WsDiscoveryFinder;
import com.ms.wsdiscovery.exception.WsDiscoveryException;
import com.ms.wsdiscovery.network.exception.WsDiscoveryNetworkException;
import com.ms.wsdiscovery.servicedirectory.WsDiscoveryService;
import com.ms.wsdiscovery.servicedirectory.interfaces.IWsDiscoveryServiceCollection;

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
}
