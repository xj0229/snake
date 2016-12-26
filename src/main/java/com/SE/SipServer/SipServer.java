/**
 * 
 */
package com.SE.SipServer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TooManyListenersException;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import gov.nist.javax.sip.header.SIPHeaderNames;
import gov.nist.javax.sip.header.WWWAuthenticate;
import gov.nist.javax.sip.message.SIPRequest;
import Manscdp.IMessageProcessor;
/**
 * SipLayer is the core server of SIP, all basic IO services are implemented in this class
 * @author Bryson Han
 * @Time 
 */
public class SipServer implements SipListener{
	//Object for processing message
	private ISipMessageProcessor m_iMessageProcessor;
	
	private String m_userName;
	//Object of sip factory to implement sip stack
	private SipFactory m_sipFactory;
	//Object of message factory to implement objects related to message
	private MessageFactory m_messageFactory;
	//Object of address factory to implement objects related to address
	private AddressFactory m_addressFactory;
	//First interface to create ListeningPoint and SipProvider
	private SipStack m_sipStack;
	//Object of header factory to implement objects related to header
	private HeaderFactory m_headerFactory;
	//SipProvide is used for providing message sending service
	private SipProvider m_sipProvider;
	//Built a instance for Manscdp message processor
	private ISipMessageProcessor m_manscdpProcessor = new Manscdp.IMessageProcessor();
	
	private String m_host;
	
	private int m_port;
	
	/*
	 * Siplayer construction
	 */
	public SipServer (String user, String ip, int port) throws 
	PeerUnavailableException, TransportNotSupportedException,
    InvalidArgumentException, ObjectInUseException,
    TooManyListenersException{
		setUserName(user);
		setHost(ip);
		setPort(port);
		m_sipFactory = SipFactory.getInstance();
		//SEFirstSip represents the method name implementing a sip stack instance
		m_sipFactory.setPathName("SEFirstSip");
		Properties sipProperties = new Properties();
		//set the sip stack name as StationSipServer
		sipProperties.setProperty("javax.sip.STACK_NAME", "StationSipServer");
		//set the sip stack ip address from the value ip 
		sipProperties.setProperty("javax.sip.IP_ADDRESS", ip);
		
		m_sipStack = m_sipFactory.createSipStack(sipProperties);
		m_headerFactory = m_sipFactory.createHeaderFactory();
		m_addressFactory = m_sipFactory.createAddressFactory();
		m_messageFactory = m_sipFactory.createMessageFactory();
		
		//Create two listening points separately for tcp and udp
		ListeningPoint tcp_listening = m_sipStack.createListeningPoint(user, port, "tcp");
		ListeningPoint udp_listening = m_sipStack.createListeningPoint(user, port, "udp");
		//SipLayer implements SipListener that can be the listener
		m_sipProvider = m_sipStack.createSipProvider(tcp_listening);
		m_sipProvider.addSipListener(this);
		
		m_sipProvider = m_sipStack.createSipProvider(udp_listening);
		m_sipProvider.addSipListener(this);
	}
	
	/*
	 * User name setter
	 */
	public void setUserName (String userName){
		this.m_userName = userName;
	}
	
	/*
	 * User name getter
	 */
	public String getUserName (){
		return this.m_userName;
	}
	
	/*
	 * Host setter
	 */
	public void setHost (String ip){
		this.m_host = ip;
	}
	
	/*
	 * Host getter
	 */
	public String getHost (){
		return this.m_host;
	}
	
	/*
	 * Port setter
	 */
	public void setPort (int port){
		this.m_port = port;
	}
	
	/*
	 * Port getter
	 */
	public int getPort (){
		return this.m_port;
	}
	
	/*
	 * Implementing the same method in SipListener, that is used to process response
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processResponse(javax.sip.ResponseEvent)
	 */
	public void processResponse(ResponseEvent evt) {
		//Get the response object from the response event
		Response sipResponse = evt.getResponse();
		//Get the Status Code of response
		int responseStatus = sipResponse.getStatusCode();
		
		if( (responseStatus >= 200) && (responseStatus < 300) ){//Communication is succeed!
			
		}
		else{//Communication is failed!
			
		}
	}
	
