package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.revature.models.Editor;
import com.revature.models.GEJoin;
import com.revature.models.Genre;
import com.revature.utils.JDBCConnection;

public class GEJoinRepo implements GenericRepo<GEJoin> {
	private Connection conn = JDBCConnection.getConnection();

	@Override
	public GEJoin add(GEJoin j) {
		String sql = "insert into genre_editor_join values (default, ?, ?) returning *;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, j.getGenre().getId());
			ps.setInt(2, j.getEditor().getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				j.setId(rs.getInt("id"));
				return j;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public GEJoin getById(Integer id) {
//		String sql = "select * from genre_editor_join where id = ?;";
		String sql = "select gej.id, g.id as g_id, g.name, e.id as e_id, e.first_name, e.last_name, e.username, e.password, e.senior, e.assistant "
				+ "from genre_editor_join gej "
				+ "full join genres g "
				+ "on gej.genre = g.id "
				+ "full join editors e "
				+ "on gej.editor = e.id "
				+ "where gej.id = ?;";
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

	@Override
	public Map<Integer, GEJoin> getAll() {
//		String sql = "select * from genre_editor_join;";
		String sql = "select gej.id, g.id as g_id, g.name, e.id as e_id, e.first_name, e.last_name, e.username, e.password, e.senior, e.assistant "
				+ "from genre_editor_join gej "
				+ "full join genres g "
				+ "on gej.genre = g.id "
				+ "full join editors e "
				+ "on gej.editor = e.id;";
		try {
			Map<Integer, GEJoin> map = new HashMap<Integer, GEJoin>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				GEJoin j = this.make(rs);
				map.put(j.getId(), j);
			}
			
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean update(GEJoin j) {
		String sql = "update genre_editor_join set genre = ?, editor = ? where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, j.getGenre().getId());
			ps.setInt(2, j.getEditor().getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean delete(GEJoin j) {
		String sql = "delete from genre_editor_join where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, j.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public GEJoin make(ResultSet rs) throws SQLException {
		GEJoin j = new GEJoin();
		j.setId(rs.getInt("id"));
		Genre g = new Genre(rs.getInt("g_id"), rs.getString("name"));
		j.setGenre(g);
		Editor e = new Editor(rs.getInt("e_id"), rs.getString("first_name"), rs.getString("last_name"));
		e.setUsername(rs.getString("username"));
		e.setPassword(rs.getString("password"));
		e.setSenior(rs.getBoolean("senior"));
		e.setAssistant(rs.getBoolean("assistant"));
		j.setEditor(e);
		return j;
	}
}
