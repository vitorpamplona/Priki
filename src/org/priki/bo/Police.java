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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Police implements Serializable{
    
    public static final long serialVersionUID= 1L;
    
    private int badLinkLimit;
    private Set<String> badLinks;
    private Map<String, Date> badUsers;
    
    public Police(int badLinkLimit) {
        this.badLinkLimit = badLinkLimit;
    }
    
    private Set<String> badLinks() {
        if (badLinks == null)
            badLinks = new HashSet<String>();
        return badLinks;
    }
    
    private Map<String, Date> badUsers() {
        if (badUsers == null)
            badUsers = new HashMap<String, Date>();
        return badUsers;
    }
        
    private void setBadUsers(Text text) {
        if (!text.isAnyone()) {
            badUsers().put(text.getPostUser(), text.getPostDate());
        }
    }
    
    public void recompileBadLinks() {
    	Set<String> badLinksSaved = badLinks;
    	badLinks = new HashSet<String>();
    	
    	for (String std : badLinksSaved) {
    		badLinks().add(onlyTheDomain(std));
    	}
    }
    
    private String onlyTheDomain(String std) {
    	if (std.indexOf("/", 9) >= 0) {
    		return std.substring(0, std.indexOf("/", 9));
     	} else {
     		return std;
     	}
    }
    
    private void setBadLinks(Text text) {
        for (Element e : text) {
            if (e.isALink()) {
            	// only the domain
            	badLinks().add(onlyTheDomain(e.getKeyword()));
            }
        }
    }
    
    /** Sets this text as BAD! 
     **/
    public void setBadText(Text text) {
        setBadLinks(text);
        setBadUsers(text);
    }
    
    public boolean isBadUser(Text text) {
        if (!text.isAnyone()) {
            return isBadUser(text.getPostUser());
        }
        return false;
    }

    public boolean isBadUser(String ip) {
        return badUsers().containsKey(ip);
    }
    
    public boolean isBadLink(String text) {
        return badLinks().contains(text);
    }
    
    public boolean hasBadLink(Text text) {
        int badLinksCount = 0;
        for (Element e : text) {
            if (e.isALink() && isBadLink(onlyTheDomain(e.getKeyword()))) {
                badLinksCount++;
            }
        }
        return badLinksCount > badLinkLimit;
    }
    
    /** 
     * If there are more than 5 elements and more than 60% of a text with links, returns true.
     */ 
    public boolean hasToMuchLinks(Text text) {
        if (text.getElementCount() <= 5) return false;
        
        int badLinksCount = 0;
        int wordsCount = 0;
        for (Element e : text) {
            if (e.isALink()) {
                badLinksCount++;
            }
            if (e instanceof Wikiword) {
                wordsCount++;
            }
        }  
        
        float percent = 100f * badLinksCount / (wordsCount + badLinksCount);
        return percent > 60;
    }
    
    /** returns true if the Text is valid **/
    public boolean isBadText(Text text) {       
        if (isBadUser(text) || hasBadLink(text) || hasToMuchLinks(text)) {
            setBadText(text);
            return true;
        }
        return false;
    }
    
    public void setGoodLink(String link) {
        badLinks().remove(onlyTheDomain(link));
    }
    
    public void setGoodUser(String ip) {
        badUsers().remove(ip);
    }
    
    public void setBadUser(String ip) {
        badUsers().put(ip, new Date());
    }

    public void setBadTexts(List<Text> bads) {
        for (Text text : bads) {
            setBadText(text); 
        }
    }

    public void clear() {
        badUsers().clear();
        badLinks().clear();
    }

    public List<String> getBadUsers() {
        return new ArrayList<String>(badUsers().keySet());
    }
    
    public List<String> getBadLinks() {
        return new ArrayList<String>(badLinks());
    }
}
