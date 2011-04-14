package org.priki.bo;

import java.io.Serializable;

public class AnonymousUser implements Someone, Serializable {
	public static final long serialVersionUID= 12L;

	private String ip;
	
	public AnonymousUser (String ip) {
		this.ip = ip;
	}
	
	public String getIdentifier() {
		return ip;
	}

	public String getIP() {
		return ip;
	}
	
	public String toString() {
		return getIP();
	}
	
	public String getName() {
		return ip;
	}
}
