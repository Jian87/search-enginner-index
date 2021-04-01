package com.jian.utdir.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jian.utdir.dto.SearchResultDTO;
import com.jian.utdir.parser.Parser;
import com.jian.utdir.ranker.PageRanker;
import com.jian.utdir.service.SearchServiceImpl;

@Controller
public class SearchController {
	
	@Autowired
	SearchServiceImpl searchService;
	@Autowired
	Parser parser;
	
	@Autowired
	PageRanker pageRanker;
	
	@RequestMapping("/parse")
	public String parse() {
		
		// data folder path, eg. C:\\Users\\lixua\\Documents\\2021spring\\6322\\data
		File dataFolder = new File("C:\\Users\\lixua\\Documents\\2021spring\\6322\\data");
		
		// stop file path, eg. C:\\Users\\lixua\\Documents\\2021spring\\6322\\a2\\resources\\stopwords
		File stopFile = new File("C:\\Users\\lixua\\Documents\\2021spring\\6322\\a2\\resources\\stopwords");

		try {
			parser.readStopFile(stopFile);
			parser.readDataFile(dataFolder);
			
			// page ranker read the input file
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/";
	}
	

	@RequestMapping("/")
	public String home() {

		return "index";
	}
	
	
	@ResponseBody
	@RequestMapping( value = "/search", produces="application/json")
	public List<SearchResultDTO> search(@RequestParam("searchContent") String searchContent) {
		
		if(searchContent == null) {
			return null;
		}
		
		List<String> searchResult = searchService.search(searchContent);
		
		List<SearchResultDTO> jsonObjs = new ArrayList<>();
		
		for(String str: searchResult) {
			SearchResultDTO searchResultDTO = new SearchResultDTO(str, "");
			jsonObjs.add(searchResultDTO);
		}
		
		return jsonObjs;
	}
}
