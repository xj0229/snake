package Manscdp;

import Manscdp.CatalogQueryParam;
import Manscdp.CatalogQueryResParam;

public interface IMANSCDPEncode {
	/*
	 * cmdType �������ͣ�Catalog, DeviceStatus, DeviceInfo, RecordInfo, Alarm
	 * deviceID Ŀ���豸���� sn �豸���к� startTime �豸����ʼʱ�䣬Ĭ��ֵΪ0 endTime �豸����ֹʱ�䣬Ĭ��ֵΪ0
	 */
	public <T> byte[] genQueryReq(String cmdType, T t);

	/*
	 * deviceID Ŀ���豸���� sn �豸���к� startTime �豸����ʼʱ�䣬Ĭ��ֵΪ0 endTime �豸����ֹʱ�䣬Ĭ��ֵΪ0
	 */
	public byte[] genQueryCatalogReq(CatalogQueryParam param);
	/*
	 * deviceID Ŀ���豸���� sn �豸���к�
	 */

	public byte[] genQueryCatalogResponse(CatalogQueryResParam t);

}
