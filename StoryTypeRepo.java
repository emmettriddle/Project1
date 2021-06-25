package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.revature.models.StoryType;
import com.revature.utils.JDBCConnection;

public class StoryTypeRepo implements GenericRepo<StoryType> {
	private Connection conn = JDBCConnection.getConnection();

	@Override
	public StoryType add(StoryType st) {
		String sql = "insert into story_type values (default, ?, ?) returning *;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, st.getName());
			ps.setInt(2, st.getPoints());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				st.setId(rs.getInt("id"));
				return st;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public StoryType getById(Integer id) {
		String sql = "select * from story_type where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return this.make(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public StoryType getByName(String name) {
		String sql = "select * from story_type where sname = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return this.make(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Map<Integer, StoryType> getAll() {
		String sql = "select * from story_type;";
		try {
			Map<Integer, StoryType> map = new HashMap<Integer, StoryType>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				StoryType st = this.make(rs);
				map.put(st.getId(), st);
			}
			
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean update(StoryType st) {
//		String sql = "";
//		try {
//			PreparedStatement ps = conn.prepareStatement(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		return false;
	}

	@Override
	public boolean delete(StoryType st) {
		String sql = "delete from story_type where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, st.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public StoryType make(ResultSet rs) throws SQLException {
		StoryType st = new StoryType();
		st.setId(rs.getInt("id"));
		st.setName(rs.getString("sname"));
		st.setPoints(rs.getInt("points"));
		return st;
	}

}
