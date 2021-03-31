package com.jian.utdir.model;

import java.util.HashMap;
import java.util.Map;

public class DocNode implements Comparable<DocNode> {

	private String docId;
	private int maxFreq;
	private int size;
	private double termFreqWeight;
	private Map<String, Integer> termFreqMap;

	public DocNode(String docId) {

		this.docId = docId;
		this.termFreqMap = new HashMap<String, Integer>();
		this.size = 0;
		this.maxFreq = 0;
		this.termFreqWeight = 0.0;
	}
	
	@Override
	public int compareTo(DocNode o) {
		
		if(this.termFreqWeight >= o.getTermFreqWeight()) {
			return 1;
		} else {
			return -1;
		}
		
	}


	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public int getMaxFreq() {
		return maxFreq;
	}

	public void setMaxFreq(int maxFreq) {
		this.maxFreq = maxFreq;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Map<String, Integer> getTermFreqMap() {
		return termFreqMap;
	}

	public void setTermFreqMap(Map<String, Integer> termFreqMap) {
		this.termFreqMap = termFreqMap;
	}

	public double getTermFreqWeight() {
		return termFreqWeight;
	}

	public void setTermFreqWeight(double termFreqWeight) {
		this.termFreqWeight = termFreqWeight;
	}
}
