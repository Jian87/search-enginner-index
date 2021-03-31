package com.jian.utdir.ranker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.jian.utdir.model.PageRankNode;

@Component
public class PageRanker {

	public Map<String, PageRankNode> pageGraph;
	public List<Double> perplexity;
	public List<PageRankNode> listNodes;
	public int numberOfNodes;

	public PageRanker() {
		pageGraph = new HashMap<String, PageRankNode>();
		perplexity = new ArrayList<Double>();
		listNodes = new ArrayList<PageRankNode>();
		numberOfNodes = 0;
	}

	public void initGraph(File linkFile) {

		Scanner myReader;
		try {
			myReader = new Scanner(linkFile);
			while (myReader.hasNext()) {

				String currPageId, inlinkId;
				PageRankNode currPageNode, inlinkPageNode;

				String line = myReader.nextLine();

				String[] tokens = line.split(" ");
				currPageId = tokens[0];

				if (!pageGraph.containsKey(currPageId)) {
					currPageNode = new PageRankNode(currPageId);
					pageGraph.put(currPageId, currPageNode);
				} else {
					currPageNode = pageGraph.get(currPageId);
				}

				for (int i = 1; i < tokens.length; i++) {
					inlinkId = tokens[i];
					if (!pageGraph.containsKey(inlinkId)) {
						inlinkPageNode = new PageRankNode(inlinkId);
						pageGraph.put(inlinkId, inlinkPageNode);
					} else {
						inlinkPageNode = pageGraph.get(inlinkId);
					}
					if (!currPageNode.getInLinks().contains(inlinkPageNode))
						currPageNode.getInLinks().add(inlinkPageNode);

					inlinkPageNode.getOutLinks().add(currPageNode);
				}
			}

			myReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initialize() {
		numberOfNodes = pageGraph.size();
		double initValue = 1.0 / numberOfNodes;

		for (PageRankNode pageNode : pageGraph.values()) {
			pageNode.setPageRank(initValue);

			if (pageNode.getOutLinks().size() == 0) {
				listNodes.add(pageNode);
			}
		}
	}

	public void runPageRank() {

		double accPageRank;
		double newPageRank[] = new double[numberOfNodes];
		double d = 0.85;
		int index = 0;

		while (!isConverge()) {
			accPageRank = 0;
			index = 0;

			for (PageRankNode pageNode : listNodes) {
				accPageRank += pageNode.getPageRank();
			}

			for (PageRankNode pageNode : pageGraph.values()) {
				newPageRank[index] = ((1.0 - d) + (d * accPageRank)) / numberOfNodes;
				for (PageRankNode inlistNode : pageNode.getInLinks()) {
					newPageRank[index] += d * inlistNode.getPageRank() / inlistNode.getOutLinks().size();
				}
				index++;
			}

			index = 0;

			for (PageRankNode pageNode : pageGraph.values()) {
				pageNode.setPageRank(newPageRank[index++]);
			}

			perplexity.add(getPerplexity());
		}

	}

	public Map<String, PageRankNode> getFirstKDocs(int K) {
		
		Map<String, PageRankNode> ansMap = new HashMap<String, PageRankNode>();
		
		if (K < pageGraph.size()) {
			K = numberOfNodes;
		}

		List<PageRankNode> helperList = new ArrayList<>(pageGraph.values());
		Collections.sort(helperList, (a, b) -> b.compareTo(a));

		for (int i = 0; i < K; i++) {
			ansMap.put(helperList.get(i).getDocId(), helperList.get(i));
		}

		return ansMap;
	}

	public double getPerplexity() {
		double sum = 0;
		for (PageRankNode pageNode : pageGraph.values()) {
			sum += pageNode.getPageRank() * (Math.log(pageNode.getPageRank()) / Math.log(2));
		}

		return Math.pow(2, -sum);
	}

	public boolean isConverge() {
		if (perplexity.size() < 4)
			return false;
		for (int i = perplexity.size() - 1; i > perplexity.size() - 4; i--) {
			double perplexityOne = perplexity.get(i);
			double perplexityTwo = perplexity.get(i - 1);
			if ((int) perplexityOne % 10 != (int) perplexityTwo % 10 || perplexityOne - perplexityTwo > 1.0)
				return false;
		}
		return true;
	}
}
