package com.jian.utdir.model;


public class PostingListNode implements Comparable<PostingListNode>{

		private String docId;
		private double termFreqWeight;
		private int termFreqInCurrDoc;

		public PostingListNode(String docId) {
			this.docId = docId;
			this.termFreqInCurrDoc = 0;
			this.termFreqWeight = 0.0;
		}
		
		@Override
		public int compareTo(PostingListNode o) {
			
			if(this.termFreqWeight > o.getTermFreqWeight()) {
				return 1;
			} else if(this.termFreqWeight < o.getTermFreqWeight()){
				return -1;
			} else {
				if(this.termFreqInCurrDoc >= o.getTermFreqInCurrDoc()) {
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

		public int getTermFreqInCurrDoc() {
			return termFreqInCurrDoc;
		}

		public void setTermFreqInCurrDoc(int termFreqInCurrDoc) {
			this.termFreqInCurrDoc = termFreqInCurrDoc;
		}

		
}
