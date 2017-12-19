package cn.ucas.irsys.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DBCPUtil {
	private static DataSource ds;
	
	private static Properties gprops; // Global Props
	
	static {
		try {
			InputStream in = DBCPUtil.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
			InputStream gin = DBCPUtil.class.getClassLoader().getResourceAsStream("globalproperties.properties");

			Properties prop = new Properties();
			gprops = new Properties();
			gprops.load(gin);
			prop.load(in);
			ds = BasicDataSourceFactory.createDataSource(prop);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static Properties getGProperties() {
		return gprops;
	}
	
	public static DataSource getDatasource() {
		return ds;
	}
	
	public static Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
