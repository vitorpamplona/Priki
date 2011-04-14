/*
 * Priki - Prevalent Wiki
 * Copyright (c) 2005 Priki 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 *
 * @author Vitor Fernando Pamplona - vitor@babaxp.org
 *
 */
package org.priki.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.priki.utils.SortedBag;

public class Wikiword extends Element implements Serializable, Comparable<Wikiword> {
    public static final long serialVersionUID= 1L;

    public static final boolean DEFAULT_CASE_SENSITIVE_VALUE = true;
    public static final Visibility DEFAULT_VISIBILITY = Visibility.Public;
    
    public static enum Visibility { Public, User, Reader, Editor, Admin };
    
    /* History of the description about this element*/ 
    private HistoryText definition;
    
    /* Hold an list of text that are using this element. 
     * SortedBag is a Set with a counter of inclusion for each word. 
     * We use SortedBag here for 2 purposes 
     *  - Count how many wikiwords are duplicated and putting this in the top. Doing so
     *    we have the most important related words first. 
     *  - Avoid the re-relate All when a comment is removed, because it could remove
     *    a related word that is also in the definition text.      
     **/
    private SortedBag<Wikiword> related;

    /* tags related to this wikiword. */
    private SortedBag<Wikiword> tags;
    
    /* comments for each Wikiword. */
    private List<Text> comments;
    
    /* Users escorting this Wikiword. */
    private Set<User> escortWord;
    
    /* if this wikiword is case sensitive */
    private Boolean caseSensitive = DEFAULT_CASE_SENSITIVE_VALUE;

    /* Wikiword text is visible only to a group */ 
    private Visibility visibility = DEFAULT_VISIBILITY;
    
    /* Creates a new Wikiword with a key. */
    public Wikiword (String key) {
        super(key);
    }
    
    public boolean isUsed() {
    	return !escortWord().isEmpty() 
    	    || !comments().isEmpty() 
    	    || !tags().isEmpty() 
    	    || !related().isEmpty() 
    	    || definition().hasCurrent();
    }
    
    public boolean isTalkingAbout(Element thisThing) {
    	if (!hasDefinition()) {
    		return false;
    	} else {
    		// Check if this page is talking about thisThing in comments
    		// tags or in the current text
    		boolean isTalkingInComments = false; 
    		for (Text t : comments()) {
    			if (t.has(thisThing)) {
    				isTalkingInComments = true;
    				break;
    			}
    		}
    		
    		if (getDefinition().has(thisThing)
 			||  getTags().contains(thisThing)
 			||  isTalkingInComments)
    			return true;
    		
    		return false;
    	}
    }

    // ***************************
    // Collection creators.
    // ***************************    
    private HistoryText definition() {
        if (definition == null)
            definition = new HistoryText(this);
        return definition;
    }

    private SortedBag<Wikiword> related() {
        if (related == null)
            related = new SortedBag<Wikiword>();
        return related;
    }

    private SortedBag<Wikiword> tags() {
        if (tags == null)
        	tags = new SortedBag<Wikiword>();
        return tags;
    }
    
    private List<Text> comments() {
        if (comments == null)
        	comments = new ArrayList<Text>();
        return comments;
    }
   
    private Set<User> escortWord() {
    	if (escortWord == null) 
    		escortWord = new HashSet<User>();
    	return escortWord;
    }
    
    public Set<User> getEscortWikiWord() {
    	return escortWord();
    }
    
    // ***************************
    // Related Text
    // ***************************
    
    /**
     * Called when a comment or a tag is removed. 
     * Causes remove elements using reference  
     */
    private void unrelateText(Text text) {
    	if (text == null) return;
        for (Element e : text) {
            if (e instanceof Wikiword){
                ((Wikiword)e).removeRelated(this);
            }
        }
    }
    
    /**
     * Called when a comment is included. 
     */
    private void relateText(Text text) {
    	if (text == null) return;
        for (Element e : text) {
            if (e instanceof Wikiword){
                ((Wikiword)e).addRelated(this);
            }
        }
    }        
    
    public void cleanRelated() {
    	unrelateText(definition.getCurrent());
    	for (Text t: comments()) {
    		unrelateText(t);
    	}
    	for (Wikiword tag: tags()) {
    		tag.removeRelated(this);
    	}
    }
    
    public void relateAll() {
    	relateText(definition.getCurrent());
    	for (Text t : comments()) {
    		relateText(t);	
    	}
    	for (Wikiword tag : tags()) {
    		tag.addRelated(this);
    	}
    }
    
    /** sets that the text in param is using this element. */
    public void addRelated(Wikiword word) {
        related().add(word);
    }
    
    /** sets that the text in param is using this element. */
    public void removeRelated(Wikiword word) {
        related().remove(word);
    }
    
    /** Returns all Text that are using this Element. */ 
    public SortedBag<Wikiword> getRelated() {
        return related();
    }    
    
    // ***************************
    // Comments
    // ***************************
    public void addComment(Text text) {
    	comments().add(text);
    	if (text.getWhoPosted() instanceof User) {
    		escortWord().add((User)text.getWhoPosted());
    	}
    	relateText(text);
    }
    
    /**
     * Do not remove the escorting user.
     * The user must explicity do this. 
     */
    public Text removeComment(int index) {
    	Text t = comments().remove(index);
    	unrelateText(t);
    	return t;
    }
    
    public void clearComments() {
        for (Text comm : comments()) {
        	unrelateText(comm);
        }
    	comments = null;
    }

