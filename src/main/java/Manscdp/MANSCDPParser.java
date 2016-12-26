/**
 * 
 */
package Manscdp;

import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import Manscdp.IMANSCDPParser;
import Manscdp.ManscdpDataType.ItemType;
import Manscdp.CatalogQueryParam;
import Manscdp.CatalogQueryResParam;
import Manscdp.DeviceID;
import Manscdp.MANSCDPInstrType;

/**
 * @author Administrator
 *
 */
public class MANSCDPParser implements IMANSCDPParser {

	private MANSCDPInstrType MANSCDPInstrType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sec.vc.core.parser.api.IMANSCDPParser#parse(byte[])
	 */
	public Object parse(byte[] xmlData) {
		// TODO Auto-generated method stub

		byte[] transMessage = xmlData;
		if (getMANSCDPInstrType(transMessage) == MANSCDPInstrType.QueryCatalogInstr) {
			try {
				// TODO Auto-generated method stub
				// 定义要解析的XML字符串
				// 创建xml解析对象
				SAXReader reader = new SAXReader();
				// 定义一个文档
				Document document = null;
				// 将字符串转换为
				document = reader.read(new ByteArrayInputStream(transMessage));
				// 得到xml的根节点(message)
				Element root = document.getRootElement();
				String SN = root.elementText("SN");
				String DeviceID = root.elementText("DeviceID");
				String StartTime = root.elementText("StartTime");
				String EndTime = root.elementText("EndTime");
				CatalogQueryParam catalogqueryparam = new CatalogQueryParam();
				int sn = Integer.valueOf(SN).intValue();
				catalogqueryparam.setSn(sn);
				catalogqueryparam.setDeviceID(DeviceID);
				catalogqueryparam.setStartTime(StartTime);
				catalogqueryparam.setEndTime(EndTime);
				return catalogqueryparam;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (getMANSCDPInstrType(transMessage) == MANSCDPInstrType.QueryCatalogResInstr) {
			try {
				// TODO Auto-generated method stub
				// 定义要解析的XML字符串
				// 创建xml解析对象
				SAXReader reader = new SAXReader();
				// 定义一个文档
				Document document = null;
				// 将字符串转换为
				document = reader.read(new ByteArrayInputStream(transMessage));
				// 得到xml的根节点(message)
				Element root = document.getRootElement();
				String SN = root.elementText("SN");
				String DeviceID = root.elementText("DeviceID");
				String SumNum = root.elementText("SumNum");
				CatalogQueryResParam catalogqueryresparam = new CatalogQueryResParam();
				int sn = Integer.valueOf(SN).intValue();
				catalogqueryresparam.setsn(sn);
				catalogqueryresparam.setdeviceID(DeviceID);
				int sumnum = Integer.valueOf(SumNum).intValue();
				catalogqueryresparam.setsumNum(sumnum);
				List<Element> list = root.element("DeviceList").elements();
				int parental;
				int errCode;
				double longitude;
				double latitude;
				List<ItemType> it = new ArrayList<ItemType>();
				for (int i = 0; i < list.size(); i++) {
					parental = Integer.valueOf(list.get(i).elementText("Parental")).intValue();
					errCode = Integer.valueOf(list.get(i).elementText("ErrCode")).intValue();
					longitude = Integer.valueOf(list.get(i).elementText("Longitude")).intValue();
					latitude = Integer.valueOf(list.get(i).elementText("Latitude")).intValue();
					catalogqueryresparam.setdevList(list.get(i).elementText("DeviceID"),
							list.get(i).elementText("Name"), list.get(i).elementText("Manufacturer"),
							list.get(i).elementText("ResType"), list.get(i).elementText("RecLocation"),
							list.get(i).elementText("OperateType"), list.get(i).elementText("Model"),
							list.get(i).elementText("Block"), list.get(i).elementText("Address"), parental,
							list.get(i).elementText("ParentID"), list.get(i).elementText("CertNum"), errCode,
							list.get(i).elementText("EndTime"), list.get(i).elementText("IPAddress"),
							list.get(i).elementText("Port"), list.get(i).elementText("Password"), longitude, latitude,
							list.get(i).elementText("PlayUrl"), i, it);
				}
				return catalogqueryresparam;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sec.vc.core.parser.api.IMANSCDPParser#getMANSCDPInstrType()
	 */
	public MANSCDPInstrType getMANSCDPInstrType(byte[] transMessage2) {

		try {
			// TODO Auto-generated method stub
			// 定义要解析的XML字符串
			byte[] transMessage = transMessage2;
			// 创建xml解析对象
			SAXReader reader = new SAXReader();
			// 定义一个文档
			Document document = null;
			// 将字符串转换为
			document = reader.read(new ByteArrayInputStream(transMessage));
			// 得到xml的根节点(message)
			Element root = document.getRootElement();
			String cmdtype = root.elementText("CmdType");
			System.out.println("CmdType = " + cmdtype);
			String catalog = "Catalog";
			if (cmdtype.equals(catalog)) {
				MANSCDPInstrType = MANSCDPInstrType.QueryCatalogInstr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return MANSCDPInstrType;
		// test1
	}

	public DeviceID parse(String id) {
		// TODO Auto-generated method stub
		return null;
	}
}
