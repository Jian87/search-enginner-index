package com.jian.utdir.model;

public class FileNode {
	
	private String link;
	private String title;
	private String description;
	
	public FileNode(String link, String title, String description) {
		this.link = link;
		this.title = title;
		this.description = description;
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
