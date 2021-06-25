package com.revature.models;

import java.lang.reflect.Type;
import java.sql.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.revature.repositories.AuthorRepo;
import com.revature.repositories.GenreRepo;
import com.revature.repositories.StoryTypeRepo;

public class Story {
	private Integer id;
	private String title;
	private Genre genre;
	private StoryType type;
	private Author author;
	private String description;
	private String tagLine;
	private Date completionDate;
	private Date submissionDate;
	private String approvalStatus;
	private String reason;
	private String request;
	private String response;
	private String receiverName;
	private String requestorName;
	private String draft;
	private Boolean modified;
	private Integer draftApprovalCount;
	// These are null until the proposal is approved by that level
	private Editor assistant;
	private Editor editor;
	private Editor senior;
	
	public Story() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public StoryType getType() {
		return type;
	}

	public void setType(StoryType type) {
		this.type = type;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTagLine() {
		return tagLine;
	}

	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getRequestorName() {
		return requestorName;
	}

	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}

	public String getDraft() {
		return draft;
	}

	public void setDraft(String draft) {
		this.draft = draft;
	}

	public Boolean getModified() {
		return modified;
	}

	public Integer getDraftApprovalCount() {
		return draftApprovalCount;
	}

	public void setDraftApprovalCount(Integer draftApprovalCount) {
		this.draftApprovalCount = draftApprovalCount;
	}

	public void setModified(Boolean modified) {
		this.modified = modified;
	}

	public Editor getAssistant() {
		return assistant;
	}

	public void setAssistant(Editor assistant) {
		this.assistant = assistant;
	}

