package org.priki.bo;

import java.io.Serializable;

public class Plugin implements Serializable, Comparable<Plugin> {
	public static final long serialVersionUID= 1L;
	
	private String name;
	private int position;
	private int order;
	private String html;
	
	public Plugin(String name, int position, String html, int order) {
		super();
		this.name = name;
		this.position = position;
		this.html = html;
		this.order = order;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	@Override
	public int compareTo(Plugin o) {
		if (order < o.order) return -1;
	    if (order > o.order) return 1;
	        
	    return 0;
	}
}
