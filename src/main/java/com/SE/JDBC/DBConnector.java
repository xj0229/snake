/**
 * 
 */
package com.SE.JDBC;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * @author Administrator
 *
 */
public class DBConnector {
	public void connectDB(){ 
	 try {
		 //装载数据库连接驱动
         Class.forName("com.mysql.jdbc.Driver");  
         System.out.println("开始连接数据库"); 
	 }
	 catch (ClassNotFoundException e) {
		 System.out.println("数据库驱动加载异常！");
         e.printStackTrace();  
     } 
	 try{
         Connection conn = (Connection) DriverManager.getConnection(  
                 "jdbc:mysql://localhost/test", "root", "123456hbx");  
         System.out.println("连接到MySQL数据库" + conn);
         /*
         Statement stmt = (Statement) conn.createStatement();  
         ResultSet rs = stmt.executeQuery("select * from users");  
         while (rs.next()) {  
             String name = rs.getString("uname");  
             String contact = rs.getString("contact");  
             System.out.println("name------" + name + "--------contact-" + contact);  
         }*/  
     }
	 catch (SQLException e1) {
		 System.out.println("数据库连接异常！");
         e1.printStackTrace();  
     } 
	}
}
