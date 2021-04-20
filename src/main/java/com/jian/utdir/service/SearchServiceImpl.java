package com.jian.utdir.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.utdir.dao.SearchDAOImpl;
import com.jian.utdir.dto.DocDTO;
import com.jian.utdir.dto.SearchResultDTO;
import com.jian.utdir.parser.Parser;
import com.jian.utdir.parser.Tokenization;
import com.jian.utdir.stem.MyStemmer;

@Service
public class SearchServiceImpl {

	@Autowired
	Parser parser;

	@Autowired
	Tokenization tokenization;

	@Autowired
	MyStemmer myStemmer;

	@Autowired
	SearchDAOImpl searchDAO;

	public List<List<SearchResultDTO>> search(String inputContent) {

		if (inputContent == null || inputContent.isEmpty()) {
			return null;
		}

		List<List<DocDTO>> docDTOs = searchDAO.search(getQueryTerms(inputContent));
		List<List<SearchResultDTO>> searchResults = new ArrayList<>();
		
		for(List<DocDTO> docDTO: docDTOs) {
			List<SearchResultDTO> searchResult = new ArrayList<>();
			for (DocDTO doc : docDTO) {
				SearchResultDTO searchResultDTO = new SearchResultDTO(doc.docId, doc.title, doc.description);
				searchResult.add(searchResultDTO);
			}
			searchResults.add(searchResult);
		}
		

		return searchResults;
	}

	public List<String> getQueryTerms(String input) {

		Set<String> stops = parser.getStops();

		List<String> queryTerms = new ArrayList<String>();

		input = tokenization.transformString(input);

		String[] words = input.split(" ");

		for (String word : words) {

			if (stops.contains(word)) {
				continue;
			}

			String term = myStemmer.stemResult(word);
			if (queryTerms.contains(term)) {
				continue;
			}

			queryTerms.add(term);
		}

		return queryTerms;
	}

}
