package com.jian.utdir.parser;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.jian.utdir.model.DocNode;

@Component
public class Handler {

	private DocNode docStemNode;
	private Set<String> stops;
	private String fileName;
	
	public Handler() {
		
	}
	
	public Handler(Set<String> stops, String fileName) {
		this.stops = stops;
		this.docStemNode = new DocNode(fileName);
		this.fileName = fileName;
	}

	public void store(String token, String stemResult) {

		if (!stops.contains(token) && !stops.contains(stemResult)) {

			// term frequent
			int cunt = docStemNode.getTermFreqMap().containsKey(stemResult) ? docStemNode.getTermFreqMap().get(stemResult) : 0;
			docStemNode.getTermFreqMap().put(stemResult, cunt + 1);

			if (docStemNode.getMaxFreq() < cunt + 1) {
				docStemNode.setMaxFreq(cunt + 1);
			}
		}

		int stemSize = docStemNode.getSize();
		docStemNode.setSize(stemSize + 1);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public DocNode getDocStemNode() {
		return docStemNode;
	}

	public void setDocStemNode(DocNode docStemNode) {
		this.docStemNode = docStemNode;
	}

	public Set<String> getStops() {
		return stops;
	}

	public void setStops(Set<String> stops) {
		this.stops = stops;
	}

}
