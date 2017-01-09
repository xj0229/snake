package com.SE.onvif.service;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import com.SE.onvif.api.core.DBService;
import com.SE.onvif.beans.IpcBeanImpl;
import com.SE.onvif.core.Main;
import com.SE.onvif.persistence.IpcBean;
import com.SE.onvif.persistence.JdbcUtil;

public class JdbcService implements DBService {

	private static Logger logger = Logger.getLogger(JdbcService.class);

	private final static String getStationByIDSQL = "select st.* from tb_station st where st.id = ?";
	private final static String getStationByNo = "select st.* from tb_station st where st.st_no = ?";
	private final static String getAllStationSQL = "select st.*,  IFNULL(st_p.st_no,0) as parentNo from tb_station st left join tb_station st_p on st.parent_id = st_p.id";

	private final static String getAllDeviceSQL = "select dev.* from tb_device dev";
	private final static String getDeviceByIP = "select dev.* from tb_device dev where dev.ip = ?";
	private final static String getAllEncdecSQL = "select * from tb_encdec";
	private final static String getAllEncdec_ResourceSQL = "select * from tb_encdec_resource";
	private final static String getAllCameraSQL = "select * from tb_camera";

	private final static String getUserByUserNameSQL = "select tu.* from tb_user tu where tu.name = ?";
	private final static String getCameraByCamIdSQL = "select tc.* from tb_camera tc where tc.dev_id = ?";
	private final static String getCamerasByStIdSQL = "select tc.* from tb_camera tc,tb_device td,tb_station ts where tc.dev_id = td.id and td.stId = ts.id and ts.id = ? limit ?,?";
	private final static String getCamerasByStNoSQL = "select tc.* from tb_camera tc,tb_device td,tb_station ts where tc.dev_id = td.id and td.stId = ts.id and ts.st_no = ? limit ?,?";

	private final static String getCameraIdByNameSQL = "select tc.* from tb_camera tc,tb_device td,tb_station ts where tc.dev_id = td.id and td.stId = ts.id and td.name = ? and ts.st_no = ?";
	private final static String getTotalCameraCountSQL = "select count(*) from tb_camera tc,tb_device td,tb_station ts where tc.dev_id = td.id and td.stId = ts.id and ts.id = ?";
	private final static String getEncdecResourceByCamIdSQL = "select te.* from tb_camera tc,tb_encdec_resource te where tc.enc_resource_id = te.enc_id and tc.dev_id = ?";
	private final static String getDeviceNamesSQL = "select td.name from tb_device td where td.type = ? or td.type = ?";
	private final static String getIpMaskGateSQL = "select td.ip,td.netmask,td.gateway from tb_device td,tb_encdec_resource te where td.id = te.own_dev_id and te.enc_id = ?";

	private final static String getCardInfoSQL = "select te.enc_id, te.ip,te.marking,te.channel,td.name from tb_camera tc,tb_encdec_resource te, tb_device td where tc.enc_resource_id = te.enc_id "
			+ "and tc.dev_id = td.id and tc.dev_id = ?";
	private final static String getConDevNameAndIpSQL = "select td.name,td.ip from tb_camera tc,tb_device td where tc.control_dev_id = td.id and tc.dev_id = ?";
	private final static String getPartCamInfoSQL = "select td.name,td.devNo from tb_camera tc,tb_device td where tc.dev_id = td.id and tc.dev_id = ?";
	private final static String getBussCardInfoSQL = "select td.name,te.marking,te.ip,te.enc_id from tb_device td,tb_encdec_resource te,tb_camera tc where tc.enc_resource_id = te.enc_id and te.own_dev_id = td.id and tc.dev_id = ?";
	private final static String getUserAndPwdSQL = "select tu.name,tu.passwd from tb_user tu where tu.name = ? and tu.passwd = ?";
	private final static String getArmIpByNameSQL = "select td.ip from tb_device td where td.name = ?";
	private final static String insertDeviceSQL = "insert into tb_device (type,name,stId,devNo,ip,netmask,gateway) values(?,?,?,?,?,?,?)";
	private final static String insertCameraSQL = "insert into tb_camera (type,control_mode,control_dev_id,enc_resource_id) values(?,?,?,?)";
	private final static String insertEncdecResourceSQL = "insert into tb_encdec_resource (own_dev_id,ip,netmask,gateway,switch_proto,doom_proto,tsIp,tsPort,marking) values(?,?,?,?,?,?,?,?,?)";

	private final static String getAlarmBeanSQL = "select tr.subSystemNo,tr.siteNo,tr.sectorNo,tr.ruleNo,tr.alarmType," +
			"tr.ruleName,tu.processNo,tu.paramNo,tu.showText,tp.processName,tp.pollflg,ta.param from tb_rule tr,tb_process tp,tb_param ta,tb_rule_process_param tu where tr.ruleNo = tu.ruleNo and tu.processNo = tp.processNo and tu.paramNo = ta.paramNo"
			+ " and tr.subSystemNo = ? and tr.siteNo = ? and tr.alarmType = ? and tr.sectorNo = ?";

