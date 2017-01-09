package com.SE.onvif.beans;

import com.SE.onvif.persistence.IpcBean;

public class IpcBeanImpl implements IpcBean {
	private Integer ipcID;
	private String onvifServIPAndPort;
	private String xaddr;
	private String sipID;
	private String rtspUrl;
	private String userName;
	private String passwd;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public IpcBeanImpl() {

	}

	public IpcBeanImpl(Integer ipcID, String ipAndPort, String xaddr, String sipID, String rtspUrl) {
		this.setIpcID(ipcID);
		this.setOnvifServIPAndPort(ipAndPort);
		this.setXAddr(xaddr);
		this.setSipID(sipID);
		this.setRtspUrl(rtspUrl);
	}

	public Integer getIpcID() {
		// TODO Auto-generated method stub
		return this.getIpcID();
	}

	public void setIpcID(Integer ipcID) {
		// TODO Auto-generated method stub
		this.ipcID = ipcID;
	}

	public String getOnvifServIPAndPort() {
		// TODO Auto-generated method stub
		return this.onvifServIPAndPort;
	}

	public void setOnvifServIPAndPort(String ipAndPort) {
		// TODO Auto-generated method stub
		this.onvifServIPAndPort = ipAndPort;
	}

	public String getSipID() {
		// TODO Auto-generated method stub
		return this.sipID;
	}

	public void setSipID(String sipID) {
		// TODO Auto-generated method stub
		this.sipID = sipID;
	}

	public String getRtspUrl() {
		// TODO Auto-generated method stub
		return this.rtspUrl;
	}

	public void setRtspUrl(String rtspUrl) {
		// TODO Auto-generated method stub
		this.rtspUrl = rtspUrl;
	}

	public String getXAddr() {
		// TODO Auto-generated method stub
		return this.xaddr;
	}

	public void setXAddr(String xaddr) {
		// TODO Auto-generated method stub
		this.xaddr = xaddr;
	}

}
