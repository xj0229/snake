/**
 * 
 */
package com.SE.TestMain;

import com.SE.NettyServer.*;
import org.apache.log4j.Logger;
/**
 * @author BrysonH
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Logger mLogger = Logger.getLogger(Main.class);
		mLogger.debug("¿ªÊ¼");
		NettyServer server= new NettyServer(1666); 
	}

}
