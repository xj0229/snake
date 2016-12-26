/**
 * 
 */
package Manscdp;

import Manscdp.IMANSCDPEncode;
import Manscdp.CatalogQueryParam;
import Manscdp.CatalogQueryResParam;
import Manscdp.ResParam;

/**
 * @author Administrator
 *
 */
public class MANSCDPEncode implements IMANSCDPEncode {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sec.vc.core.parser.api.IMANSCDPEncode#genQueryReq(java.lang.String,
	 * java.lang.Object)
	 */
	public <T> byte[] genQueryReq(String cmdType, T t) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sec.vc.core.parser.api.IMANSCDPEncode#genQueryCatalogReq(com.sec.vc.
	 * core.parser.api.manscdp.CatalogQueryParam)
	 */
	public byte[] genQueryCatalogReq(CatalogQueryParam param) {
		// TODO Auto-generated method stub
		String req;
		req = "<?xmlversion=\"1.0\"?>\n" + "<Query>\n" + "<CmdType>Catalog</CmdType>\n" + "<SN>" + param.getSn()
				+ "</SN>\n" + "<DeviceID>" + param.getDeviceID() + "</DeviceID>\n" + "</Query>";
		byte[] request = req.getBytes();
		return request;
	}

	public byte[] genQueryCatalogResponse(CatalogQueryResParam t) {
		// TODO Auto-generated method stub
		String req;
		req = "<?xmlversion=\"1.0\"?>\n" + "<Response>\n" + "<CmdType>Catalog</CmdType>\n" + "<SN>" + t.getsn()
				+ "</SN>\n" + "<DeviceID>" + t.getdeviceID() + "</DeviceID>\n" + "<SumNum>" + t.getsumNum()
				+ "</SumNum>\n" + "<DeviceList Num=\"" + t.getdevList().size() + "\">\n";
		// for (int i=0;i<t.getdevList().size();i++)
		for (int i = 0; i < 1; i++) {
			req = req + "<Item>";
			req = req + "<DeviceID>" + t.getdevList().get(i).getdeviceID() + "</DeviceID>\n";
			req = req + "<Name>" + t.getdevList().get(i).getname() + "</Name>\n";
			req = req + "<Manufacturer>" + t.getdevList().get(i).getmanufacture() + "</Manufacturer>\n";
			req = req + "<ResType>" + t.getdevList().get(i).getResType() + "</ResType>\n";
			req = req + "<RecLocation>" + t.getdevList().get(i).getRecLocation() + "</RecLocation>\n";
			req = req + "<OperateType>" + t.getdevList().get(i).getOperateType() + "</OperateType>\n";
			req = req + "<Model>" + t.getdevList().get(i).getmodel() + "</Model>\n";
			req = req + "<Owner>" + t.getdevList().get(i).getowner() + "</Owner>\n";
			req = req + "<CivilCode>" + t.getdevList().get(i).getcivilCode() + "</CivilCode>\n";
			req = req + "<Block>" + t.getdevList().get(i).getblock() + "</Block>\n";
			req = req + "<Address>" + t.getdevList().get(i).getaddress() + "</Address>\n";
			req = req + "<Parental>" + t.getdevList().get(i).getparental() + "</Parental>\n";
			req = req + "<ParentID>" + t.getdevList().get(i).getparentID() + "</ParentID>\n";
			req = req + "<SafetyWay>0</SafetyWay>\n";
			req = req + "<RegisterWay>1</RegisterWay>\n";
			req = req + "<CertNum>" + t.getdevList().get(i).getcertNum() + "</CertNum>\n";
			req = req + "<Certifiable>0</Certifiable>\n";
			req = req + "<ErrCode>" + t.getdevList().get(i).geterrCode() + "</ErrCode>\n";
			req = req + "<EndTime>" + t.getdevList().get(i).getendTime() + "</EndTime>\n";
			req = req + "<Secrecy>0</Secrecy>\n";
			req = req + "<IPAddress>" + t.getdevList().get(i).getipAddress() + "</IPAddress>\n";
			req = req + "<Port>" + t.getdevList().get(i).getPort() + "</Port>\n";
			req = req + "<Password>" + t.getdevList().get(i).getpassword() + "</Password>\n";
			req = req + "<Status>ON</Status>\n";
			req = req + "<Longitude>" + t.getdevList().get(i).getlongitude() + "</Longitude>\n";
			req = req + "<Latitude>" + t.getdevList().get(i).getlatitude() + "</Latitude>\n";
			req = req + "<PlayUrl>" + t.getdevList().get(i).getPlayUrl() + "</PlayUrl>\n";
			req = req + "<Item>";
		}
		req = req + "</DeviceList>\n" + "</Response>";
		byte[] request = req.getBytes();
		return request;
	}
}
