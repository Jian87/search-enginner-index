package com.jian.utdir.ranker;

import java.io.File;
import java.util.Scanner;

public class HITS {
	int[][] adjMatrix;
	int[] outdegree;
	double[] hubArray;
	double[] authArray;
	
	public void init(int vextexCount, double initValue) {
		
		int iter = 0;
		hubArray = new double[vextexCount];
		authArray = new double[vextexCount];
		if(initValue == 0 || initValue == 1) {
			while(iter < vextexCount) {
				hubArray[iter] = initValue;
				authArray[iter] = initValue;
				iter++;
			}
		} else if(initValue == -1) {
			while(iter < vextexCount) {
				hubArray[iter] = 1.0 / vextexCount;
				authArray[iter] = 1.0 / vextexCount;
				iter++;
			}
		} else if(initValue == -2) {
			while(iter < vextexCount) {
				hubArray[iter] = 1.0 / Math.sqrt(vextexCount);
				authArray[iter] = 1.0 / Math.sqrt(vextexCount);
				iter++;
			}
		}
		
	}
	
	public double[] updateAuthArray(int vextexCount, double[] scaleHub, double[] scaleAuth) {
		
		for(int i = 0; i < vextexCount; i++) {
			scaleAuth[i] = 0.0;
			for(int j = 0; j < vextexCount; j++) {
				if(adjMatrix[j][i] == 1) {
					scaleAuth[i] += scaleHub[j];
				}
			}
		}
		
		return scaleAuth;
		
	}
	
	public double[] updateHubArray(int vextexCount, double[] scaleHub, double[] scaleAuth) {
		
		for(int i = 0; i < vextexCount; i++) {
			scaleHub[i] = 0.0;
			for(int j = 0; j < vextexCount; j++) {
				if(adjMatrix[i][j] == 1) {
					scaleHub[i] += scaleAuth[j];
				}
			}
		}
		
		return scaleHub;
	}
	
	public double[] scaleArray(int vextexCount, double[] arr) {
		double sum = 0.0;
		
		for(int i = 0; i < vextexCount; i++) {
			sum += arr[i] * arr[i];
		}
		
		for(int i = 0; i < vextexCount; i++) {
			arr[i] = arr[i] / Math.sqrt(sum);
		}
		
		return arr;
	}
	
	public boolean errorCalculate(double[] arr, double[] target, int vextexCount, int iteration) {
		
		double errorRate = 0.00001;
		if(iteration < 0) {
			errorRate = Math.pow(10, iteration);
		}
		
		for(int i = 0; i < vextexCount; i++) {
			if(Math.abs(arr[i] - target[i]) > errorRate) {
				return false;
			}
		}
		
		return true;
	}
	
	public void hitsCalculate(int iteration, int initValue, String fileName) {
		int vextexCount = 0;
		int edgeCount = 0;
		int nrow = 0;
		int ncol = 0;
		int iter = 0, iter2 = 0;
		
		double[] prevAuth = null, prevHub = null, scaleHub = null, scaleAuth = null;
		
		try {
			Scanner scanner = new Scanner(new File(fileName));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		prevAuth = new double[vextexCount];
		prevHub = new double[vextexCount];
		scaleAuth = new double[vextexCount];
		scaleHub = new double[vextexCount];
		
		iteration = 0;
		initValue = -1;
		init(vextexCount, initValue);
		
		for(iter = 0; iter < vextexCount; iter++) {
			scaleHub[iter] = hubArray[iter];
			scaleAuth[iter] = authArray[iter];
			prevAuth[iter] = scaleAuth[iter];
			prevHub[iter] = scaleHub[iter];
		}
		
		do {
			
			for(iter = 0; iter < vextexCount; iter++) {
				prevAuth[iter] = scaleAuth[iter];
				prevHub[iter] = scaleHub[iter];
			}
			
			scaleAuth = updateAuthArray(vextexCount, scaleHub, scaleAuth);
			scaleHub = updateHubArray(vextexCount, scaleHub, scaleAuth);
			scaleAuth = scaleArray(vextexCount, scaleAuth);
			scaleHub = scaleArray(vextexCount, scaleHub);
		} while (!errorCalculate(scaleHub, prevHub, vextexCount, iteration) || !errorCalculate(scaleAuth, prevAuth, vextexCount, iteration));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
