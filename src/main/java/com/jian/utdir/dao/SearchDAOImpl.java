package com.jian.utdir.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.utdir.dto.DocDTO;
import com.jian.utdir.dto.SearchDTO;
import com.jian.utdir.model.DocNode;
import com.jian.utdir.model.PostingList;
import com.jian.utdir.model.PostingListNode;
import com.jian.utdir.parser.Parser;

@Service
public class SearchDAOImpl implements SearchDAO {

	@Autowired
	Parser parser;

	@Override
	public List<DocDTO> search(List<String> terms) {

		Map<String, PostingList> termDict = parser.getDictionary().getStemDict();
		Map<String, DocNode> docDict = parser.getDictionary().getStemFileDict();
		int N = docDict.size();
		int numOfQueryTerms = terms.size();

//		System.out.println("search terms: " + terms);

		// length[N]
		Map<String, Double> tw_normalize = new HashMap<String, Double>();

		// weight[N]
		Map<String, Double> weight = new HashMap<String, Double>();

		// record how many query terms show up in a doc
		Map<String, Integer> count = new HashMap<String, Integer>();

		// sort query terms by IDF weight;
		List<SearchDTO> queryDTOs = new ArrayList<>();

		for (String term : terms) {

			if (!termDict.containsKey(term)) {
				queryDTOs.add(new SearchDTO(term, 0.0));
				continue;
			}

			int df = termDict.get(term).getDocFreq();
			double idf_weight = Math.log10((double) N / (double) df);

			queryDTOs.add(new SearchDTO(term, idf_weight));
		}

		Collections.sort(queryDTOs, (a, b) -> b.compareTo(a));
		System.out.println(queryDTOs);
		
		// if there are too many query terms, throw the terms which contain low idf;
		if(numOfQueryTerms >= 4) {
			queryDTOs = queryDTOs.subList(0, (int)(0.75 * numOfQueryTerms));
		}
		
		System.out.println(queryDTOs);

		// check the term one by one from high idf to low idf;
		for (SearchDTO searchDTO : queryDTOs) {

			String term = searchDTO.getTerm();
			double idf_weight = searchDTO.getIdfWeight();
			
			System.out.println(term + " high list size: " + termDict.get(term).getHighTWList().size());

			// at first only consider the high tw docs
			for (PostingListNode postingListNode : termDict.get(term).getHighTWList()) {
				String docId = postingListNode.getDocId();
				double tf_weight = postingListNode.getTermFreqWeight();
				tw_normalize.put(docId, tw_normalize.getOrDefault(docId, 0.0) + tf_weight * tf_weight);
				weight.put(docId, weight.getOrDefault(docId, 0.0) + tf_weight * idf_weight);
				count.put(docId, count.getOrDefault(docId, 0) + 1);
			}
		}

		System.out.println("After high list: " + weight.size());
		
		// if the seach result is too small, check the low tw docs in each term's
		// postting list
		if (weight.size() < 50) {

			for (SearchDTO searchDTO : queryDTOs) {

				String term = searchDTO.getTerm();
				double idf_weight = searchDTO.getIdfWeight();

				for (PostingListNode postingListNode : termDict.get(term).getLowTWList()) {
					String docId = postingListNode.getDocId();
					double tf_weight = postingListNode.getTermFreqWeight();
					tw_normalize.put(docId, tw_normalize.getOrDefault(docId, 0.0) + tf_weight * tf_weight);
					weight.put(docId, weight.getOrDefault(docId, 0.0) + tf_weight * idf_weight);
					count.put(docId, count.getOrDefault(docId, 0) + 1);
				}
			}

		}

		PriorityQueue<DocDTO> pq = new PriorityQueue<DocDTO>((a, b) -> a.compareTo(b));

		for (String docId : weight.keySet()) {

			// each docDTO will have its corresponding similarity
			DocDTO docDTO = new DocDTO(docId, weight.get(docId) / (Math.sqrt(tw_normalize.get(docId))));
			pq.add(docDTO);

			if (pq.size() > 50) {
				pq.poll();
			}
		}

		List<DocDTO> searchResult = new ArrayList<DocDTO>();

		while (!pq.isEmpty()) {
			searchResult.add(0, pq.poll());
		}

		System.out.println(searchResult);
		
		return searchResult;
	}

}
