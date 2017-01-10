package com.SE.onvif.soap;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;

import org.onvif.ver10.device.wsdl.Device;
import org.onvif.ver10.device.wsdl.DeviceService;
import org.onvif.ver10.events.wsdl.EventPortType;
import org.onvif.ver10.events.wsdl.EventService;
import org.onvif.ver10.media.wsdl.Media;
import org.onvif.ver10.media.wsdl.MediaService;
import org.onvif.ver10.receiver.wsdl.ReceiverPort;
import org.onvif.ver10.receiver.wsdl.ReceiverService;
import org.onvif.ver10.schema.Capabilities;
import org.onvif.ver10.schema.CapabilityCategory;
import org.onvif.ver10.schema.DateTime;
import org.onvif.ver20.imaging.wsdl.ImagingPort;
import org.onvif.ver20.imaging.wsdl.ImagingService;
import org.onvif.ver20.ptz.wsdl.PTZ;
import org.onvif.ver20.ptz.wsdl.PtzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.SE.onvif.beans.DeviceInfo;
import com.SE.onvif.util.WSDLLocations;

//notice this class
public class OnvifDevice {
	private static final Logger logger = LoggerFactory.getLogger(OnvifDevice.class);

	private String deviceIp, originalIp;
	private boolean isProxy;
	private String username, password;
	private String deviceUri;

	private Device device;
	private Media media;
	private PTZ ptz;
	private ImagingPort imaging;
	private EventPortType events;
	private ReceiverPort receiver;

	/**
	 * Initializes an Onvif device, e.g. a Network Video Transmitter (NVT) with
	 * logindata.
	 * 
	 * @param deviceIp
	 *            The IP address of your device, you can also add a port but not
	 *            protocol (e.g. http://)
	 * @param user
	 *            Username you need to login
	 * @param password
	 *            User's password to login
	 * @throws ConnectException
	 *             Exception gets thrown, if device isn't accessible or invalid
	 *             and doesn't answer to SOAP messages
	 * @throws SOAPException
	 */
	public OnvifDevice(String deviceIp, String user, String password) throws ConnectException, SOAPException {
		this.deviceIp = deviceIp;
		URL wsdlLocation = this.getClass().getResource(WSDLLocations.DEVICE);
		this.device = new DeviceService(wsdlLocation).getDevicePort();

		if (!isOnline()) {
			throw new ConnectException("Host not available.");
		}

		this.deviceUri = "http://" + deviceIp + "/onvif/device_service";
		this.username = user;
		this.password = password;

		init();
	}

	/**
	 * Initializes an Onvif device, e.g. a Network Video Transmitter (NVT) with
	 * logindata.
	 * 
	 * @param hostIp
	 *            The IP address of your device, you can also add a port but
	 *            noch protocol (e.g. http://)
	 * @throws ConnectException
	 *             Exception gets thrown, if device isn't accessible or invalid
	 *             and doesn't answer to SOAP messages
	 * @throws SOAPException
	 */
	public OnvifDevice(String hostIp) throws ConnectException, SOAPException {
		this(hostIp, null, null);
	}