    public Date hasCommentBy(String byUser) {
        for (Text comm : comments()) {
        	if (byUser.equals(comm.getWhoPosted().getIdentifier())) {
        		return comm.getPostDate();
        	}
        }
        
        return null;
    }
    
    public Date hasCommentAfter(Date since) {
        for (Text comm : comments()) {
        	if (!since.after(comm.getPostDate())) {
        		return comm.getPostDate();
        	}
        }
        
        return null;
    }    

	public List<Text> getComments() {
		return comments();
	}
	
	public Date getLastCommentPostDate() {
		if (comments().isEmpty()) return null; 
		return comments().get(comments().size()-1).getPostDate();
	}
    
    // ***************************
    // Escorting
    // ***************************
    public void addEscortWikiword(User user) {
    	escortWord().add(user);
    }

    public void removeEscortWikiword(User user) {
    	escortWord().remove(user);
    }

    
    public boolean isEscorting(String user) {
    	if (user == null) return false;
    	for (User u : escortWord()) {
    		if (user.equals(u.getIdentifier())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    // *************************
    // Tags
    // *************************
    public void addTag(Wikiword tag) {
    	tags().add(tag);
    	tag.addRelated(this);
    }

    public void removeTag(Wikiword tag) {
    	tags().remove(tag);
    	tag.removeRelated(this);
    }        
    
    public SortedBag<Wikiword> getTags() {
    	return tags();
    }

    public void clearTags() {
    	for (Wikiword w : tags()) {
    		w.removeRelated(this);
    	}
    	tags().clear();
    }
    
    // *************************
    // Definition
    // *************************
    
    /** Gets the definition of an element */
    public Text getDefinition() {
        return this.definition().getCurrent();
    }

    /** Sets the definition of an element */
    public void setDefinition(Text definition) {
        this.definition().setText(definition);
        
    	if (definition.getWhoPosted() != null 
    	&& definition.getWhoPosted() instanceof User) {
    		escortWord().add((User)definition.getWhoPosted());
    	}
    }
    
    /**
     * Returns the History in descending mode. 
     * Index = 0 is the most actual text. 
     * @param index
     */
    public Text getHistory(int index) {
        return definition().getHistory(index);
    }
    
    /** Returns the number of definitions already posted to this Element **/
    public int getHistoryCount() {
        return definition().getHistoryCount();
    }
    
    /** if this element has a definition */
    public boolean hasDefinition() {
        return definition().hasCurrent(); 
    }

    public Date getLastDefinitionDate() {
        if (!hasDefinition())
            return null;
        return getDefinition().getPostDate();
    }

    public Date getFirstDefinitionDate() {
        if (!hasDefinition())
            return null;
        return getHistory(getHistoryCount()-1).getPostDate();
    }
    
    public String getLastDefinitionUser() {
        if (!hasDefinition())
            return null;
        return getDefinition().getWhoPosted().getIdentifier();
    }
    
    /** Returns true if this Wikiword has a definition after the date. */
    public boolean hasLastDefinitionDateAfter(Date date)  {
        if (!hasDefinition()) return false;
        if (!getLastDefinitionDate().before(date)) {
            return true;
        }
        return false;
    }
    
    public boolean hasFirstDefinitionDateAfter(Date date)  {
        if (!hasDefinition()) return false;
        if (!getFirstDefinitionDate().before(date)) {
            return true;
        }
        return false;
    }    
    
    public List<Text> removeDefinitions(Date since, String byUser) {
        if (!hasDefinition()) return null;
        List<Text> list = new ArrayList<Text>();
        
        for (Text comm : comments()) {
        	if (!since.after(comm.getPostDate())) {
        		if (byUser.equals(comm.getWhoPosted().getIdentifier())) {
        			list.add(comm);
        		}
        	}
        }
        
        for (Text comm : list) {
       		comments().remove(comm);
        }
        
        list.addAll(definition().removeTextByUser(since, byUser));
        
        return list;
    }
    
    // *************************
    // Copy and Clean: Rename
    // *************************    
    public void copy(Wikiword w) {
    	definition = w.definition.clone(this);
    	comments().addAll(w.comments());
    	caseSensitive = w.caseSensitive;
    	visibility = w.visibility;
    	escortWord().addAll(w.escortWord());
    	tags = w.tags;
    	
    	relateAll();
    }
    
    public void clean() {
    	cleanRelated();
    	
    	definition = null;
    	caseSensitive = DEFAULT_CASE_SENSITIVE_VALUE;
    	comments = null;
    	visibility = DEFAULT_VISIBILITY;
    	escortWord = null;
    	tags = null;
    }
    
    // *************************
    // Case Sensitive
    // *************************  
    
	private boolean caseSensitive() {
		if (caseSensitive == null) {
			caseSensitive = DEFAULT_CASE_SENSITIVE_VALUE;
		}
		return caseSensitive;
	}
	
    public boolean isCaseSensitive() {
        return caseSensitive();
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
    
    // *************************
    // Visibility
    // *************************    

	public Visibility getVisibility() {
		if (visibility == null) {
			visibility = DEFAULT_VISIBILITY;
		}
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
    
	public boolean isPublic() { return getVisibility() == Visibility.Public; }
	public boolean isToUsers() { return getVisibility() == Visibility.User; }
	public boolean isToReaders() { return getVisibility() == Visibility.Reader; }
	public boolean isToEditors() { return getVisibility() == Visibility.Editor; }
	public boolean isToAdmins() { return getVisibility() == Visibility.Admin; }

	public int compareTo(Wikiword arg0) {
		if (arg0 == null) return -1;
		return getKeyword().compareTo(arg0.getKeyword());
	}
}
