package com.jian.utdir.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostingList {

	private int docFreq;
	private int termFreq;
//	private List<DocNode> docNodes;
	private List<PostingListNode> highTWList;
	private List<PostingListNode> lowTWList;
	private List<PageRankPostingListNode> highPageRankPostingListNodes;
	private List<PageRankPostingListNode> lowPageRankPostingListNodes;

	public PostingList() {
		docFreq = 0;
		termFreq = 0;
//		docNodes = new ArrayList<DocNode>();
		highTWList = new ArrayList<PostingListNode>();
		lowTWList = new ArrayList<PostingListNode>();
		highPageRankPostingListNodes = new ArrayList<PageRankPostingListNode>();
		lowPageRankPostingListNodes = new ArrayList<PageRankPostingListNode>();
	}
	
	public void sortHigh() {
		Collections.sort(highTWList, (a, b) -> b.compareTo(a));
		Collections.sort(highPageRankPostingListNodes, (a, b) -> b.compareTo(a));
	}
	
	public void sortLow() {
		Collections.sort(lowTWList, (a, b) -> b.compareTo(a));
		Collections.sort(lowPageRankPostingListNodes, (a, b) -> b.compareTo(a));
	}

	public int getDocFreq() {
		return docFreq;
	}

	public void setDocFreq(int docFreq) {
		this.docFreq = docFreq;
	}

	public int getTermFreq() {
		return termFreq;
	}

	public void setTermFreq(int termFreq) {
		this.termFreq = termFreq;
	}

//	public List<DocNode> getDocNodes() {
//		return docNodes;
//	}
//
//	public void setDocNodes(List<DocNode> docNodes) {
//		this.docNodes = docNodes;
//	}

	public List<PostingListNode> getHighTWList() {
		return highTWList;
	}

	public void setHighTWList(List<PostingListNode> highTWList) {
		this.highTWList = highTWList;
	}

	public List<PostingListNode> getLowTWList() {
		return lowTWList;
	}

	public void setLowTWList(List<PostingListNode> lowTWList) {
		this.lowTWList = lowTWList;
	}
	
	public List<PageRankPostingListNode> getHighPageRankPostingListNodes() {
		return highPageRankPostingListNodes;
	}

	public void setHighPageRankPostingListNodes(List<PageRankPostingListNode> highPageRankPostingListNodes) {
		this.highPageRankPostingListNodes = highPageRankPostingListNodes;
	}

	public List<PageRankPostingListNode> getLowPageRankPostingListNodes() {
		return lowPageRankPostingListNodes;
	}

	public void setLowPageRankPostingListNodes(List<PageRankPostingListNode> lowPageRankPostingListNodes) {
		this.lowPageRankPostingListNodes = lowPageRankPostingListNodes;
	}

	@Override
	public String toString() {
		return "PostingList [docFreq=" + docFreq + ", termFreq=" + termFreq + "]";
	}

}