	private final static String getAllNetMrgDeviceSQL = "SELECT t.*,t1.id,t1.statusName,t1.statusValue,t1.memo FROM " +
			"tb_mgr_device t inner join tb_mgr_devstatus t1 on t.id = t1.devId";
	private final static String getAllNetMrgDeviceByStationIdSQL = "SELECT t.*,t2.name " +
			"FROM tb_mgr_device t inner join tb_station t2 on t.stationId = t2.id where stationId = ?";

	private final static String insertDeviceStatusLogSQL = "insert into tb_mgr_devstatus_log (devName,devType,stationName,devIP,statusName,statusValue,time,errorProperty) values(?,?,?,?,?,?,?,?)";

	private final static String insertDeviceStatusSQL = "insert into tb_mgr_devstatus (devId,statusName,statusValue,memo,errorProperty) values(?,?,?,?,?)";

	private final static String getStatusByDeviceIpAndStatusNameSQL = "SELECT t2.statusValue, t1.id,t1.devType,t1.name,t3.name,t1.ip,t2.statusName  from tb_mgr_device t1 inner join tb_mgr_devstatus t2" +
			" on t1.id = t2.devId inner join tb_station t3 on t1.stationId = t3.id where t1.ip = ? and t2.statusName = ?";

	private final static String updateDeviceStatusSQL = "update tb_mgr_devstatus set statusValue = ?, errorProperty = ? where devId = ? and statusName = ?";

	private final static String getStatusByDeviceId = "SELECT * from tb_mgr_devstatus where devId = ?";
	private final static String getStatusLog = "SELECT * from tb_mgr_devstatus_log where 1=1";

	private final static String getIpcBySipID = "select t.* from tb_ipc t where t.sipID = ?";
	private final static String getIpcByID = "select t.* from tb_ipc t where t.dev_id = ?";
	private final static String insertIpc = "insert into tb_ipc (dev_id, onvifServIPAndPort, xaddr) values(?, ?, ?)";
	private final static String updateIpcByOnvifServIPAndPort = "update tb_ipc set rtspUrl = ? where onvifServIPAndPort = ? ";
	private final static String updateIpcByID = "update tb_ipc set onvifServIPAndPort = ?, xaddr = ? where dev_id = ? ";
	private static final JdbcService jdbcService = new JdbcService();
	
	/////////////////////////////////////////////////////////
	public static final int PARAM_TYPE_STRING = 1;
	public static final int PARAM_TYPE_INT = 2;
	public static final int PARAM_TYPE_LONG = 3;
	
	public static JdbcService newInstance() {
		return jdbcService;
	}

	public void init() {

	}

	public void destroy() {

	}
	
	public IpcBean getIpcBySipID(String sipID) throws Exception, RemoteException {
		IpcBean cBean = null;
		Connection conn = null;

		try {
			conn = JdbcUtil.getInstance().getConn();
			PreparedStatement stmt = JdbcUtil.getInstance().getStatement(conn,
					JdbcService.getIpcBySipID);
			stmt.setString(1, sipID);

			ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				logger.debug("cannot find camera data!");
			else {
				cBean = new IpcBeanImpl(new Integer(rs.getInt("dev_id")),
						rs.getString("ip"), rs.getString("xaddr"), 
						rs.getString("sipID"),	rs.getString("rtspUrl"));
			}
			stmt.close();

			JdbcUtil.getInstance().commit(conn);
		} catch (Exception e) {
			JdbcUtil.getInstance().rollBack(conn);
			e.printStackTrace();
		} finally {
			JdbcUtil.getInstance().releaseConn(conn);
		}
		return cBean;
	}
	
