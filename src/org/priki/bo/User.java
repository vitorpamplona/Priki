package org.priki.bo;

public class User extends Wikiword implements Someone {

	public static final long serialVersionUID= 19L;
	
	private String password;
	private String email;
	private String completeName;
	private String cookieId;
	private boolean escortWiki;
	
    public User(String login) {
		super(login);
	}

    public User(String login, String password, String completeName, String email) {
		super(login);
		this.password = password;
		this.completeName = completeName;
		this.email = email;
	}
    
    public User(String login, String password, String completeName, String email, boolean escortWiki) {
		super(login);
		this.password = password;
		this.completeName = completeName;
		this.email = email;
		this.escortWiki = escortWiki;
	}    
    
    public String getIdentifier() {
    	return getKeyword();
    }
    
	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
 
	public String getName() {
		if (completeName == null) return getIdentifier();
		return completeName;
	}

	public String getCookieId() {
		return cookieId;
	}

	public void setCookieId(String cookieId) {
		this.cookieId = cookieId;
	}

	public boolean isEscortWiki() {
		return escortWiki;
	}

	public void setEscortWiki(boolean escortWiki) {
		this.escortWiki = escortWiki;
	}
    
}
