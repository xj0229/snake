package Manscdp;

public class ResParam {
	// 目标设备编码
	String deviceID;
	// 命令序列号
	Integer sn;
	// 执行结果标志
	String result = ManscdpDataType.ResultType.OK;

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

	public String getresult() {
		return result;
	}

	public void setresult(String result) {
		this.result = result;
	}
}
