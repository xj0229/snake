package Manscdp;

public class ResParam {
	// Ŀ���豸����
	String deviceID;
	// �������к�
	Integer sn;
	// ִ�н����־
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
