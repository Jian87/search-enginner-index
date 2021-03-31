package com.jian.utdir.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.jian.utdir.model.Dictionary;

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

	public void readDataFile(File folder) throws IOException {

		if (folder == null) {
			return;
		}

		for (File file : folder.listFiles()) {

			Scanner myReader = new Scanner(file);
			Tokenization tokenization = new Tokenization();

//			String fileName = file.getName().replaceAll("[^\\d]", "");
			String fileName = file.getName();
			Handler handler = new Handler(this.stops, fileName);

			// read the file line by line
			while (myReader.hasNext()) {

				String line = myReader.nextLine();

				if (line.isEmpty()) {
					continue;
				}

				tokenization.tokenize(line, handler);
			}

			myReader.close();

			this.dictionary.add(handler);
		}
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
