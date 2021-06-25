package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.revature.models.Editor;
import com.revature.models.Genre;
import com.revature.utils.JDBCConnection;

public class GenreRepo implements GenericRepo<Genre> {
	private Connection conn = JDBCConnection.getConnection();
	
	@Override
	public Genre add(Genre g) {
		String sql = "insert into genre values (default, ?) returning *;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, g.getName());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				g.setId(rs.getInt("id"));
				return g;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Genre getById(Integer id) {
		String sql = "select * from genre where id = ?;";
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
	
	public Genre getByName(String name) {
		String sql = "select * from genre where genre = ?;";
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
	public Map<Integer, Genre> getAll() {
		String sql = "select * from genre;";
		try {
			Map<Integer, Genre> map = new HashMap<Integer, Genre>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Genre g = this.make(rs);
				map.put(g.getId(), g);
			}
			
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean update(Genre g) {
		return false;
	}

	@Override
	public boolean delete(Genre g) {
		String sql = "delete from genre where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, g.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public Genre make(ResultSet rs) throws SQLException {
		Genre g = new Genre();
		g.setId(rs.getInt("id"));
		g.setName(rs.getString("genre"));
		return g;
	}

}
