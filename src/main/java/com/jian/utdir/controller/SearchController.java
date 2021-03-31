package com.jian.utdir.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
		
		File dataFolder = new File("C:\\Users\\lixua\\Documents\\2021spring\\6322\\data");
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
	@RequestMapping("/search")
	public String search(@RequestParam("searchContent") String searchContent) {
		
		List<String> res = searchService.search(searchContent);
		
		return res.toString();
	}
}
