package Manscdp;

public class CatalogQueryParam extends ReqParam {
	/*
	 * deviceID Ŀ���豸���� sn �������к� startTime �豸����ʼʱ�䣬Ĭ��ֵΪ0 endTime �豸����ֹʱ�䣬Ĭ��ֵΪ0
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
