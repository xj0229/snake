package Manscdp;

import Manscdp.DeviceID;
import Manscdp.MANSCDPInstrType;

public interface IMANSCDPParser {
	public Object parse(byte[] xmlData);

	public MANSCDPInstrType getMANSCDPInstrType(byte[] xmlData);

	// ½âÎö20Î»±àÂë
	public DeviceID parse(String id);
}
