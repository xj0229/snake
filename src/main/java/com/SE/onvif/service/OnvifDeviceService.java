package com.SE.onvif.service;

import java.net.ConnectException;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.onvif.ver10.media.wsdl.Capabilities;
import org.onvif.ver10.media.wsdl.Media;
import org.onvif.ver10.media.wsdl.ProfileCapabilities;
import org.onvif.ver10.media.wsdl.StreamingCapabilities;
import org.onvif.ver10.schema.IntRectangle;
import org.onvif.ver10.schema.MediaUri;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.StreamSetup;
import org.onvif.ver10.schema.StreamType;
import org.onvif.ver10.schema.Transport;
import org.onvif.ver10.schema.TransportProtocol;
import org.onvif.ver10.schema.VideoResolution;
import org.onvif.ver10.schema.VideoSource;

import com.SE.onvif.api.core.DBService;
import com.SE.onvif.beans.IpcBeanImpl;
import com.SE.onvif.persistence.IpcBean;
import com.SE.onvif.soap.OnvifDevice;
import com.SE.onvif.util.ServiceFactory;


/**
 * @author zhangjp
 *
 */
/**
 * @author Administrator
 *
 */
public class OnvifDeviceService {
	
	public List<String> getDevInfo(String onvifServIPAndPort, String userName, String password) {
		OnvifDevice onvifDev =null;
		long startTime = System.currentTimeMillis();
		try {
			onvifDev = new OnvifDevice(onvifServIPAndPort, userName, password);
			Media media = onvifDev.getMedia();
			List<Profile> profiles = media.getProfiles();
			String profileToken = null;
			StreamSetup ss = new StreamSetup();
			ss.setStream(StreamType.RTP_UNICAST);
			Transport tport = new Transport();
			tport.setProtocol(TransportProtocol.RTSP);
			ss.setTransport(tport);
			
			for(Profile p : profiles) {
				profileToken = p.getName();
				IntRectangle bounds = p.getVideoSourceConfiguration().getBounds();
				System.out.println("video source bounds :" + bounds.getHeight() + " * " + bounds.getWidth());
				
				VideoResolution vr = p.getVideoEncoderConfiguration().getResolution();
				System.out.println("video encode resolution :" + vr.getHeight() + " * " + vr.getWidth());
				
				MediaUri mediaUri = media.getStreamUri(ss, profileToken);
				String rtspUri = mediaUri.getUri();
				System.out.println("rtsp uri :" + rtspUri);
				if(vr.getHeight() == 1080) {
					System.out.println("高清视频源！");
					try {
						this.updateDBInfo(onvifServIPAndPort, rtspUri);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			List<VideoSource> vsList = media.getVideoSources();
			Capabilities capbilities = media.getServiceCapabilities();
			ProfileCapabilities pc = capbilities.getProfileCapabilities();
			StreamingCapabilities sc = capbilities.getStreamingCapabilities();
			
			}
		catch (ConnectException | SOAPException e1) {
			System.err.println("No connection to device -> "+onvifServIPAndPort+", please try again.");
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Elapse time for onvif device :" + (endTime - startTime)/1000.0);
		
		return null;
	}

	private  List<Profile> getProfiles(OnvifDevice onvifDev)  {

		List<Profile> profiles = onvifDev.getMedia().getProfiles();
		for (Profile p : profiles) {
			System.out.println("URL from Profile \'" + p.getName() + "\': " + onvifDev.getMedia().getSnapshotUri(p.getToken()));
		}
		return profiles;
	}
	
	/**
	 * 更新Onvif设备的相关信息到数据表tb_ipc
	 * @return 0 - 更新成功；
	 */
	private int updateDBInfo(String onvifServIPAndPort, String streamUri) throws Exception {
		int ret = -1;
		DBService dbService = ServiceFactory.getDBService();
		
		IpcBean ib = new IpcBeanImpl();
		ib.setOnvifServIPAndPort(onvifServIPAndPort);
		ib.setRtspUrl(streamUri);
		ret = dbService.updateIpcByOnvifServIPAndPort(ib);
		return ret;
	}
}
