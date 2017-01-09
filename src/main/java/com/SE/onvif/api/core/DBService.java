package com.SE.onvif.api.core;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.SE.onvif.persistence.IpcBean;

public interface DBService {

	public IpcBean getIpcBySipID(String sipID) throws Exception, RemoteException;
	public IpcBean getIpcByIP(String IP) throws Exception, RemoteException;
	public int insertIpc(IpcBean ipcBean) throws Exception, RemoteException;
	public int updateIpcByOnvifServIPAndPort(IpcBean ipcBean) throws Exception, RemoteException;
	public int updateIpcByID(IpcBean ipcBean) throws Exception, RemoteException;
	
	public Connection getConnect() throws Exception;
	public PreparedStatement getPreparedStatement(Connection conn, String sql) throws Exception;
	public void setStatementParam(PreparedStatement stmt, int paramType, int index, Object param) throws Exception;
	public void commit(Connection conn) ;
	public void rollback(Connection conn) ;
	public void releaseConn(Connection conn) ;

}
