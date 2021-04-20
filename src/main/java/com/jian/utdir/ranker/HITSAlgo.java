package com.jian.utdir.ranker;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class HITSAlgo {
	public Map<String, Double> authMap;
	public Map<String, Double> hubMap;
	public Map<String, Set<String>> graph; // recorde node and its hubs(source nodes);

	public HITSAlgo() {
        authMap = new HashMap<>();
        hubMap = new HashMap<>();
        graph = new HashMap<>();
    }

	public void hits(File file, Map<String, String> id2urlMap) {

		Set<String> linkCount = new HashSet<>();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.isEmpty()) {
					continue;
				}
				line = line.toLowerCase();
				String[] links = line.split("\\s+");
				if(links.length != 2 || links[1].equals("null") || !id2urlMap.containsKey(links[0]) || !id2urlMap.containsKey(links[1])) {
					continue;
				}
				String currLink = links[0];
				linkCount.add(currLink);

				if (!graph.containsKey(currLink)) {
					graph.put(currLink, new HashSet<>());
				}

				for (int i = 1; i < links.length; i++) {
					linkCount.add(links[i]);
					graph.get(currLink).add(links[i]);
				}

			}
			scanner.close();
			init(linkCount);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Double> prevHub = new HashMap<>();
		Map<String, Double> prevAuth = new HashMap<>();
		Map<String, Double> scaleHub = new HashMap<>();
		Map<String, Double> scaleAuth = new HashMap<>();

		for (String link : linkCount) {
			scaleHub.put(link, hubMap.get(link));
			scaleAuth.put(link, authMap.get(link));
			prevHub.put(link, scaleHub.get(link));
			prevAuth.put(link, scaleAuth.get(link));
		}

		int iter = 0;

		do {
			for (String link : linkCount) {
				prevAuth.put(link, scaleAuth.get(link));
				prevHub.put(link, scaleHub.get(link));
			}

			scaleAuth = updateAuthMap(linkCount, scaleHub, scaleAuth);
			scaleHub = updateHubMap(linkCount, scaleHub, scaleAuth);
			scaleAuth = scaleMap(scaleAuth);
			scaleHub = scaleMap(scaleHub);
			iter++;

		} while (!errorateCalculation(scaleAuth, prevAuth) || !errorateCalculation(scaleHub, prevHub));

		for (String key : authMap.keySet()) {
			authMap.put(key, Double.parseDouble(String.format("%.3f", scaleAuth.get(key))));
		}

		for (String key : hubMap.keySet()) {
			hubMap.put(key, Double.parseDouble(String.format("%.3f", scaleHub.get(key))));
		}

		System.out.println("iterations are: " + iter);
	}

	public void init(Set<String> links) {

		//int count = links.size();

		for (String link : links) {
			// hubMap.put(link, 1.0 / (double) count);
			// authMap.put(link, 1.0 / (double) count);
			hubMap.put(link, 1.0);
			authMap.put(link, 1.0);
		}
	}

	public Map<String, Double> updateAuthMap(Set<String> links, Map<String, Double> scaleHub,
			Map<String, Double> scaleAuth) {

		for (String link : links) {
			if (!graph.containsKey(link))
				continue;
			for (String hub : graph.get(link)) {
				scaleAuth.put(link, scaleAuth.get(link) + scaleHub.getOrDefault(hub, 0.0));
			}
		}

		return scaleAuth;
	}

	public Map<String, Double> updateHubMap(Set<String> links, Map<String, Double> scaleHub,
			Map<String, Double> scaleAuth) {

		for (String link : links) {
			if (!graph.containsKey(link))
				continue;
			for (String auth : graph.keySet()) {
				if (graph.get(auth).contains(link)) {
					scaleHub.put(link, scaleHub.get(link) + scaleAuth.getOrDefault(auth, 0.0));
				}
			}
		}

		return scaleHub;
	}

	public Map<String, Double> scaleMap(Map<String, Double> inputMap) {

		double sum = 0.0;

		for (String key : inputMap.keySet()) {
			sum += inputMap.get(key);
		}

		for (String key : inputMap.keySet()) {
			inputMap.put(key, inputMap.get(key) / Math.sqrt(sum));
		}

		return inputMap;
	}

	private static boolean errorateCalculation(Map<String, Double> curr, Map<String, Double> target) {
		double errorRate = 0.00001; // Default errorrate of iteration is 0

		for (String key : curr.keySet()) {
			if (Math.abs(curr.get(key) - target.get(key)) > errorRate) {
				return false;
			}
		}

		return true;
	}

	public void print() {
		System.out.println("below is hub map: ");
		System.out.println(hubMap);
		System.out.println("\n");

		System.out.println("below is auth map: ");
		System.out.println(authMap);
		System.out.println("\n");

	}
}
