package com.jian.utdir.model;

import java.util.HashMap;
import java.util.Map;

import com.jian.utdir.parser.Handler;

public class Dictionary {

	private Map<String, PostingList> stemDict;
	private Map<String, FileNode> fileDict;
	private int numberOfDocs;

	public Dictionary() {

		this.stemDict = new HashMap<String, PostingList>();
		this.fileDict = new HashMap<String, FileNode>();
		this.numberOfDocs = 0;
	}

	public void add(Handler handler, double pageRank, FileContent fileContent) {
		
		String link = handler.getFileName();
		String title = fileContent.getTitle();
		String description = fileContent.getContent().length() > 50? fileContent.getContent().substring(0,50): fileContent.getContent();
		// if need the content of website, use code below
		// String description = fileContent.getContent();
		
		
		FileNode fileNode = new FileNode(link, title, description);
		fileDict.put(fileNode.getLink(), fileNode);
		
		this.numberOfDocs += 1;
		updateDictMap(stemDict, handler.getDocStemNode(), pageRank);

	}
	
	public void updateDictMap(Map<String, PostingList> appendTo, DocNode docNode, double pageRank) {
		
		Map<String, Integer> appendFrom = docNode.getTermFreqMap();
		
		for (String term : appendFrom.keySet()) {

			PostingList postingList;
			
			if (appendTo.containsKey(term)) {
				postingList = appendTo.get(term);
				postingList.setDocFreq(postingList.getDocFreq() + 1);
				postingList.setTermFreq(postingList.getTermFreq() + appendFrom.get(term));
			} else {
				postingList = new PostingList();
				postingList.setDocFreq(1);
				postingList.setTermFreq(appendFrom.get(term));
			}

//			postingList.getDocNodes().add(docNode);
			
			PostingListNode postingListNode = new PostingListNode(docNode.getDocId());
			PageRankPostingListNode pageRankPostingListNode = new PageRankPostingListNode(docNode.getDocId());
			
			postingListNode.setTermFreqInCurrDoc(docNode.getTermFreqMap().get(term));
			pageRankPostingListNode.setTermFreqInCurrDoc(docNode.getTermFreqMap().get(term));
			
			double tf_weight = Double.parseDouble(String.format("%.4f", 1.0 + Math.log10(postingListNode.getTermFreqInCurrDoc())));
			
			postingListNode.setTermFreqWeight(tf_weight);
			pageRankPostingListNode.setTermFreqWeight(tf_weight);
			pageRankPostingListNode.setPageRank(pageRank);
			
			postingList.getHighTWList().add(postingListNode);
			postingList.getHighPageRankPostingListNodes().add(pageRankPostingListNode);
			postingList.sortHigh();
						
			if(postingList.getHighTWList().size() > 100) {
				postingList.getLowTWList().add(postingList.getHighTWList().get(100));
				postingList.getHighTWList().remove(100);
				postingList.sortLow();
				
				if(postingList.getLowTWList().size() > 100) {
					postingList.getLowTWList().remove(100);
				}
			}
			
			if(postingList.getHighPageRankPostingListNodes().size() > 100) {
				postingList.getLowPageRankPostingListNodes().add(postingList.getHighPageRankPostingListNodes().get(100));
				postingList.getHighPageRankPostingListNodes().remove(100);
				postingList.sortLow();
				
				if(postingList.getLowPageRankPostingListNodes().size() > 100) {
					postingList.getLowPageRankPostingListNodes().remove(100);
				}
			}
			
			appendTo.put(term, postingList);
		}
	}

	public Map<String, PostingList> getStemDict() {
		return stemDict;
	}

	public void setStemDict(Map<String, PostingList> stemDict) {
		this.stemDict = stemDict;
	}

	public int getNumberOfDocs() {
		return numberOfDocs;
	}

	public void setNumberOfDocs(int numberOfDocs) {
		this.numberOfDocs = numberOfDocs;
	}

	public Map<String, FileNode> getFileDict() {
		return fileDict;
	}

	public void setFileDict(Map<String, FileNode> fileDict) {
		this.fileDict = fileDict;
	}
}
