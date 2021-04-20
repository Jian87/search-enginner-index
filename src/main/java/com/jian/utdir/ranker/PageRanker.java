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

	public void initGraph(File linkFile, Map<String, String> id2urlMap) {

		Scanner myReader;
		try {
			myReader = new Scanner(linkFile);
			while (myReader.hasNext()) {

				String currPageId, inlinkId;
				PageRankNode currPageNode, inlinkPageNode;

				String line = myReader.nextLine();
                line = line.toLowerCase();

				String[] tokens = line.split("\\s+");
				
				if(tokens.length != 2 || tokens[1].equals("null") || !id2urlMap.containsKey(tokens[0]) || !id2urlMap.containsKey(tokens[1])) {
					continue;
				}
				
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

                    pageGraph.put(inlinkId, inlinkPageNode);
				}

                pageGraph.put(currPageId, currPageNode);
			}

			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("page graph map size: " + pageGraph.size());
	}

	public void initialize() {
		numberOfNodes = pageGraph.size();
		// double initValue = 1.0 / numberOfNodes;

		//System.out.println("init pg rank value: ");
		for (PageRankNode pageNode : pageGraph.values()) {
			// pageNode.setPageRank(initValue);
			pageNode.setPageRank(1.0 / pageNode.getOutLinks().size());
			if (pageNode.getOutLinks().size() == 0) {
				listNodes.add(pageNode);
			}

			//System.out.println(pageNode.getDocId() + ": " + pageNode.getPageRank());

		}
	}

	public void runPageRank() {

		// double accPageRank;
		// double newPageRank[] = new double[numberOfNodes];

		Map<String, Double> newPageRankMap = new HashMap<>();
		double d = 0.85;
		// int index = 0;
		int iteration = 0;
		boolean flag = false;

		while (!flag && iteration < 60) {
			// accPageRank = 0;
			// index = 0;

			// for (PageRankNode pageNode : listNodes) {
			// accPageRank += pageNode.getPageRank();
			// }

			for (PageRankNode pageNode : pageGraph.values()) {
				// newPageRank[index] = ((1.0 - d) + (d * accPageRank)) / (double)numberOfNodes;
				newPageRankMap.put(pageNode.getDocId(), 1.0 - d);
				// newPageRank[index] = 1.0 - d;
				double val = 1.0 - d;
				for (PageRankNode inlistNode : pageNode.getInLinks()) {
					val += d * inlistNode.getPageRank() / (double) inlistNode.getOutLinks().size();
					// newPageRank[index] += val;
				}

				newPageRankMap.put(pageNode.getDocId(), val);

				// index++;
			}

			// index = 0;
			int cunt = 0;

			for (PageRankNode pageNode : pageGraph.values()) {
				String s1 = String.format("%.3f", pageNode.getPageRank()),
						// s2 = String.format("%.3f", newPageRank[index]),
						s3 = String.format("%.3f", newPageRankMap.get(pageNode.getDocId()));
				// pageNode.setPageRank(newPageRank[index++]);

				pageNode.setPageRank(newPageRankMap.get(pageNode.getDocId()));

				if (!s1.equals(s3)) {
					cunt++;
				}
			}

			if (cunt == 0) {
				flag = true;
			}

			iteration++;
		}

		System.out.println("Run " + iteration + " iterations");

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

//	public double getPerplexity() {
//		double sum = 0;
//		for (PageRankNode pageNode : pageGraph.values()) {
//			sum += pageNode.getPageRank() * (Math.log(pageNode.getPageRank()) / Math.log(2));
//		}
//
//		return Math.pow(2, -sum);
//	}

//	public boolean isConverge() {
//		if (perplexity.size() < 4)
//			return false;
//		for (int i = perplexity.size() - 1; i > perplexity.size() - 4; i--) {
//			double perplexityOne = perplexity.get(i);
//			double perplexityTwo = perplexity.get(i - 1);
//			if ((int) perplexityOne % 10 != (int) perplexityTwo % 10 || perplexityOne - perplexityTwo > 1.0)
//				return false;
//		}
//		return true;
//	}
}
