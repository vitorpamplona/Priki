package org.priki.bo;

import java.io.Serializable;

public class I18N implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String language;
	private String key;
	private String text;
	
	public I18N(String language, String key, String text) {
		super();
		this.language = language;
		this.key = key;
		this.text = text;
	}
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

}
