/**
 * 
 */
package Manscdp;

/**
 * @author Administrator
 *
 */
public class ReqParam {
	// 目标设备编码
	String deviceID;
	// 命令序列号
	Integer sn;

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}
}