	public IpcBean getIpcByIP(String IP) throws Exception, RemoteException {
		IpcBean cBean = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = JdbcUtil.getInstance().getConn();
			stmt = JdbcUtil.getInstance().getStatement(conn, JdbcService.getDeviceByIP);
			stmt.setString(1, IP);
			rs = stmt.executeQuery();
			int devID = -1;
			if (!rs.next())
				logger.debug("cannot find camera data!");
			else {
				devID = rs.getInt("dev_id");
			}

			stmt = JdbcUtil.getInstance().getStatement(conn,	JdbcService.getIpcByID);
			stmt.setInt(1, devID);

			rs = stmt.executeQuery();
			if (!rs.next())
				logger.debug("cannot find camera data!");
			else {
				cBean = new IpcBeanImpl(new Integer(rs.getInt("dev_id")),
						rs.getString("onvifServIPAndPort"), rs.getString("xaddr"), 
						rs.getString("sipID"),	rs.getString("rtspUrl"));
				cBean.setUserName(rs.getString("userName"));
				cBean.setPasswd(rs.getString("passwd"));
			}

			JdbcUtil.getInstance().commit(conn);
		} catch (Exception e) {
			JdbcUtil.getInstance().rollBack(conn);
			e.printStackTrace();
		} finally {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			JdbcUtil.getInstance().releaseConn(conn);
		}
		return cBean;
	}
	
	public int insertIpc(IpcBean ipcBean) throws Exception, RemoteException {
		Connection conn = null;
		PreparedStatement stmt = null;
		int rs = -1;
		try {
			conn = JdbcUtil.getInstance().getConn();
			stmt = conn.prepareStatement(JdbcService.insertDeviceSQL, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, 5);
			stmt.setString(2, "IPC");
			stmt.setInt(3, Main.getStationID()); //stationId, come from xml configure
			stmt.setInt(4, 0);
			String ip = ipcBean.getOnvifServIPAndPort();
			ip = ip.substring(0, ip.indexOf(":"));
			stmt.setString(5, ip);
			stmt.setString(6, "255.255.255.0");
			stmt.setString(7, ip.substring(0, ip.lastIndexOf(".")) + ".1");
			rs = stmt.executeUpdate();

			if(rs < 0) {
				JdbcUtil.getInstance().rollBack(conn);
				return rs;
			}
			//get device id
			int dev_id = -1;
			ResultSet ret = stmt.getGeneratedKeys();
			while(ret.next()) {
				dev_id = ret.getInt(1);
			}

			//insert table tb_ipc
			stmt = JdbcUtil.getInstance().getStatement(conn,	JdbcService.insertIpc);
			stmt.setInt(1, dev_id);
			stmt.setString(2, ipcBean.getOnvifServIPAndPort());
			stmt.setString(3, ipcBean.getXAddr());
			rs = stmt.executeUpdate();

			JdbcUtil.getInstance().commit(conn);
		} catch (Exception e) {
			JdbcUtil.getInstance().rollBack(conn);
			e.printStackTrace();
		} finally {
			stmt.close();
			JdbcUtil.getInstance().releaseConn(conn);
		}

		return rs;
	}
	
	public int updateIpcByOnvifServIPAndPort(IpcBean ipcBean) throws Exception, RemoteException {
		Connection conn = null;
		int rs = -1;
		try {
			conn = JdbcUtil.getInstance().getConn();
			PreparedStatement stmt = JdbcUtil.getInstance().getStatement(conn,
					JdbcService.updateIpcByOnvifServIPAndPort);
			stmt.setString(1, ipcBean.getRtspUrl());
			stmt.setString(2, ipcBean.getOnvifServIPAndPort());

			rs = stmt.executeUpdate();

			stmt.close();

			JdbcUtil.getInstance().commit(conn);
		} catch (Exception e) {
			JdbcUtil.getInstance().rollBack(conn);
			e.printStackTrace();
		} finally {
			JdbcUtil.getInstance().releaseConn(conn);
		}

		return rs;
	}
	
	public int updateIpcByID(IpcBean ipcBean) throws Exception, RemoteException {
		Connection conn = null;
		int rs = -1;
		try {
			conn = JdbcUtil.getInstance().getConn();
			PreparedStatement stmt = JdbcUtil.getInstance().getStatement(conn,
					JdbcService.updateIpcByID);
			stmt.setString(1, ipcBean.getOnvifServIPAndPort());
			stmt.setString(2, ipcBean.getXAddr());
			stmt.setInt(3, ipcBean.getIpcID());
			rs = stmt.executeUpdate();

			stmt.close();

			JdbcUtil.getInstance().commit(conn);
		} catch (Exception e) {
			JdbcUtil.getInstance().rollBack(conn);
			e.printStackTrace();
		} finally {
			JdbcUtil.getInstance().releaseConn(conn);
		}

		return rs;
	}
	
	public Connection getConnect() throws Exception {
		return JdbcUtil.getInstance().getConn();
	}
	
	public PreparedStatement getPreparedStatement(Connection conn, String sql) throws Exception {
		return JdbcUtil.getInstance().getStatement(conn,sql);
	}
	
	public void setStatementParam(PreparedStatement stmt, int paramType, int index, Object param) throws Exception {
		if(paramType == this.PARAM_TYPE_STRING) {
			stmt.setString(1, (String)param);
		} else if(paramType == this.PARAM_TYPE_INT) {
			stmt.setInt(index, (Integer)param);
		} else if(paramType == this.PARAM_TYPE_LONG) {
			stmt.setLong(index, (Long)param);
		}
	}
	
	public void commit(Connection conn) {
		try {
			JdbcUtil.getInstance().commit(conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rollback(Connection conn) {
		try {
			JdbcUtil.getInstance().rollBack(conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void releaseConn(Connection conn) {
		try {
			JdbcUtil.getInstance().releaseConn(conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