	/**
	 * Internal function to check, if device is available and answers to ping
	 * requests.
	 */
	private boolean isOnline() {
		String port = deviceIp.contains(":") ? deviceIp.substring(deviceIp.indexOf(':') + 1) : "80";
		String ip = deviceIp.contains(":") ? deviceIp.substring(0, deviceIp.indexOf(':')) : deviceIp;

		Socket socket = null;
		try {
			SocketAddress sockaddr = new InetSocketAddress(ip, new Integer(port));
			socket = new Socket();

			socket.connect(sockaddr, 2000);
		} catch (NumberFormatException | IOException e) {
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException ex) {
			}
		}
		return true;
	}

	/**
	 * Initalizes the addresses used for SOAP messages and to get the internal
	 * IP, if given IP is a proxy.
	 * 
	 * @throws ConnectException
	 *             Get thrown if device doesn't give answers to
	 *             GetCapabilities()
	 * @throws SOAPException
	 */
	protected void init() throws ConnectException, SOAPException {
		configService((BindingProvider) device, deviceUri);
		Capabilities capabilities = device.getCapabilities(Arrays.asList(CapabilityCategory.ALL));

		if (capabilities == null) {
			throw new ConnectException("Capabilities not reachable.");
		}

		String localDeviceUri = capabilities.getDevice().getXAddr();

		if (localDeviceUri.startsWith("http://")) {
			originalIp = localDeviceUri.replace("http://", "");
			originalIp = originalIp.substring(0, originalIp.indexOf('/'));
		} else {
			logger.error("Unknown/Not implemented local procotol!");
		}

		if (!originalIp.equals(deviceIp)) {
			isProxy = true;
		}

		if (capabilities.getMedia() != null && capabilities.getMedia().getXAddr() != null) {
			URL wsdlLocation = this.getClass().getResource(WSDLLocations.MEDIA);
			media = new MediaService(wsdlLocation).getMediaPort();
			String mediaUri = replaceLocalIpWithProxyIp(capabilities.getMedia().getXAddr());
			configService((BindingProvider) media, mediaUri);
		}

		if (capabilities.getPTZ() != null && capabilities.getPTZ().getXAddr() != null) {
			URL wsdlLocation = this.getClass().getResource(WSDLLocations.PTZ);
			ptz = new PtzService(wsdlLocation).getPtzPort();
			String ptzUri = replaceLocalIpWithProxyIp(capabilities.getPTZ().getXAddr());
			configService((BindingProvider) ptz, ptzUri);
		}

		if (capabilities.getImaging() != null && capabilities.getImaging().getXAddr() != null) {
			URL wsdlLocation = this.getClass().getResource(WSDLLocations.IMAGING);
			imaging = new ImagingService(wsdlLocation).getImagingPort();
			String imagingUri = replaceLocalIpWithProxyIp(capabilities.getImaging().getXAddr());
			configService((BindingProvider) imaging, imagingUri);
		}

		if (capabilities.getEvents() != null && capabilities.getEvents().getXAddr() != null) {
			URL wsdlLocation = this.getClass().getResource(WSDLLocations.EVENT);
			events = new EventService(wsdlLocation).getEventPort();
			String eventsUri = replaceLocalIpWithProxyIp(capabilities.getEvents().getXAddr());
			configService((BindingProvider) events, eventsUri);
		}
		
		if (capabilities.getExtension().getReceiver() != null && capabilities.getExtension().getReceiver().getXAddr() != null) {
			URL wsdlLocation = this.getClass().getResource(WSDLLocations.RECEIVER);
			receiver = new ReceiverService(wsdlLocation).getReceiverPort();
			String receiverUri = replaceLocalIpWithProxyIp(capabilities.getExtension().getReceiver().getXAddr());
			configService((BindingProvider) receiver, receiverUri);
		}
	}

	private void configService(BindingProvider bindingProvider, String serviceUrl) {
		// Add a security handler for the credentials
		final Binding binding = bindingProvider.getBinding();
		@SuppressWarnings("rawtypes")
		List<Handler> handlerList = binding.getHandlerChain();
		if (handlerList == null)
			handlerList = new ArrayList<>();
		handlerList.add(new SimpleSecurityHandler(username, password));
		binding.setHandlerChain(handlerList);

		// Set the actual web services address instead of the mock service
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);
	}

	public String replaceLocalIpWithProxyIp(String original) {
		if (original.startsWith("http:///")) {
			original.replace("http:///", "http://" + deviceIp);
		}
		if (isProxy) {
			return original.replace(originalIp, deviceIp);
		}
		return original;
	}

	/**
	 * Is used for basic devices and requests of given Onvif Device
	 */
	public Device getDevice() {
		return device;
	}

	public PTZ getPtz() {
		return ptz;
	}

	public Media getMedia() {
		return media;
	}

	public ImagingPort getImaging() {
		return imaging;
	}

	public EventPortType getEvents() {
		return events;
	}

	public DateTime getDate() {
		return device.getSystemDateAndTime().getLocalDateTime();
	}

	public DeviceInfo getDeviceInfo() {
		Holder<String> manufacturer = new Holder<>();
		Holder<String> model = new Holder<>();
		Holder<String> firmwareVersion = new Holder<>();
		Holder<String> serialNumber = new Holder<>();
		Holder<String> hardwareId = new Holder<>();
		device.getDeviceInformation(manufacturer, model, firmwareVersion, serialNumber, hardwareId);
		return new DeviceInfo(manufacturer.value, model.value, firmwareVersion.value, serialNumber.value,
				hardwareId.value);
	}

	public String getHostname() {
		return device.getHostname().getName();
	}

	public String reboot() throws ConnectException, SOAPException {
		return device.systemReboot();
	}
}
