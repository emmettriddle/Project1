package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.revature.models.Author;
import com.revature.models.Editor;
import com.revature.models.Genre;
import com.revature.models.Story;
import com.revature.models.StoryType;
import com.revature.utils.JDBCConnection;
import com.revature.utils.Utils;
import com.revature.repositories.*;

public class StoryRepo implements GenericRepo<Story> {
	private Connection conn = JDBCConnection.getConnection();
	
	@Override
	public Story add(Story s) {
		String sql = "insert into story values (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning *";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, s.getTitle());
			ps.setInt(2, s.getGenre().getId());
			ps.setInt(3, s.getType().getId());
			ps.setInt(4, s.getAuthor().getId());
			ps.setString(5, s.getDescription());
			ps.setString(6, s.getTagLine());
			ps.setDate(7, s.getCompletionDate());
			ps.setString(8, s.getApprovalStatus());
			ps.setString(9, s.getReason());
			ps.setDate(10, s.getSubmissionDate());
			if (s.getAssistant() == null) ps.setNull(11, java.sql.Types.INTEGER);
			else ps.setInt(11, s.getAssistant().getId());
			if (s.getEditor() == null) ps.setNull(12, java.sql.Types.INTEGER);
			else ps.setInt(12, s.getEditor().getId());
			if (s.getSenior() == null) ps.setNull(13, java.sql.Types.INTEGER);
			else ps.setInt(13, s.getSenior().getId());
			ps.setString(14, s.getRequest());
			ps.setString(15, s.getResponse());
			ps.setString(16, s.getReceiverName());
			ps.setString(17, s.getRequestorName());
			ps.setString(18, s.getDraft());
			ps.setBoolean(19, s.getModified());
			ps.setInt(20, s.getDraftApprovalCount());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				s.setId(rs.getInt("id"));
				return s;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Story> getByASession(String username, String password) {
		String sql = "select * "
				+ "from story s "
				+ "left join genre g on s.g_id=g.id "
				+ "left join story_type st on s.st_id = st.id "
				+ "left join author a on s.a_id=a.id "
				+ "where a.username = ? and a.password = ?; ";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			AuthorRepo a = new AuthorRepo();
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				List <Story> list = new ArrayList<Story>();
				 list.add(this.make(rs)); 
				 return list;
				}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Story getById(Integer id) {
		String sql = "select * from story where id = ?;";
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
	
	public List<Story> getAllByReceiverName(String firstName, String lastName) {
		String name = firstName + " " + lastName;
		List<Story> list = new ArrayList<Story>();
		String sql = "select * from story where receiver_name = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// TODO: move this check to Services
				Story s = this.make(rs);
				if (s.getResponse() == null || s.getResponse().equals("")) list.add(s);
//				list.add(this.make(rs));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Story> getAllByAuthor(Integer a_id) {
		String sql = "select * from story where a_id = ?;";
		try {
			List<Story> list = new ArrayList<Story>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, a_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(this.make(rs));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Story> getAllByGenre(Genre g) {
		String sql = "select * from story where genre = ?;";
		try {
			List<Story> list = new ArrayList<Story>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, g.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(this.make(rs));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Story> getAllByStatus(String status) {
		String sql = "select * from story where approval = ?;";
		try {
			List<Story> list = new ArrayList<Story>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(this.make(rs));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Story> getAllByGenreAndStatus(Genre g, String status) {
		String sql = "select * from story where genre = ? and approval = ?;";
		try {
			List<Story> list = new ArrayList<Story>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, g.getId());
			ps.setString(2, status);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(this.make(rs));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Story> getAllWithDrafts() {
		String sql = "select * from story where draft notnull;";
		try {
			List<Story> list = new ArrayList<Story>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(this.make(rs));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Story> getAllWithDraftsForEditor(Editor e) {
		Set<Genre> genres = Utils.getGenres(e);
		List<Story> list = new ArrayList<Story>();
		String sql;
		
		if (e.getSenior()) {
			sql = "select * from story where genre = ? and draft notnull;";
		} else if (e.getAssistant()) {
			sql = "select * from story where genre = ? and story_type in (1, 2) and draft notnull;";
		} else {
			sql = "select * from story where genre = ? and story_type in (1, 2, 3) and draft notnull";
		}
		
		for (Genre g : genres) {
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, g.getId());
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					list.add(this.make(rs));
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		if (!e.getSenior() && !e.getAssistant()) {
			sql = "select * from story where editor = ? and draft notnull;";
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, e.getId());
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					list.add(this.make(rs));
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		}
		
		return list;
	}
		
//		String sql = "select * from stories where";
//		if (e.getSenior()) {
//			sql += " senior = ?;";
//		} else if (e.getAssistant() && t.getName().equals(t)) {
//			sql += " assistant = ?;";
//		} else {
//			sql += " editor = ?;";
//		}
//		try {
//			List<Story> list = new ArrayList<Story>();
//			PreparedStatement ps = conn.prepareStatement(sql);
//			ps.setInt(1, e.getId());
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				list.add(this.make(rs));
//			}
//			
//			return list;
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}

	@Override
	public Map<Integer, Story> getAll() {
		String sql = "select * from story;";
		try {
			Map<Integer, Story> map = new HashMap<Integer, Story>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Story s = this.make(rs);
				map.put(s.getId(), s);
			}
			
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean update(Story s) {
		String sql = "update story set title = ?, description = ?, tag = ?, completion = ?, approval = ?, reason = ?, assistant = ?, editor = ?, senior = ?, request = ?, response = ?, receiver_name = ?, requestor_name = ?, draft = ?, modified = ?, draft_approval_count = ? where id = ? returning *;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, s.getTitle());
			ps.setString(2, s.getDescription());
			ps.setString(3, s.getTagLine());
			ps.setDate(4, s.getCompletionDate());
			ps.setString(5, s.getApprovalStatus());
			ps.setString(6, s.getReason());
			if (s.getAssistant() == null) ps.setNull(7, java.sql.Types.INTEGER);
			else ps.setInt(7, s.getAssistant().getId());
			if (s.getEditor() == null) ps.setNull(8, java.sql.Types.INTEGER);
			else ps.setInt(8, s.getEditor().getId());
			if (s.getSenior() == null) ps.setNull(9, java.sql.Types.INTEGER);
			else ps.setInt(9, s.getSenior().getId());
			ps.setString(10, s.getRequest());
			ps.setString(11, s.getResponse());
			ps.setString(12, s.getReceiverName());
			ps.setString(13, s.getRequestorName());
			ps.setString(14, s.getDraft());
			ps.setBoolean(15, s.getModified());
			ps.setInt(16, s.getDraftApprovalCount());
			ps.setInt(17, s.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean delete(Story s) {
		String sql = "delete from story where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, s.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public Story make(ResultSet rs) throws SQLException {
		Story s = new Story();
		s.setId(rs.getInt("id"));
		s.setTitle(rs.getString("title"));
		Genre g = new GenreRepo().getById(rs.getInt("g_id"));
		s.setGenre(g);
		StoryType st = new StoryTypeRepo().getById(rs.getInt("st_id"));
		s.setType(st);
		Author a = new AuthorRepo().getById(rs.getInt("a_id"));
		s.setAuthor(a);
		s.setDescription(rs.getString("description"));
		s.setTagLine(rs.getString("tag"));
		s.setCompletionDate(rs.getDate("completion"));
		s.setApprovalStatus(rs.getString("approval"));
		s.setReason(rs.getString("reason"));
		//s.setSubmissionDate(rs.getDate("submission_date"));
//		Editor assistant = new EditorRepo().getById(rs.getInt("assistant"));
//		s.setAssistant(assistant);
//		Editor editor = new EditorRepo().getById(rs.getInt("editor"));
//		s.setEditor(editor);
//		Editor senior = new EditorRepo().getById(rs.getInt("senior"));
//		s.setSenior(senior);
//		s.setRequest(rs.getString("request"));
//		s.setResponse(rs.getString("response"));
//		s.setReceiverName(rs.getString("receiver_name"));
//		s.setRequestorName(rs.getString("requestor_name"));
//		s.setDraft(rs.getString("draft"));
//		s.setModified(rs.getBoolean("modified"));
//		s.setDraftApprovalCount(rs.getInt("draft_approval_count"));
		return s;
	}

}
