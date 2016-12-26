package Manscdp;

import com.SE.SipServer.ISipMessageProcessor;

class Main {
	public static void main(String[] args) {
		ISipMessageProcessor Processor = new IMessageProcessor();
		String fromAddress = "192.168.1.100";
		String messageContent = "<?xml version=\"1.0\"?><Query><CmdType>Catalog</CmdType><SN>248</SN><DeviceID>34020000001110000001</DeviceID></Query>";
		Processor.messageProcessor(fromAddress, messageContent);
	}
}
