package com.SE.onvif.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Stack;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

public class JdbcUtil {

	private Logger logger = Logger.getLogger(JdbcUtil.class);
	private static JdbcUtil instance;
	private Connection DB_CONN;
	private DataSource dataSource;
	private Stack<Connection> stack;
	private final int MAX_COUNT = 10;
	private int currentCount;

	public JdbcUtil() {
		stack = new Stack<Connection>();
		currentCount = 0;
		try {
			System.out.println("Loading underlying JDBC driver.");
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Done.");
			
			lookupDataSource();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		init(dataSource);
	}

	private void init(DataSource dataSource) {
		synchronized (stack) {
			if (this.stack.isEmpty()) {
				for (int i = 0; i < this.MAX_COUNT; i++) {
					try {
						logger.info("Getting connection ...");
						DB_CONN = dataSource.getConnection();
						logger.info("Get a connection DB_CONN :" + DB_CONN);
						DB_CONN.setAutoCommit(false);
						stack.push(DB_CONN);
						this.currentCount++;
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	public synchronized static JdbcUtil getInstance() {
		if (instance == null) {
			instance = new JdbcUtil();
		}

		return instance;
	}

	/*
	 * Lookup the TradeData DataSource
	 */
	private void lookupDataSource() throws Exception {
		if (dataSource == null) {
			String connectURI = "jdbc:mysql://localhost:3306/secvideoplatform?" +
											"autoReconnect=true&useSSL=false&user=root&password=mysql";
			dataSource = setupDataSource(connectURI);
			/*
			String name = "osgi:service/" + DataSource.class.getName();
			name = name + "/" + "(osgi.jndi.service.name=jdbc/dvxDataSource)";

			try {
				InitialContext ic = new InitialContext();
				dataSource = (DataSource) ic.lookup(name);
			} catch (NamingException e) {
				logger.debug("NamingException on OSGI service lookup" + name);

				e.printStackTrace();
			}
			*/
		}
	}

	/*
	 * Allocate a new connection to the datasource
	 */
//	public synchronized Connection getConn() throws Exception {
//		if (DB_CONN == null) {
//
//			if (this.currentCount != 0 && this.currentCount < this.MAX_COUNT) {
//				this.stack.pop();
//				this.currentCount--;
//			}
//		}
//
//		return DB_CONN;
//	}
	
	public synchronized Connection getConn() throws Exception {
		DB_CONN = dataSource.getConnection();
		DB_CONN.setAutoCommit(false);
		return DB_CONN;
	}

//	public synchronized void releaseConn(Connection conn) throws Exception {
//		if (conn != null && (!conn.isClosed())) {
//			if (this.currentCount < this.MAX_COUNT) {
//				stack.push(DB_CONN);
//				this.currentCount++;
//			}
//		}
//	}
	
	public synchronized void releaseConn(Connection conn) throws Exception {
		if (conn != null ) {
			conn.close();
		}
	}
	/*
	 * Commit the provided connection if not under Global Transaction scope -
	 * conn.commit() is not allowed in a global transaction. the txn manager
	 * will perform the commit
	 */
	public void commit(Connection conn) throws Exception {

		conn.commit();
	}

	/*
	 * Rollback the statement for the given connection
	 */
	public void rollBack(Connection conn) throws Exception {
		if (conn != null)
			conn.rollback();
	}

	/*
	 * Allocate a new prepared statment for this connection
	 */
	public PreparedStatement getStatement(Connection conn, String sql)
			throws Exception {
		return conn.prepareStatement(sql);
	}
	
	public DataSource setupDataSource(String connectURI) {
		//
        // First, we'll need a ObjectPool that serves as the
        // actual pool of connections.
        //
        // We'll use a GenericObjectPool instance, although
        // any ObjectPool implementation will suffice.
        //
        ObjectPool connectionPool = new GenericObjectPool(null);

        //
        // Next, we'll create a ConnectionFactory that the
        // pool will use to create Connections.
        // We'll use the DriverManagerConnectionFactory,
        // using the connect string passed in the command line
        // arguments.
        //
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI,null);
//        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:mysql://localhost:3306/dvx200ver100?autoReconnect=true&useSSL=false", "root", "mysql");

        //
        // Now we'll create the PoolableConnectionFactory, which wraps
        // the "real" Connections created by the ConnectionFactory with
        // the classes that implement the pooling functionality.
        //
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);

        //
        // Finally, we create the PoolingDriver itself,
        // passing in the object pool we created.
        //
        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

        return dataSource;
    }
}
