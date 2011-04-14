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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Basic class for all content. Text is a list of Elements. This simulates
 * a String class. 
 *
 * @author <a href="mailto:vitor@babaxp.org">Vitor Fernando Pamplona</a>
 *
 * @since 31/05/2005
 * @version $Id: Text.java,v 1.1 2005/07/21 16:04:17 vfpamp Exp $
 */
public class Text implements Iterable<Element> , Serializable {

    public static final long serialVersionUID= 1L;
    
    public static final String ANYONE = "anyone";
    
    private List<Element> text = new ArrayList<Element>();
    
    private Date postDate = new Date();

    private Someone whoPosted; 

    public Text() {
    	this.whoPosted = new AnonymousUser(ANYONE);
    }
    
    public Text(String ip) {
        this.whoPosted = new AnonymousUser(ip);
    }

    public Text(Someone user) {
        this.whoPosted = user;
    }
    
    public Text(String ip, Date postDate) {
        this.whoPosted = new AnonymousUser(ip);
        this.postDate = postDate;
    }

    public Text(Someone user, Date postDate) {
        this.whoPosted = user;
        this.postDate = postDate;
    }
    
    private List<Element> text() {
        if (text == null) 
            text = new ArrayList<Element>();
            
        return text;
    }
  
    /** needed bu foreach **/ 
    public Iterator<Element> iterator() {
        return text().iterator();
    }
    
    /**
     * Adds ane element to text
     * @param element element to add. 
     */
    public void add(Element element) {
        text().add(element);
    }
    
    public boolean has(Element element) {
    	for (Element e: text()) {
    		if (e.getKeyword().equals(element.getKeyword())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /** Returns the total number of elements in this text 
     * @return Element count */
    public int getElementCount() {
        return text().size();
    }

    /** Returns an existing element in this class 
     * @param index The index in the element list.  
     * @return The Element to key.*/
    public Element getElement(int index) {
        return text().get(index);
    }
    
    public Date getPostDate() {
        return this.postDate;
    }

    public boolean isAnyone() {
        return this.whoPosted.getIdentifier().equals(ANYONE);
    }

    
    
    @Deprecated
    private String postUser;    
    
    @Deprecated
    public String getPostUser() {
    	if (whoPosted==null) whoPosted = new AnonymousUser(postUser);
        return this.whoPosted.getIdentifier();
    }

    @Deprecated
    public boolean isAfter(Date since, String byUser) {
    	if (whoPosted==null) whoPosted = new AnonymousUser(postUser);
        return this.whoPosted.getIdentifier().equalsIgnoreCase(byUser) && !postDate.before(since);
    }
    
    public String toString() {
    	if (whoPosted==null) whoPosted = new AnonymousUser(postUser);
        return this.whoPosted.getIdentifier() + " - " + this.postDate;
    }

	public Someone getWhoPosted() {
		if (whoPosted==null) whoPosted = new AnonymousUser(postUser);
		return whoPosted;
	}

	public void setWhoPosted(Someone whoPosted) {
		this.whoPosted = whoPosted;
	}
	
	/** Check if the text is only a link. Priki may redirect this post. */
	public boolean isOnlyALink() {
		int count=0;
		for (Element e : text) {
			if (e instanceof Wikiword)
				return false;
			if (e.isALink())
				count ++;
		}
		return count == 1;
	}
	
	/** Check  if the text is only a link and return the link or null if it isn't. Priki may redirect this post. */
	public String getOnlyALink() {
		String link;
		for (Element e : text) {
			if (e instanceof Wikiword)
				return null;
			if (e.isALink())
				return e.getKeyword();
		}
		return null;
	}


}
