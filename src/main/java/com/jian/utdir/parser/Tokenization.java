package com.jian.utdir.parser;

import org.springframework.stereotype.Component;

import com.jian.utdir.stem.MyStemmer;

@Component
public class Tokenization {
	
	public void tokenize(String line, Handler handler) {
		
		line = transformString(line);
		
		String[] words = line.split("\\s+");
		
		for(String token: words) {
			if(token.isEmpty()) {
				continue;
			}
			
			// get the stem
			MyStemmer myStemmer = new MyStemmer();
			String stemResult = myStemmer.stemResult(token);
		
			// store the term and doc info into handler
			handler.store(token, stemResult);
		}
		
	}
	
	
	public String transformString(String line) {

		// Replacing the SGML tags with space.
		line = line.replaceAll("\\<.*?>", " ");

		// Remove digits
		line = line.replaceAll("[\\d+]", "");

		// Remove the special characters
		line = line.replaceAll("[+^:,?;=%#&~`$!@*_)/(}{\\.]", "");

		// Remove possessives
		line = line.replaceAll("\\'s", "");

		// Replace "'" with a space
		line = line.replaceAll("\\'", " ");

		// Replace - with space to count two words
		line = line.replaceAll("-", " ");

		// Remove multiple white spaces
		line = line.replaceAll("\\s+", " ");

		// Trim and set text to lower case
		line = line.trim().toLowerCase();

		return line;
	}

}