	public Editor getEditor() {
		return editor;
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

	public Editor getSenior() {
		return senior;
	}

	public void setSenior(Editor senior) {
		this.senior = senior;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approvalStatus == null) ? 0 : approvalStatus.hashCode());
		result = prime * result + ((assistant == null) ? 0 : assistant.hashCode());
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((completionDate == null) ? 0 : completionDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((draft == null) ? 0 : draft.hashCode());
		result = prime * result + ((draftApprovalCount == null) ? 0 : draftApprovalCount.hashCode());
		result = prime * result + ((editor == null) ? 0 : editor.hashCode());
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((receiverName == null) ? 0 : receiverName.hashCode());
		result = prime * result + ((request == null) ? 0 : request.hashCode());
		result = prime * result + ((requestorName == null) ? 0 : requestorName.hashCode());
		result = prime * result + ((response == null) ? 0 : response.hashCode());
		result = prime * result + ((senior == null) ? 0 : senior.hashCode());
		result = prime * result + ((submissionDate == null) ? 0 : submissionDate.hashCode());
		result = prime * result + ((tagLine == null) ? 0 : tagLine.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Story other = (Story) obj;
		if (approvalStatus == null) {
			if (other.approvalStatus != null)
				return false;
		} else if (!approvalStatus.equals(other.approvalStatus))
			return false;
		if (assistant == null) {
			if (other.assistant != null)
				return false;
		} else if (!assistant.equals(other.assistant))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (completionDate == null) {
			if (other.completionDate != null)
				return false;
		} else if (!completionDate.equals(other.completionDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (draft == null) {
			if (other.draft != null)
				return false;
		} else if (!draft.equals(other.draft))
			return false;
		if (draftApprovalCount == null) {
			if (other.draftApprovalCount != null)
				return false;
		} else if (!draftApprovalCount.equals(other.draftApprovalCount))
			return false;
		if (editor == null) {
			if (other.editor != null)
				return false;
		} else if (!editor.equals(other.editor))
			return false;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modified == null) {
			if (other.modified != null)
				return false;
		} else if (!modified.equals(other.modified))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (receiverName == null) {
			if (other.receiverName != null)
				return false;
		} else if (!receiverName.equals(other.receiverName))
			return false;
		if (request == null) {
			if (other.request != null)
				return false;
		} else if (!request.equals(other.request))
			return false;
		if (requestorName == null) {
			if (other.requestorName != null)
				return false;
		} else if (!requestorName.equals(other.requestorName))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (senior == null) {
			if (other.senior != null)
				return false;
		} else if (!senior.equals(other.senior))
			return false;
		if (submissionDate == null) {
			if (other.submissionDate != null)
				return false;
		} else if (!submissionDate.equals(other.submissionDate))
			return false;
		if (tagLine == null) {
			if (other.tagLine != null)
				return false;
		} else if (!tagLine.equals(other.tagLine))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Story [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", genre=");
		builder.append(genre);
		builder.append(", type=");
		builder.append(type);
		builder.append(", author=");
		builder.append(author);
		builder.append(", description=");
		builder.append(description);
		builder.append(", tagLine=");
		builder.append(tagLine);
		builder.append(", completionDate=");
		builder.append(completionDate);
		builder.append(", submissionDate=");
		builder.append(submissionDate);
		builder.append(", approvalStatus=");
		builder.append(approvalStatus);
		builder.append(", reason=");
		builder.append(reason);
		builder.append(", request=");
		builder.append(request);
		builder.append(", response=");
		builder.append(response);
		builder.append(", receiverName=");
		builder.append(receiverName);
		builder.append(", requestorName=");
		builder.append(requestorName);
		builder.append(", draft=");
		builder.append(draft);
		builder.append(", modified=");
		builder.append(modified);
		builder.append(", draftApprovalCount=");
		builder.append(draftApprovalCount);
		builder.append(", assistant=");
		builder.append(assistant);
		builder.append(", editor=");
		builder.append(editor);
		builder.append(", senior=");
		builder.append(senior);
		builder.append("]");
		return builder.toString();
	}



	public static class Deserializer implements JsonDeserializer<Story> {
		@Override
		public Story deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			Story story = new Story();
			JsonObject jo = json.getAsJsonObject();
			System.out.println(jo);
			if (jo.has("author")) {
				// TODO: move this to AuthorServices
				story.setAuthor(context.deserialize(jo.get("author"), Author.class));
			}
			if (jo.has("approvalStatus")) {
				story.setApprovalStatus(context.deserialize(jo.get("approvalStatus"), String.class));
			}
			if (jo.has("reason")) {
				story.setReason(context.deserialize(jo.get("reason"), String.class));
			}
			if (jo.has("id")) {
				story.setId(context.deserialize(jo.get("id"), Integer.class));
			}
			story.setTitle(context.deserialize(jo.get("title"), String.class));
			// TODO: move this to GenreServices!!!
			JsonElement gElem = jo.get("genre");
			if (gElem.isJsonObject()) {
				story.setGenre(context.deserialize(gElem, Genre.class));
			} else {
				Genre g = new GenreRepo().getByName(context.deserialize(jo.get("genre"), String.class));
				story.setGenre(g);
			}
			// TODO: move this to StoryTypeServices!!!
			JsonElement tElem = jo.get("type");
			if (tElem.isJsonObject()) {
				story.setType(context.deserialize(tElem, StoryType.class));
			} else {
				StoryType t = new StoryTypeRepo().getByName(context.deserialize(jo.get("type"), String.class));
				story.setType(t);
			}
			story.setDescription(context.deserialize(jo.get("description"), String.class));
			story.setTagLine(context.deserialize(jo.get("tagLine"), String.class));
			story.setCompletionDate(context.deserialize(jo.get("completionDate"), Date.class));
			story.setSubmissionDate(context.deserialize(jo.get("submissionDate"), Date.class));
			story.setRequest(context.deserialize(jo.get("request"), String.class));
			story.setResponse(context.deserialize(jo.get("response"), String.class));
			story.setReceiverName(context.deserialize(jo.get("receiverName"), String.class));
			story.setRequestorName(context.deserialize(jo.get("requestorName"),	String.class));
			story.setDraft(context.deserialize(jo.get("draft"), String.class));
			story.setModified(context.deserialize(jo.get("modified"), Boolean.class));
			story.setDraftApprovalCount(context.deserialize(jo.get("draftApprovalCount"), Integer.class));
			story.setAssistant(context.deserialize(jo.get("assistant"), Editor.class));
			story.setEditor(context.deserialize(jo.get("editor"), Editor.class));
			story.setSenior(context.deserialize(jo.get("senior"), Editor.class));
			return story;
		}
	}
}
