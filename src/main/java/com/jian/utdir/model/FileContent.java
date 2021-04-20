package com.jian.utdir.model;

public class FileContent {
	
	private String link;
	private String id;
	private String title;
	private String content;
	
	public FileContent(String link, String title, String content) {
		this.link = link;
		this.id = "";
		this.title = title;
		this.content = content;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
