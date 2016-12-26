/**
 * 
 */
package com.SE.SipServer;

import java.util.List;

/**
 * @author Administrator
 *
 */
public interface ISipMessageProcessor {
	public String messageProcessor (String fromAddress, String messageContent);
	public List<String> messageProcessors (String fromAddress, String messageContent);
	public Boolean messageCheck(String messageContent);
	public void  errorProcessor (String errorMessage);
	public void  infoProcessor (String infoMessage);
}
