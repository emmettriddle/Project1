package com.revature.servlets;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.google.gson.*;
import com.revature.models.*;
import com.revature.repositories.*;
import com.revature.utils.*;

//import dev.riddle.dao.AuthorDAO;
//import dev.riddle.dao.GenreDAO;
//import dev.riddle.dao.StoryTypeDAO;

public class FrontControllerServlet extends HttpServlet {
	class LoginInfo {
		public String username;
		public String password;
	}

//	class StoryInfo {
//		public String title;
//		public String genre;
//		public String type;
//		public String description;
//		public String tagline;
//		public Date date;
//	}

	public FrontControllerServlet() {
		// Utils.loadEntries();
	}

	private Gson gson = new Gson();
	public static HttpSession session;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Story.class, new Story.Deserializer());
//		gsonBuilder.registerTypeAdapter(Author.class, new Author.Deserializer());
		gsonBuilder.setDateFormat("yyyy-MM-dd");
		this.gson = gsonBuilder.create();

		String uri = request.getRequestURI();
		System.out.println(uri);
		String json = "";

		response.setHeader("Access-Control-Allow-Origin", "*"); // Needed to avoid CORS violations
		response.setHeader("Content-Type", "application/json"); // Needed to enable json data to be passed between front
																// and back end

		session = request.getSession();

		uri = uri.substring("/Project_1/controller/".length());
		switch (uri) {
		case "getStoryFromSession": {
//		JsonObject jo = (JsonObject)session.getAttribute("story");
//		response.getWriter().append(jo.toString());
			// TEMP CODE TO BE REMOVED WHE CORS ERROR IS RESOLVED
			Story s = new Story();
			s.setApprovalStatus("pending");
			s.setCompletionDate(new Date(System.currentTimeMillis()));
			s.setAuthor(new AuthorRepo().getById(1));
			s.setDescription("Testing Testing");
			s.setDraft("abcdefghijklmnop");
			s.setDraftApprovalCount(5);
			s.setGenre(new GenreRepo().getById(3));
			s.setId(1);
			s.setReason("because i said so");
			s.setTagLine("test");
			s.setTitle("TEST");
			s.setType(new StoryTypeRepo().getById(1));
			s.setModified(true);
			json = this.gson.toJson(s);
			System.out.println(json);
			response.getWriter().append(json);
			// System.out.println(jo);
			break;
		}

//		switch (uri) {
//			case "sign_up_author": {
//				System.out.println("Received author sign up!");
//				Author a = this.gson.fromJson(request.getReader(), Author.class);
//				if (a != null) {
//					a = new AuthorRepo().add(a);
//					System.out.println("Created new Author " + a + " and logged in!");
//					session.setAttribute("logged_in", a);
//					// TODO: change this to "author_main.html" when it exits!!!
//					response.getWriter().append("story_proposal_form.html");
//				} else {
//					System.out.println("Failed to create new Author account!");
//				}
//				break;
//			}
//			// TODO: can editor login and author login be combined into the same thing? would require that login info across the two tables be unique
		case "editor_login": {
			System.out.println("Recieved editor_login!");
			LoginInfo info = this.gson.fromJson(request.getReader(), LoginInfo.class);
			Editor e = new EditorRepo().getByUsernameAndPassword(info.username, info.password);
			if (e != null) {
				System.out.println("Editor " + e.getFirstName() + " has logged in!");
				session.setAttribute("logged_in", e);
				response.getWriter().append("editor.html");
				response.getWriter().append("editor_story_list.html");
			} else {
				System.out.println(
						"Failed to login with credentials: username=" + info.username + " password=" + info.password);
			}
			break;
		}
		case "author_login": {
			System.out.println("Received author_login!");
			LoginInfo info = this.gson.fromJson(request.getReader(), LoginInfo.class);
			Author a = new AuthorRepo().getByUsernameAndPassword(info.username, info.password);
			List<Story> s = new StoryRepo().getAllByAuthor(a.getId());
			if (a != null) {
				System.out.println("Author " + a.getFirstName() + " has logged in!");
				session.setAttribute("logged_in", a);
				session.setAttribute("story", s);
				// response.getWriter().append("story_proposal_form.html");
				response.getWriter().append("author_main.html");
			} else {
				System.out.println(
						"Failed to login with credentials: username=" + info.username + " password=" + info.password);
			}
			break;
		}

		case "logout": {
			String pageURL = "";
			Object loggedIn = session.getAttribute("logged_in");
			if (loggedIn instanceof Author)
				pageURL = "index.html";
			if (loggedIn instanceof Editor)
				pageURL = "login_editors.html";
			System.out.println("Logging out!");
			response.getWriter().append(pageURL);
			session.invalidate();
			break;
		}

		case "get_story_types": {
			List<StoryType> types = new ArrayList<StoryType>(new StoryTypeRepo().getAll().values());
			List<Genre> genres = new ArrayList<Genre>(new GenreRepo().getAll().values());
			Author a = (Author) session.getAttribute("logged_in");
			String[] jsons = new String[] { this.gson.toJson(types), this.gson.toJson(genres), this.gson.toJson(a) };
			json = gson.toJson(jsons);
			response.getWriter().append(json);
			break;
		}
		case "submit_story_form": {
			Story story = this.gson.fromJson(request.getReader(), Story.class);
			Author a = (Author) session.getAttribute("logged_in");
			System.out.println(a);
			System.out.println(story);
			
			if (a.getPoints() < story.getType().getPoints()) {
				story.setApprovalStatus("waiting");
			} else {
				story.setApprovalStatus("submitted");
				a.setPoints(a.getPoints() - story.getType().getPoints());
				new AuthorRepo().update(a);
			}
			story.setAuthor(a);
			story.setModified(false);
			story.setDraftApprovalCount(0);
			story = new StoryRepo().add(story);
			System.out.println(story);
			break;
		}
		case "get_proposals": {
			Object logged_in = session.getAttribute("logged_in");
			if (logged_in instanceof Author) {
				Author a = (Author) logged_in;
				List<Story> stories = new StoryRepo().getAllByAuthor(a.getId());
				json = "author|" + this.gson.toJson(stories);
				response.getWriter().append(json);
			} else if (logged_in instanceof Editor) {
				Editor e = (Editor) session.getAttribute("logged_in");
				Set<Genre> genres = Utils.getGenres(e);
				List<Story> stories = new ArrayList<Story>();

				for (Genre g : genres) {
					if (e.getSenior()) {
						stories.addAll(new StoryRepo().getAllByGenreAndStatus(g, "approved_editor"));
					} else if (e.getAssistant()) {
						stories.addAll(new StoryRepo().getAllByGenreAndStatus(g, "submitted"));
					} else {
						String status = "approved_assistant";
						if (g.getName().equals("Sci-fi")) {
							Genre fantasy = new GenreRepo().getByName("Fantasy");
							stories.addAll(new StoryRepo().getAllByGenreAndStatus(fantasy, status));
						} else if (g.getName().equals("Fantasy")) {
							Genre horror = new GenreRepo().getByName("Horror");
							stories.addAll(new StoryRepo().getAllByGenreAndStatus(horror, status));
						} else if (g.getName().equals("Horror")) {
							Genre scifi = new GenreRepo().getByName("Sci-fi");
							stories.addAll(new StoryRepo().getAllByGenreAndStatus(scifi, status));
						}
					}
				}

				String flag = "general|";
				if (e.getAssistant())
					flag = "assistant|";
				else if (e.getSenior())
					flag = "senior|";
				json = flag + this.gson.toJson(stories);

				response.getWriter().append(json);
			}
			break;
		}
		case "save_story_to_session": {
			JsonElement root = JsonParser.parseReader(request.getReader());
			session.setAttribute("story", root.getAsJsonObject());
			response.getWriter().append("saved");
			break;
		}
		case "get_story_from_session": {
			
			List<Story> sj = (List<Story>) session.getAttribute("story");
			System.out.println(sj);
			response.getWriter().append(gson.toJson(sj));
			
//			// JsonObject sj = (JsonObject) session.getAttribute("stories");
//			// String str = "";
//			Object logged_in = session.getAttribute("logged_in");
//			Author a = (Author) logged_in;
//			if (a == null) {
//				System.out.println("Author is null");
//			}
//			List<Story> stories = new StoryRepo().getByASession(a.getUsername(), a.getPassword());
//			String s = this.gson.toJson(stories);
////				if (logged_in instanceof Author) {
////				str = "author|";
////		} else {
////					str = "editor|";
////			}
//
//			response.getWriter().append(s);
			break;
		}

		case "approve_story": {
			Editor e = (Editor) session.getAttribute("logged_in");
			Story s = this.gson.fromJson(request.getReader(), Story.class);
			String status = s.getApprovalStatus();
			switch (status) {
			case "submitted":
				s.setApprovalStatus("approved_assistant");
				s.setAssistant(e);
				break;
			case "approved_assistant":
				s.setApprovalStatus("approved_editor");
				s.setEditor(e);
				break;
			case "approved_editor":
				s.setApprovalStatus("approved_senior");
				s.setSenior(e);
				break;
			case "approved_senior":
				s.setApprovalStatus("draft_approved");
				break;
			default:
				break;
			}
			new StoryRepo().update(s);

			break;
		}
		case "deny_story": {
			Object logged_in = session.getAttribute("logged_in");
			Story s = this.gson.fromJson(request.getReader(), Story.class);
			if (logged_in instanceof Author && s.getModified()) {
				new StoryRepo().delete(s);
				Author a = (Author) logged_in;
				a.setPoints(a.getPoints() + s.getType().getPoints());
			} else
				new StoryRepo().update(s);
			break;
		}
		case "request_info": {
			String[] strs = this.gson.fromJson(request.getReader(), String[].class);
			Story s = this.gson.fromJson(strs[0], Story.class);
			String receiverName = this.gson.fromJson(strs[1], String.class);
			s.setReceiverName(receiverName);
			new StoryRepo().update(s);
			System.out.println("Requesting info!!! " + s);
			break;
		}
		case "get_requests": {
			Object logged_in = session.getAttribute("logged_in");
			String[] receiverName = new String[2];

			if (logged_in instanceof Editor) {
				Editor e = (Editor) logged_in;
				receiverName[0] = e.getFirstName();
				receiverName[1] = e.getLastName();
			} else if (logged_in instanceof Author) {
				Author a = (Author) logged_in;
				receiverName[0] = a.getFirstName();
				receiverName[1] = a.getLastName();
			}

			List<Story> stories = new StoryRepo().getAllByReceiverName(receiverName[0], receiverName[1]);
			if (logged_in instanceof Author) {
				json = "author|" + this.gson.toJson(stories);
			} else if (logged_in instanceof Editor) {
				json = "editor|" + this.gson.toJson(stories);
			}
			response.getWriter().append(json);
			break;
		}
		case "get_draft_requests": {
			Editor e = (Editor) session.getAttribute("logged_in");

//				List<Story> stories = new StoryRepo().getAllWithDrafts();
			List<Story> stories = new StoryRepo().getAllWithDraftsForEditor(e);
			json = this.gson.toJson(stories);
			response.getWriter().append(json);
			break;
		}
		case "save_response": {
			Story s = this.gson.fromJson(request.getReader(), Story.class);
			new StoryRepo().update(s);
			break;
		}
		case "get_editor_main_labels": {
			String[] counts = new String[4];

			Editor e = (Editor) session.getAttribute("logged_in");
			if (e == null)
				System.out.println("get_editor_main_labels: editor null!!!!");
			Set<Genre> genres = Utils.getGenres(e);
			List<Story> stories = new ArrayList<Story>();

			for (Genre g : genres) {
				if (e.getSenior()) {
					stories.addAll(new StoryRepo().getAllByGenreAndStatus(g, "approved_editor"));
				} else if (e.getAssistant()) {
					stories.addAll(new StoryRepo().getAllByGenreAndStatus(g, "submitted"));
				} else {
					String status = "approved_assistant";
					if (g.getName().equals("Sci-fi")) {
						Genre fantasy = new GenreRepo().getByName("Fantasy");
						stories.addAll(new StoryRepo().getAllByGenreAndStatus(fantasy, status));
					} else if (g.getName().equals("Fantasy")) {
						Genre horror = new GenreRepo().getByName("Horror");
						stories.addAll(new StoryRepo().getAllByGenreAndStatus(horror, status));
					} else if (g.getName().equals("Horror")) {
						Genre scifi = new GenreRepo().getByName("Sci-fi");
						stories.addAll(new StoryRepo().getAllByGenreAndStatus(scifi, status));
					}
				}
			}

			List<Story> infoReqs = new StoryRepo().getAllByReceiverName(e.getFirstName(), e.getLastName());
			List<Story> draftReqs = new StoryRepo().getAllWithDraftsForEditor(e);

			counts[0] = e.getFirstName() + " " + e.getLastName();
			counts[1] = "" + stories.size();
			counts[2] = "" + infoReqs.size();
			counts[3] = "" + draftReqs.size();

			response.getWriter().append(this.gson.toJson(counts));

			break;
		}
		case "get_author_main_labels": {
			String[] counts = new String[4];
			Author a = (Author) session.getAttribute("logged_in");
			if (a == null)
				System.out.println("get_author_main_labels: author null!!!!");
			List<Story> stories = new StoryRepo().getAllByAuthor(a.getId());
			List<Story> infoReqs = new StoryRepo().getAllByReceiverName(a.getFirstName(), a.getLastName());
			counts[0] = a.getFirstName() + " " + a.getLastName();
			counts[1] = "" + stories.size();
			counts[2] = "" + infoReqs.size();
			counts[3] = "";

			response.getWriter().append(this.gson.toJson(counts));
			break;
		}
		case "update_details": {
			Story s = this.gson.fromJson(request.getReader(), Story.class);
			new StoryRepo().update(s);
			// notify author somehow
			break;
		}
		case "submit_draft": {
			Story s = this.gson.fromJson(request.getReader(), Story.class);
			new StoryRepo().update(s);
			break;
		}
		case "approve_draft": {
			Story s = this.gson.fromJson(request.getReader(), Story.class);
			String type = s.getType().getName();
			System.out.println("Approving draft for type " + type);
			switch (type) {
			case "Novel":
			case "Novella": {
				Set<Editor> editors = Utils.getEditors(s.getGenre());
				Integer count = s.getDraftApprovalCount();
				count++;
				s.setDraftApprovalCount(count);
				float avg = (float) count / (float) editors.size();
				if (avg > 0.5f) {
					s.setApprovalStatus("Approved");
					Author a = s.getAuthor();
					a.setPoints(a.getPoints() + s.getType().getPoints());
					new AuthorRepo().update(a);
				}
				new StoryRepo().update(s);
				break;
			}
			case "Short Story": {
				System.out.println("Short Story");
				Integer count = s.getDraftApprovalCount();
				count++;
				s.setDraftApprovalCount(count);
				if (count == 2) {
					s.setApprovalStatus("Approved");
					Author a = s.getAuthor();
					a.setPoints(a.getPoints() + s.getType().getPoints());
					new AuthorRepo().update(a);
				}
				new StoryRepo().update(s);
				break;
			}
			case "Article": {
				s.setApprovalStatus("Approved");
				s.setDraftApprovalCount(1);
				Author a = s.getAuthor();
				a.setPoints(a.getPoints() + s.getType().getPoints());
				new AuthorRepo().update(a);
				new StoryRepo().update(s);
				break;
			}
			}
			break;
		}
		case "deny_draft": {
			break;
		}
		case "request_draft_change": {
			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request, response);
	}
}
