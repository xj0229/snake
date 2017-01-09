package com.SE.onvif.persistence;

public interface IpcBean {
	public Integer getIpcID();
	public void setIpcID(Integer ipcID);
	
	public String getOnvifServIPAndPort();
	public void setOnvifServIPAndPort(String ipAndPort);
	
	public String getXAddr();
	public void setXAddr(String xaddr);
	
	public String getSipID();
	public void setSipID(String sipID);
	
	public String getRtspUrl();
	public void setRtspUrl(String rtspUrl);
	
	public String getUserName();
	public void setUserName(String userName);
	
	public String getPasswd();
	public void setPasswd(String passwd);
}
