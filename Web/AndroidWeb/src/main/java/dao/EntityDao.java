package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntityDao {

	public List<Entity> findAllEntity(String username) {
		if (username == null || username.equals(""))
			return null;
		
		String sql = "select * from entities where username = ?";
        Connection conn = JDBCUtils.getConn();
		List<Entity> result = new ArrayList<Entity>();
		result.clear();
		
		if (conn != null) {
        	try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                while (rs.next()){
                   int id = rs.getInt(1);
                   String name_db = rs.getString(2);
                   String subject_db = rs.getString(3);
                   String description_db = rs.getString(4);
                   String property_db = rs.getString(5);
                   String relative_db = rs.getString(6);
                   String question_db = rs.getString(7);
                   Entity tmp = new Entity(name_db, subject_db, description_db,
                		   property_db, relative_db, question_db);
                   result.add(tmp);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return result;
	}
	
	public boolean insertAllEntity(String username, List<Entity> browse) {
		if (username == null || username.equals(""))
			return false;
		
		String sql = "insert into entities(name, subject, description, property, relative, question, username) values (?, ?, ?, ?, ?, ?, ?)";
		Connection conn = JDBCUtils.getConn();
		
		if (conn != null) {
        	try {        		
        		for (int i = 0; i < browse.size(); i++) {
        			Entity tmp = browse.get(i);
        			boolean isExist = judgeExist(tmp, username);
        			if (isExist) continue;
        			PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, tmp.getName());
                    ps.setString(2, tmp.getSubject());
                    ps.setString(3, tmp.getDescription());
                    ps.setString(4, tmp.getProperty());
                    ps.setString(5, tmp.getRelative());
                    ps.setString(6, tmp.getQuestion());
                    ps.setString(7, username);
                    ps.executeUpdate();
        		}
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return true;
	}

	public boolean judgeExist(Entity e, String username) {
		if (username == null || username.equals(""))
			return true;
		String sql = "select * from entities where name = ? and subject = ? and description = ? and property = ? and relative = ? and question = ? and username = ?";
        Connection conn = JDBCUtils.getConn();
        
        if (conn != null) {
        	try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, e.getName());
                ps.setString(2, e.getSubject());
                ps.setString(3, e.getDescription());
                ps.setString(4, e.getProperty());
                ps.setString(5, e.getRelative());
                ps.setString(6, e.getQuestion());
                ps.setString(7, username);
                ResultSet rs = ps.executeQuery();

                while (rs.next()){
                   return true;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        return false;
	}
	
}
