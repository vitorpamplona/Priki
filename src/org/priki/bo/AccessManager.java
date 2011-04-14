package org.priki.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.priki.bo.Wikiword.Visibility;
import org.priki.utils.StringMap;

public class AccessManager implements Serializable {
	public static final long serialVersionUID= 1L;

    public static enum SignUp { AsUser, AsReader, AsEditor };
	
    public static final String DEFAULT_ADMIN = "admin";
    public static final String DEFAULT_PASSWD = "priki";
    public static final SignUp DEFAULT_SIGNUP = SignUp.AsEditor;
    public static final boolean DEFAULT_READONLY = false;
    public static final boolean DEFAULT_ACCEPT_ANONYMOUS_EDITOR = true;
    public static final boolean DEFAULT_ACCEPT_ANONYMOUS_READER = true;

    private StringMap<User> users;
    private Set<User> admins;
    private Set<User> readers;
    private Set<User> editors;
    
    private SignUp signup = DEFAULT_SIGNUP;
    
    private boolean defaultCaseSensitive = true;
    private Visibility defaultVisibility = Wikiword.Visibility.Public;
    
    private boolean readonly = DEFAULT_READONLY;
    private boolean acceptAnonymousEditor = DEFAULT_ACCEPT_ANONYMOUS_EDITOR;
    private boolean acceptAnonymousReader = DEFAULT_ACCEPT_ANONYMOUS_READER;
    
    private Set<User> readers() {
        if (readers == null) readers = new HashSet<User>(); 
        return readers;
    }

    private Set<User> editors() {
        if (editors == null) editors = new HashSet<User>();
        return editors;
    }

    private StringMap<User> users() {
        if (users == null) {
        	users = new StringMap<User>();
        	users.put(DEFAULT_ADMIN, new User(DEFAULT_ADMIN, DEFAULT_PASSWD, "Administrator", "") );
        	addAdmin(DEFAULT_ADMIN);
        }
        return users;
    }
    
    private Set<User> admins() {
    	if (admins== null) {
    		admins = new HashSet<User>();
    		addAdmin(DEFAULT_ADMIN);
    	}
    	return admins;
    }
    
    public boolean isReadonly() { return this.readonly; }
    public void setReadonly(boolean readonly) { this.readonly = readonly; }

	public boolean isAcceptAnonymousEditor() { return acceptAnonymousEditor; }
	public void setAcceptAnonymousEditor(boolean acceptAnonymousEditor) { this.acceptAnonymousEditor = acceptAnonymousEditor; }
	
	public boolean isAcceptAnonymousReader() { return acceptAnonymousReader; }
	public void setAcceptAnonymousReader(boolean acceptAnonymousReader) { this.acceptAnonymousReader = acceptAnonymousReader; }
    
	public boolean isDefaultCaseSensitive() { return defaultCaseSensitive; }
	public void setDefaultCaseSensitive(boolean defaultCaseSensitive) { this.defaultCaseSensitive = defaultCaseSensitive; }

	public Visibility getDefaultVisibility() {
		if (defaultVisibility == null) {
			defaultVisibility  = Wikiword.Visibility.Public;
		}
		return defaultVisibility; 
	}
	public void setDefaultVisibility(Visibility defaultVisibility) { this.defaultVisibility = defaultVisibility; }
	
    public boolean isAdmin(User user) {
    	return admins().contains(user);
    }

    public boolean isAdmin(String user) {
    	return isAdmin(getUser(user));
    }

    public boolean isReader(String user) {
    	return isReader(getUser(user));
    }
    
    public boolean isReader(User user) {
        return readers().contains(user);
    }

    public boolean isEditor(User user) {
        return editors().contains(user);
    }    

    public boolean isEditor(String user) {
    	return isEditor(getUser(user));
    }
    
    public boolean isUser(String login) {
        return users().containsKey(login);
    }    
    
    public User getUser(String login) {
        return users().get(login);
    }
    
    public User getUserByCookieId(String cookie) {
    	if (cookie == null) return null;
    	for (User u : users().values()) {
    		if (cookie.equals(u.getCookieId()))
    			return u;
    	}
        return null;
    }

    public User getUserByEmail(String email) {
    	if (email == null) return null;
    	for (User u : users().values()) {
    		if (email.equals(u.getEmail()))
    			return u;
    	}
        return null;
    }
    
    public List<User> getEscortingUsers() {
    	List<User> ret = new ArrayList<User>();
    	for (User u : users().values()) {
    		if (u.isEscortWiki())
    			ret.add(u);
    	}
    	return ret;
    }
    
    public Set<User> getEscortingUsers(User user, Wikiword page) {
    	List<User> users = getEscortingUsers();
    	users.addAll(page.getEscortWikiWord());
    	
    	Set<User> ret = new HashSet<User>();
    	
    	for (User u : users) {
    		if (iCanRead(u.getIdentifier(), page)) {
    			ret.add(u);
    		}    	
    	}
    	
    	return ret;
    }
    
