package com.jian.utdir.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jian.utdir.dto.DocDTO;
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
	
	@ResponseBody
	@RequestMapping("/parse")
	public String parse() {
		
		// data folder path, eg. C:\\Users\\lixua\\Documents\\2021spring\\6322\\data
		File dataFolder = new File("C:\\Users\\lixua\\Documents\\2021spring\\6322\\java_project\\data");
		
		// stop file path, eg. C:\\Users\\lixua\\Documents\\2021spring\\6322\\a2\\resources\\stopwords
		File stopFile = new File("C:\\Users\\lixua\\Documents\\2021spring\\6322\\a2\\resources\\stopwords");

		try {
			parser.readStopFile(stopFile);
			parser.readFiles(dataFolder);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "successfully";
	}
	

	@RequestMapping("/")
	public String home() {

		return "index";
	}
	
	
	@ResponseBody
	@RequestMapping( value = "/search", produces="application/json")
	public List<List<SearchResultDTO>> search(@RequestParam("searchContent") String searchContent) {
		
		if(searchContent == null) {
			return null;
		}
		
		List<List<SearchResultDTO>> jsonObjs = searchService.search(searchContent);
		
		return jsonObjs;
	}
}
