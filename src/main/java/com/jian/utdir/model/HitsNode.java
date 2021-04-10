package com.jian.utdir.model;

import java.util.HashSet;
import java.util.Set;

public class HitsNode {

	public String name;
	public Set<HitsNode> nexts;
	public double hubVal;
	public double authVal;
	
	public HitsNode(String name) {
		this.name = name;
		this.nexts = new HashSet<HitsNode>();
		this.hubVal = 0.0;
		this.authVal = 0.0;
	}
}
