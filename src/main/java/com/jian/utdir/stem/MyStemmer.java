package com.jian.utdir.stem;

import org.springframework.stereotype.Component;

/*
 * implements the stemmer
 */

@Component
public class MyStemmer {

	
	public String stemResult(String token) {
		
		Stemmer stemmer = new Stemmer();
		
		for(char c: token.toCharArray()) {
			stemmer.add(c);
		}
		
		stemmer.stem();
		
		return stemmer.toString();
	}
	
	
}
