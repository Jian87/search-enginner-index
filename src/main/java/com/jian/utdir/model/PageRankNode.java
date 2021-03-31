package com.jian.utdir.model;

import java.util.HashSet;
import java.util.Set;

public class PageRankNode implements Comparable<PageRankNode>{

	private String docId;
	private double pageRank;
	private Set<PageRankNode> inLinks;
	private Set<PageRankNode> outLinks;

	public PageRankNode(String docId) {
		this.docId = docId;
		this.pageRank = 0.0;
		inLinks = new HashSet<PageRankNode>();
		outLinks = new HashSet<PageRankNode>();
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public double getPageRank() {
		return pageRank;
	}

	public void setPageRank(double pageRank) {
		this.pageRank = pageRank;
	}

	public Set<PageRankNode> getInLinks() {
		return inLinks;
	}

	public void setInLinks(Set<PageRankNode> inLinks) {
		this.inLinks = inLinks;
	}

	public Set<PageRankNode> getOutLinks() {
		return outLinks;
	}

	public void setOutLinks(Set<PageRankNode> outLinks) {
		this.outLinks = outLinks;
	}

	@Override
	public int compareTo(PageRankNode o) {
		
		if(this.pageRank >= o.getPageRank()) {
			return 1;
		} else {
			return -1;
		}

	}

}