	/*
	 * Implementing the same method in SipListener, that is used to process request
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processRequest(javax.sip.RequestEvent)
	 */
	public void processRequest(RequestEvent evt) {
		Request sipRequest = evt.getRequest();
		//Get the request method type
		String sipMethod  = sipRequest.getMethod();
		
		if (sipMethod.equals("MESSAGE")){
			//get the request header "From"
			FromHeader messageFrom = (FromHeader)sipRequest.getHeader("From");
			//get the request message content
			String messageContent = new String(sipRequest.getRawContent());
			//get the request sender ip from header "From"
			String fromAddress = messageFrom.getAddress().toString();
			//get the request message content type to decide use which message processor 
			ContentTypeHeader sipContentTypeHeader = (ContentTypeHeader)sipRequest.getHeader("ContentType");
			String messageContSubType = sipContentTypeHeader.getContentSubType();
			boolean checkResult = m_manscdpProcessor.messageCheck(messageContent);
			if (checkResult)
			{
				try {
					Response directResponse = m_messageFactory.createResponse(200, sipRequest);
					ServerTransaction messageTrans = m_sipProvider.getNewServerTransaction(sipRequest);
					messageTrans.sendResponse(directResponse);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransactionAlreadyExistsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransactionUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				List<String> responseContent = m_manscdpProcessor.messageProcessors(fromAddress, messageContent);
				if (responseContent.get(2) == "sip")
				{
					try {
						sendMessage(fromAddress,responseContent.get(1));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (responseContent.get(2) == "onvif")
				{
				}
				else
				{
				}
			}
		}
		else if (sipMethod.equals("INVITE")){
			
		}
		else if (sipMethod.equals("REGISTER")){
			
		}
		else if (sipMethod.equals("CANCEL")){
			
		}
		else if (sipMethod.equals("BYE")){
			
		}
		else if (sipMethod.equals("OPTIONS")){
			
		}
		else if (sipMethod.equals("ACK")){
			
		}
		else{
			
		}
	}

	/*
	 * Implementing the same method in SipListener
	 * that is used to process things when dialog terminated
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processDialogTerminated(javax.sip.DialogTerminatedEvent)
	 */
	public void processDialogTerminated(DialogTerminatedEvent evt){
		
	}
	
	/*
	 * Implementing the same method in SipListener, that is used to process IO exception
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processIOException(javax.sip.IOExceptionEvent)
	 */
	public void processIOException(IOExceptionEvent evt){
		
	}
	
	/*
	 * Implementing the same method in SipListener, that is used to process things when time out
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processTimeout(javax.sip.TimeoutEvent)
	 */
	public void processTimeout (TimeoutEvent evt){
		
	}
	
	/*
	 * Implementing the same method in SipListener
	 * that is used to process things when transaction terminated
	 * (non-Javadoc)
	 * @see javax.sip.SipListener#processTransactionTerminated(javax.sip.TransactionTerminatedEvent)
	 */
	public void processTransactionTerminated(TransactionTerminatedEvent evt){
		
	}
	
	/*
	 * Package send message according to request or response
	 */
	public void sendMessage(String to, String message) throws
    ParseException, InvalidArgumentException, SipException {
		//From
		SipURI sendFrom = m_addressFactory.createSipURI(getUserName(),getHost() + ":" + getPort());
		Address sendFromAddress = m_addressFactory.createAddress(sendFrom);
		sendFromAddress.setDisplayName(getUserName());
		FromHeader sendFromHeader = m_headerFactory.createFromHeader(sendFromAddress, "SEVideoServer");
		//To
		String toUserName = to.substring(to.indexOf(":")+1, to.indexOf("@"));
		String toStringAddress = to.substring(to.indexOf("@")+1);
		SipURI toAddress = m_addressFactory.createSipURI(toUserName, toStringAddress);
		Address toNameAddress = m_addressFactory.createAddress(toAddress);
        toNameAddress.setDisplayName(toUserName);
        ToHeader toHeader = m_headerFactory.createToHeader(toNameAddress, null);
        //URI
        SipURI requestURI = m_addressFactory.createSipURI(toUserName, toStringAddress);
        requestURI.setTransportParam("udp");
        //Header
        ArrayList viaHeaders = new ArrayList();
        ViaHeader viaHeader = m_headerFactory.createViaHeader(getHost(),getPort(),"udp",null);
        viaHeaders.add(viaHeader);
        //CallID
        CallIdHeader sendCallIdHeader = m_sipProvider.getNewCallId();
        //CSeq
        @SuppressWarnings("deprecation")
		CSeqHeader sendCSeqHeader = m_headerFactory.createCSeqHeader(1, Request.MESSAGE);
        //MaxForward
        MaxForwardsHeader sendMaxForwards = m_headerFactory.createMaxForwardsHeader(70);
        
        SipURI sendContactURI = m_addressFactory.createSipURI(getUserName(), getHost());
        sendContactURI.setPort(getPort());
        Address sendContactAddress = m_addressFactory.createAddress(sendContactURI);
        sendContactAddress.setDisplayName(getUserName());
        ContactHeader contactHeader = m_headerFactory.createContactHeader(sendContactAddress);

        ContentTypeHeader contentTypeHeader;
	}
}
