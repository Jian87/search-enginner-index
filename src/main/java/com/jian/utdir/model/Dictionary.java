package com.jian.utdir.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jian.utdir.parser.Handler;
import com.jian.utdir.ranker.PageRanker;

public class Dictionary {
	
	@Autowired
	PageRanker pageRanker;

	private Map<String, PostingList> stemDict;
	private Map<String, DocNode> stemFileDict;

	public Dictionary() {

		this.stemDict = new HashMap<String, PostingList>();
		this.stemFileDict = new HashMap<String, DocNode>();
	}

	public void add(Handler handler) {

		String fileName = handler.getFileName();

		if (!stemFileDict.containsKey(fileName)) {
			stemFileDict.put(fileName, handler.getDocStemNode());
		}
		
		updateDictMap(stemDict, handler.getDocStemNode());

	}
	
	public void updateDictMap(Map<String, PostingList> appendTo, DocNode docNode) {
		
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
			
			double pageRank =  0.0; //pageRanker.pageGraph.get(docNode.getDocId()).getPageRank();
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

	public Map<String, DocNode> getStemFileDict() {
		return stemFileDict;
	}

	public void setStemFileDict(Map<String, DocNode> stemFileDict) {
		this.stemFileDict = stemFileDict;
	}
}
