package com.jian.utdir.model;

public class PageRankPostingListNode implements Comparable<PageRankPostingListNode> {

	private String docId;
	private double termFreqWeight;
	private double pageRank;
	private int termFreqInCurrDoc;

	public PageRankPostingListNode(String docId) {
		this.docId = docId;
		this.termFreqWeight = 0.0;
		this.pageRank = 0.0;
		this.termFreqInCurrDoc = 0;
	}

	@Override
	public int compareTo(PageRankPostingListNode o) {
		
		if(this.pageRank > o.getPageRank()) {
			return 1;
		} else if(this.pageRank < o.getPageRank()) {
			return -1;
		} else {
			if(this.termFreqWeight >= o.getTermFreqWeight()) {
				return 1;
			} else {
				return -1;
			}
		}
		
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public double getTermFreqWeight() {
		return termFreqWeight;
	}

	public void setTermFreqWeight(double termFreqWeight) {
		this.termFreqWeight = termFreqWeight;
	}

	public double getPageRank() {
		return pageRank;
	}

	public void setPageRank(double pageRank) {
		this.pageRank = pageRank;
	}

	public int getTermFreqInCurrDoc() {
		return termFreqInCurrDoc;
	}

	public void setTermFreqInCurrDoc(int termFreqInCurrDoc) {
		this.termFreqInCurrDoc = termFreqInCurrDoc;
	}

}
