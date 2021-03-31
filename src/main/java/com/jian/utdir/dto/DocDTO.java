package com.jian.utdir.dto;

public class DocDTO implements Comparable<DocDTO> {

	public String docId;
	public double weight;
	
	public DocDTO(String docId, double weight) {
		
		this.docId = docId;
		this.weight = weight;
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
