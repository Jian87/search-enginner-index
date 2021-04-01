package com.jian.utdir.dto;

public class SearchResultDTO {

	private String name;
	private String link;

	public SearchResultDTO(String name, String link) {
		this.name = name;
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "SearchResultDTO [name=" + name + ", link=" + link + "]";
	}

}
