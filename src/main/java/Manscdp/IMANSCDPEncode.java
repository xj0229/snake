package Manscdp;

import Manscdp.CatalogQueryParam;
import Manscdp.CatalogQueryResParam;

public interface IMANSCDPEncode {
	/*
	 * cmdType 命令类型，Catalog, DeviceStatus, DeviceInfo, RecordInfo, Alarm
	 * deviceID 目标设备编码 sn 设备序列号 startTime 设备的起始时间，默认值为0 endTime 设备的终止时间，默认值为0
	 */
	public <T> byte[] genQueryReq(String cmdType, T t);

	/*
	 * deviceID 目标设备编码 sn 设备序列号 startTime 设备的起始时间，默认值为0 endTime 设备的终止时间，默认值为0
	 */
	public byte[] genQueryCatalogReq(CatalogQueryParam param);
	/*
	 * deviceID 目标设备编码 sn 设备序列号
	 */

	public byte[] genQueryCatalogResponse(CatalogQueryResParam t);

}
