package com.jian.utdir.dto;

public class DocDTO implements Comparable<DocDTO> {

	public String docId;
	public double weight;
	public String title;
	public String description;
	
	public DocDTO(String docId, double weight, String title, String description) {
		
		this.docId = docId;
		this.weight = weight;
		this.title = title;
		this.description = description;
	}

	@Override
	public int compareTo(DocDTO o) {
		
		if(this.weight >= o.weight) {
			return 1;
		} else {
			return -1;
		}
	}
	
	public String toString() {
		
		return "[ " + docId + " " + String.format("%.4f", weight) + " ]";
	}

	
}
