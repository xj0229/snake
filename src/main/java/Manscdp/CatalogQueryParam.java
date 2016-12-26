package Manscdp;

public class CatalogQueryParam extends ReqParam {
	/*
	 * deviceID 目标设备编码 sn 命令序列号 startTime 设备的起始时间，默认值为0 endTime 设备的终止时间，默认值为0
	 */

	String startTime;
	String endTime;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
