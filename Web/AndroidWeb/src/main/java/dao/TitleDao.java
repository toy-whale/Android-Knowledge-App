package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TitleDao {
	
	public List<Title> findAllTitle(String username) {
		if (username == null || username.equals(""))
			return null;
		
		String sql = "select * from titles where username = ?";
        Connection conn = JDBCUtils.getConn();
		List<Title> result = new ArrayList<Title>();
		result.clear();
		
		if (conn != null) {
        	try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                while (rs.next()){
                   String title_db = rs.getString(2);
                   String subject_db  = rs.getString(3);
                   Title tmp = new Title(title_db, subject_db);
                   result.add(tmp);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JDBCUtils.closeConn(conn);
            }
        }
		return result;
	}
	
	public boolean insertAllTitle(String username, List<Title> collect) {
		if (username == null || username.equals(""))
			return false;
		
		String sql = "insert into titles(username, title, subject) values (?, ?, ?)";
	
        Connection conn = JDBCUtils.getConn();
		
		if (conn != null) {
        	try {
        		for (int i = 0; i < collect.size(); i++) {
        			Title tmp = collect.get(i);
        			boolean isExist = judgeExist(tmp, username);
        			if (isExist) continue;
        			PreparedStatement ps = conn.prepareStatement(sql);
        			ps.setString(1, username);
                    ps.setString(2, tmp.getTitle());
                    ps.setString(3, tmp.getSubject());
                    ps.executeUpdate();
        		}

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JDBCUtils.closeConn(conn);
            }
        }
		return true;
	}
	
	public boolean deleteAllTitle(String username) {
		if (username == null || username.equals(""))
			return false;
		
		String sql = "delete from titles where username = ?";
	
        Connection conn = JDBCUtils.getConn();
		
		if (conn != null) {
        	try {
        		PreparedStatement ps = conn.prepareStatement(sql);
    			ps.setString(1, username);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JDBCUtils.closeConn(conn);
            }
        }
		return true;
	}
	
	public boolean judgeExist(Title t, String username) {
		if (username == null || username.equals(""))
			return true;
		String sql = "select * from titles where username = ? and title = ?";
        Connection conn = JDBCUtils.getConn();
        
        if (conn != null) {
        	try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, t.getTitle());
                ResultSet rs = ps.executeQuery();

                while (rs.next()){
                   return true;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBCUtils.closeConn(conn);
            }
        }
        
        return false;
	}

}
