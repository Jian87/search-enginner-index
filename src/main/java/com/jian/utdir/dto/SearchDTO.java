package com.jian.utdir.dto;

public class SearchDTO implements Comparable<SearchDTO> {

	private String term;
	private double idfWeight;

	public SearchDTO(String term, double idfWeight) {
		this.term = term;
		this.idfWeight = idfWeight;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public double getIdfWeight() {
		return idfWeight;
	}

	public void setIdfWeight(double idfWeight) {
		this.idfWeight = idfWeight;
	}

	@Override
	public int compareTo(SearchDTO o) {

		if (this.idfWeight >= o.idfWeight) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {
		return "SearchDTO [term=" + term + ", idfWeight=" + idfWeight + "]";
	}

}
