package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.revature.models.Author;
import com.revature.utils.JDBCConnection;

public class AuthorRepo implements GenericRepo<Author> {
	private Connection conn = JDBCConnection.getConnection();
	
	@Override
	public Author add(Author a) {
		String sql = "insert into author values (default, ?, ?, ?, ?, ?, ?) returning *;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, a.getFirstName());
			ps.setString(2, a.getLastName());
			ps.setString(3, a.getBio());
			ps.setInt(4, a.getPoints());
			ps.setString(5, a.getUsername());
			ps.setString(6, a.getPassword());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				a.setId(rs.getInt("id"));
				return a;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Author getById(Integer id) {
		String sql = "select * from author where id = ?;";
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
	
	public Author getByUsernameAndPassword(String username, String password) {
		String sql = "select * from author where username = ? and password = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return this.make(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Author getByName(String firstName, String lastName) {
		String sql = "select * from author where fname = ? and lname = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return this.make(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Map<Integer, Author> getAll() {
		String sql = "select * from author;";
		try {
			Map<Integer, Author> map = new HashMap<Integer, Author>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Author a = make(rs);
				map.put(a.getId(), a);
			}
			
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean update(Author a) {
		String sql = "update author set bio = ?, points = ? where id = ? returning *;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, a.getBio());
			ps.setInt(2, a.getPoints());
			ps.setInt(3, a.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean delete(Author a) {
		String sql = "delete from author where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, a.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public Author make(ResultSet rs) throws SQLException {
		Author a = new Author();
		a.setId(rs.getInt("id"));
		a.setFirstName(rs.getString("fname"));
		a.setLastName(rs.getString("lname"));
		a.setBio(rs.getString("bio"));
		a.setPoints(rs.getInt("points"));
		a.setUsername(rs.getString("username"));
		a.setPassword(rs.getString("password"));
		return a;
	}
}