    public void updateUser(User user) {
		User inTheBase = users().get(user.getIdentifier());
		// Update
		inTheBase.setCompleteName(user.getCompleteName());
		inTheBase.setEmail(user.getEmail());
		inTheBase.setPassword(user.getPassword());
		inTheBase.setEscortWiki(user.isEscortWiki());
    }
    
    public void addUser(User user) {
    	if (!isUser(user.getIdentifier())) {
    		users().put(user.getIdentifier(), user);
    		
    		if (signup == SignUp.AsReader) {
    			addReader(user.getIdentifier());
    		}
    		if (signup == SignUp.AsEditor) {
    			addEditor(user.getIdentifier());
    		}    		
    	} 
    }
    
    public void addAdmin(String userName) {
		addReader(userName);
		addEditor(userName);
    	
    	if (isUser(userName)) {
    		admins().add(getUser(userName));
    	}
    }

    public void addEditor(String userName) {
		addReader(userName);
    	
    	if (isUser(userName)) {
    		editors().add(getUser(userName));
    	}
    }
    
    public void addReader(String userName) {
    	if (isUser(userName))  
    		readers().add(getUser(userName));
    }

    public List<User> getAllUsers() {
    	return new ArrayList<User>(users().values());
    }

    public List<String> getAllAdmins() {
    	List<String> ret = new ArrayList<String>(); 
    	for (User u : admins()) {
    		ret.add(u.getKeyword());
    	}
    	return ret;
    }
    
    public void removeAdmin(String userName) {
    	if (isUser(userName)) 
    		admins().remove(getUser(userName));
    }

    public void removeEditor(String userName) {
    	removeAdmin(userName);
    	
    	if (isUser(userName)) 
    		editors().remove(getUser(userName));
    }
    
    public void removeReader(String userName) {
    	removeAdmin(userName);
    	removeEditor(userName);
    	
    	if (isUser(userName)) 
    		readers().remove(getUser(userName));
    }

    
    
    public int getUserCount() {
        return users().size();
    }

    public int getReadersCount() {
        return readers().size();
    }
    
    public int getEditorsCount() {
        return editors().size();
    }
    
    public int getAdminsCount() {
        return admins().size();
    }
    
    public void setSignUp(SignUp signup) {
    	this.signup = signup;
    }
    
    public void clear() {
    	editors().clear();
    	readers().clear();
    	users().clear();
    	admins().clear();
    	readonly = DEFAULT_READONLY;
        acceptAnonymousEditor = DEFAULT_ACCEPT_ANONYMOUS_EDITOR;
        acceptAnonymousReader = DEFAULT_ACCEPT_ANONYMOUS_READER;
    	admins.add(new User(DEFAULT_ADMIN, DEFAULT_PASSWD, "Administrator", "") );
    }

	public SignUp getSignUp() {
		return signup;
	}

	public boolean iCanPost(String user, Wikiword w) {
		if (readonly) return false;
		
		User userObj = getUser(user);
		
		if ((!isUser(user)) && (!acceptAnonymousEditor)) return false; 
		
		if (isUser(user) && !isEditor(userObj)) return false;
		
		if (w != null && !w.isPublic()) {
			if (w.isToUsers() && !isUser(user)) return false;
			if (w.isToReaders() && !isReader(userObj)) return false;
			if (w.isToEditors() && !isEditor(userObj)) return false;
			if (w.isToAdmins()  && !isAdmin(userObj)) return false;
		}		
		
		return true;
	}

	public boolean iCanRead(String user, Wikiword w) {
		User userObj = getUser(user);
		
		if ((!isUser(user)) && (!acceptAnonymousReader)) return false; 
		
		if (isUser(user) && !isReader(userObj)) return false;

		if (w != null && !w.isPublic()) {
			if (w.isToUsers() && !isUser(user)) return false;
			if (w.isToReaders() && !isReader(userObj)) return false;
			if (w.isToEditors() && !isEditor(userObj)) return false;
			if (w.isToAdmins()  && !isAdmin(userObj)) return false;
		}
		return true;
	}
	
	public boolean checkLogin(String user, String password) {
		if (user == null || password == null) return false;
		
		User regUser = users().get(user);
		
		if (regUser == null) {
			regUser = getUserByEmail(user);
		}
		
		if (regUser == null) return false;
		
		if (regUser.getPassword().equals(password)) {
			return true;
		}
		
		return false;
	}

	public void setRoles(String userName, boolean isReader, boolean isEditor, boolean isAdmin) {
    	if (!isUser(userName)) return;
    	
    	User user = getUser(userName);
		
    	if (isAdmin) {
    		readers().add(user);
    		editors().add(user);
    		admins().add(user);
    	} else if (isEditor) {
    		admins().remove(user);
    		editors().add(user);
    		readers().add(user);
       	} else if (isReader) {
       		admins().remove(user);
    		editors().remove(user);
    		readers().add(user);
    	} else {
       		admins().remove(user);
    		editors().remove(user);
    		readers().remove(user);    		
    	}
	}




	

}
