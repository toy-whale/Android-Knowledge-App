package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {


    public boolean login(String username, String password) {
    	
    	String sql = "select * from users where username = ? and password = ?";
        Connection conn = JDBCUtils.getConn();
        
        if (conn != null) {
        	try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                
                if(ps.executeQuery().next()){
                    return true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }

    public boolean register(User user) {
    	
    	if (user == null) return false;
    	
        String sql = "insert into users(username, password) values (?, ?)";
        Connection conn = JDBCUtils.getConn();

        if (conn != null) {
        	try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                int value = ps.executeUpdate();

                if (value > 0){
                    return true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public User findUser(String username) {
        String sql = "select * from users where username = ?";
        Connection conn = JDBCUtils.getConn();
        
        User user = null;
        
        if (conn != null) {
        	try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                while (rs.next()){
                   int id = rs.getInt(1);
                   String username_db = rs.getString(2);
                   String password_db  = rs.getString(3);
                   user = new User(id, username_db, password_db);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return user;
    }
}