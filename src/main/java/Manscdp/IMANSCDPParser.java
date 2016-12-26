package Manscdp;

import Manscdp.DeviceID;
import Manscdp.MANSCDPInstrType;

public interface IMANSCDPParser {
	public Object parse(byte[] xmlData);

	public MANSCDPInstrType getMANSCDPInstrType(byte[] xmlData);

	// ����20λ����
	public DeviceID parse(String id);
}
