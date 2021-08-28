package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtils {
	
	static {
		try {
			// Load MySql Driver 8.0+
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConn() {
		Connection conn = null;
		try {
			// Connect to local MySql Database
			// Replace your port(3306), account("root"), password("root") here
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false&allowPublicKeyRetrieval=true","root","root");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
