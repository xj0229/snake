package Manscdp;

import com.SE.SipServer.ISipMessageProcessor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Manscdp.CatalogQueryParam;
import Manscdp.MANSCDPInstrType;
import Manscdp.MANSCDPParser;
import Manscdp.ManscdpDataType.ItemType;
import Manscdp.IMANSCDPEncode;
import Manscdp.MANSCDPEncode;

public class IMessageProcessor implements ISipMessageProcessor {

	public String messageProcessor(String fromAddress, String messageContent) {
		String sender = "";
		IMANSCDPParser cdpParser = new MANSCDPParser();
		byte[] xmlData = messageContent.getBytes();
		MANSCDPInstrType type = cdpParser.getMANSCDPInstrType(xmlData);
		if (type == MANSCDPInstrType.QueryCatalogInstr) {
			// 200OK 发起onvif请求
			CatalogQueryResParam resparam = new CatalogQueryResParam();
			IMANSCDPEncode encoder = new MANSCDPEncode();
			resparam.setsn(3);
			resparam.setdeviceID("00151000000401000001");
			resparam.setsumNum(1);
			List<ItemType> it = new ArrayList<ItemType>();
			resparam.setdevList("DeviceID", "Name", "Manufacturer", "ResType", "RecLocation", "OperateType", "Model",
					"Block", "Address", 1, "ParentID", "CertNum", 1, "EndTime", "IPAddress", "Port", "Password", 1, 1,
					"PlayUrl", 0, it);
			byte[] senderb = encoder.genQueryCatalogResponse(resparam);
			try {
				sender = new String(senderb, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(sender);
			// 构建UDP包发送
		}
		return sender;
	}

	public void errorProcessor(String errorMessage) {
		return;
	}

	public void infoProcessor(String infoMessage) {
		return;
	}

	public List<String> messageProcessors(String fromAddress, String messageContent) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean messageCheck(String messageContent) {
		// TODO Auto-generated method stub
		return null;
	}
}
