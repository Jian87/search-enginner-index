package com.jian.utdir.service;

import java.util.List;

import com.jian.utdir.dto.SearchResultDTO;

/*
 * deprecated interface
 */

public interface SearchService {
	
	List<List<SearchResultDTO>> search(String inputContent);
}
