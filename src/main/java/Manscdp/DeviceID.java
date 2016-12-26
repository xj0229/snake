package Manscdp;

public class DeviceID {
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getGongqiName() {
		return gongqiName;
	}

	public void setGongqiName(String gongqiName) {
		this.gongqiName = gongqiName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getDomain1() {
		return domain1;
	}

	public void setDomain1(String domain1) {
		this.domain1 = domain1;
	}

	public String getDomain2() {
		return domain2;
	}

	public void setDomain2(String domain2) {
		this.domain2 = domain2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNetId() {
		return netId;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}

	public String getDevNo() {
		return devNo;
	}

	public void setDevNo(String devNo) {
		this.devNo = devNo;
	}

	public String toString() {
		return this.lineName + this.gongqiName + this.stationName + this.domain1 + this.domain2 + this.type
				+ this.devNo;
	}

	// ��·/�ϼ�����λ
	private String lineName;
	// ����
	private String gongqiName;
	// ��վ/��������/OCC
	private String stationName;
	// 1������
	private String domain1;
	// 2������
	private String domain2;
	// �豸���ͻ��û�����
	private String type;
	// �����ʶ
	private String netId;
	// �豸���к�
	private String devNo;

}
