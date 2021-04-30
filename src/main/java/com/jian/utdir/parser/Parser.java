package com.jian.utdir.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.jian.utdir.model.Dictionary;
import com.jian.utdir.model.FileContent;
import com.jian.utdir.model.PageRankNode;
import com.jian.utdir.ranker.HITSAlgo;
import com.jian.utdir.ranker.PageRanker;

@Component
public class Parser {

	private Set<String> stops;
	private Dictionary dictionary;

	public Parser() {
		this.stops = new HashSet<String>();
		this.dictionary = new Dictionary();
	}

	public void readStopFile(File file) throws FileNotFoundException, IOException {

		Scanner myReader = new Scanner(file);

		while (myReader.hasNext()) {

			String word = myReader.next();

			if (word.isEmpty()) {
				continue;
			}

			stops.add(word);
		}

		myReader.close();
	}
	
	public void readFiles(File folder) throws IOException {
		
		for(File countryFolder: folder.listFiles()) {
			if(countryFolder.listFiles() == null) {
				continue;
			}
			File dataFolder = null, webgraphFile = null, id2url = null;
			for(File file: countryFolder.listFiles()) {
				if(file.isDirectory()) {
					if(file.getName().equals("data_pages")) {
						dataFolder = file;
					}
				} else if(file.getName().equals("auth_links")) {
					webgraphFile = file;
				} else if(file.getName().equals("id2url")) {
					id2url = file;
				}
			}
			
			if(dataFolder == null || webgraphFile == null || id2url == null) {
				continue;
			}
			
			Set<String> linkSet = new HashSet<>();
			Map<String, FileContent> fileContentMap = readDataFile(dataFolder, linkSet);
			
			// get id2url map
			Map<String, String> id2urlMap = getId2UrlMap(id2url, linkSet, fileContentMap);
			
			// calculate each id's pagerank
			PageRanker pageRanker = new PageRanker();
			pageRanker.initGraph(webgraphFile, id2urlMap);
//			
			HITSAlgo hitsAlgo = new HITSAlgo();
			hitsAlgo.hits(webgraphFile, id2urlMap);
			
			handleData(fileContentMap, pageRanker.pageGraph, hitsAlgo.authMap, hitsAlgo.hubMap);
		}		
	}
	
	public void handleData(Map<String, FileContent> fileContentMap, Map<String, PageRankNode> pageRankMap,
			Map<String, Double>authMap, Map<String, Double> hubMap) {
		for(String fileName: fileContentMap.keySet()) {
			
			Tokenization tokenization = new Tokenization();
			Handler handler = new Handler(this.stops, fileName);
			
			String id = fileContentMap.get(fileName).getId();
			String title = fileContentMap.get(fileName).getTitle();
			String content = fileContentMap.get(fileName).getContent();
			String line = title + content;
			
			tokenization.tokenize(line, handler);
			
			double pageRank = pageRankMap.get(id) == null? 0.0: pageRankMap.get(id).getPageRank();
			double auth = authMap.getOrDefault(id, 0.0);
			double hub = hubMap.getOrDefault(id, 0.0);
		
			// pagerank + hits
			pageRank += (auth + hub);
			
			this.dictionary.add(handler, pageRank, fileContentMap.get(fileName));
		}
	}

	// read data_pages
	public Map<String, FileContent> readDataFile(File folder, Set<String> linkSet) throws IOException {

		if (folder == null) {
			return null;
		}
		
		Map<String, FileContent> hmap = new HashMap<String, FileContent>();
		
		// read all the data from data folder
		for (File subfolder : folder.listFiles()) {
			for(File file: subfolder.listFiles()) {
				String fileName = decodeFileName(file.getName());
				int index = fileName.lastIndexOf(".");
	            if (index != -1) {
	                String extension = fileName.substring(index + 1).toLowerCase();
	                if (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg")
	                        || extension.equals("tiff") || extension.equals("gif") || extension.equals("ico")
	                        || extension.equals("icon") || extension.equals("pdf")) {
	                    continue;
	                }
	            }
				linkSet.add(fileName);
				String[] fileContent = getDOM(file);
				if(fileContent == null) {
					linkSet.remove(fileName);
					continue;
				}
				
				hmap.put(fileName, new FileContent(fileName, fileContent[0], fileContent[1]));
			}
		}
		
		// System.out.println("total web site I collected: " + linkSet.size());
		
		return hmap;
	}
	
	public String[] getDOM(File file) {
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        String fileContentString;
        try (FileInputStream inputStream = new FileInputStream(file)) {

            inputStream.read(fileContent);
            fileContentString = new String(fileContent);

            Document doc = Jsoup.parse(fileContentString);
            String title = doc.title().trim();
            fileContentString = doc.body().text();
            fileContentString = fileContentString.trim();
            return new String[] {title, fileContentString};
        } catch (FileNotFoundException e) {
            System.out.println(file.getName());
            System.out.println("There's not such file.");
        } catch (IOException e) {
            System.out.println("IO exception " + e.toString());
        }

        return null;
	}
	
	public String decodeFileName(String fileName) {
		String s = null;
        try {
            s = URLDecoder.decode(fileName,
                    "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
	}
	

	
	public Map<String, String> getId2UrlMap(File id2url, Set<String> linkSet, Map<String, FileContent> fileContentMap) {
		Map<String, String> hmap = new HashMap<String, String>();
		
		Scanner myReader;
		try {
			myReader = new Scanner(id2url);
			// read the file line by line
			while (myReader.hasNext()) {

				String line = myReader.nextLine();

				if (line.isEmpty()) {
					continue;
				}
				
				String[] strs = line.split("\\s+");
				if(strs.length != 2 || strs[1].equals("null") || !linkSet.contains(strs[1])) {
					continue;
				}
				hmap.put(strs[0], strs[1]);
				FileContent fileContent = fileContentMap.get(strs[1]);
				fileContent.setId(strs[0]);
				fileContentMap.put(strs[1], fileContent);
			}

			myReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hmap;
	}

	public Set<String> getStops() {
		return stops;
	}

	public void setStops(Set<String> stops) {
		this.stops = stops;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

}
