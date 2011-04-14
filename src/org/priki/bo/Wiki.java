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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.priki.utils.SortedBag;
import org.priki.utils.StringMap;
import org.priki.utils.WikiwordIndex;
import org.priki.utils.WikiwordUtils;

/**
 * Class to hold all element intances. The Base of the system.
 * This class is a singleton. 
 *  
 * @author <a href="mailto:vitor@babaxp.org">Vitor Fernando Pamplona</a>
 *
 * @since 14/10/2005
 * @version $Id: $
 */
public class Wiki implements Serializable {

    public static final long serialVersionUID = 1L;

    private StringMap<Element> elements;

    @Deprecated
    private StringMap<Wikiword> words;

    private WikiwordIndex wordsIndex;

    private Admin admin;

    private Police police;

    public Admin getAdmin() {
        if (admin == null) {
            admin = new Admin();
        }
        return admin;
    }

    public Police getPolice() {
        if (police == null) {
            police = new Police(3);
        }
        return police;
    }

    private StringMap<Element> elements() {
        if (elements == null)
            elements = new StringMap<Element>();
        return elements;
    }

    private WikiwordIndex words() {
        if (wordsIndex == null) {
            wordsIndex = new WikiwordIndex();
        }
        if (words != null && words.size() > wordsIndex.size()) {
            // Translate from old index to the new one
            wordsIndex.putAll(words);
        }
        return wordsIndex;
    }

    public int getElementCount() {
        return elements().size();
    }

    public int getWikiwordCount() {
        return words().size();
    }

    public Element getElement(String keyword) {
        return elements().get(keyword);
    }

    public Wikiword getWikiword(String keyword) {
        return words().get(keyword);
    }

    public Wikiword getWikiwordIgnoreCase(String keyword) {
        Collection<Wikiword> wikiwords = getWikiwordsIgnoreCase(keyword);
        for (Wikiword wikiword : wikiwords) {
            if (WikiwordUtils.equalsKeyword(keyword, wikiword)) {
                return wikiword;
            }
        }
        return null;
    }

    public Collection<Wikiword> getWikiwordsIgnoreCase(String keyword) {
        return words().getIgnoreCase(keyword);
    }

    public void rename(String oldWikiword, String newWikiword) {
    	if (oldWikiword.equals(newWikiword)) return; 
    	
    	Wikiword oldW = getWikiword(oldWikiword);
    	Wikiword newW = newWikiword(newWikiword);

    	newW.copy(oldW);
    	oldW.clean();
    }
    
    public boolean hasElement(String keyword) {
        return elements().containsKey(keyword);
    }

    public boolean hasWikiword(String keyword) {
        return words().containsKey(keyword);
    }

    public boolean hasWikiwordIgnoreCase(String keyword) {
        Collection<Wikiword> wikiwords = getWikiwordsIgnoreCase(keyword);
        for (Wikiword wikiword : wikiwords) {
            if (WikiwordUtils.equalsKeyword(keyword, wikiword)) {
                return true;
            }
        }
        return false;
    }

    /** Return an existing or new element in this class. If it doesn't exist, create a new one. */
    public Element newElement(String keyword) {
        Element e = getElement(keyword);

        if (e == null) {
            e = new Element(keyword);
            elements().put(keyword, e);
        }

        return e;
    }

    /** Return an existing or new wikiword in this class. If it doesn't exist, create a new one. */
    public Wikiword newWikiword(String keyword) {
        Wikiword e = getWikiword(keyword);

        if (e == null) {
            e = new Wikiword(keyword);
            words().put(keyword, e);
        }

        return e;
    }
    
    public void removeElement(Element reference) {
        elements().remove(reference.getKeyword());
    }

    public void removeWikiword(Wikiword reference) {
        words().remove(reference.getKeyword());
    }

    /** Clear all */
    public void clear() {
        elements().clear();
        words().clear();
        getAdmin().getAccessManager().clear();
        getPolice().clear();
    }

    public Collection<Wikiword> wordsStartingWith(String keyword) {
        return words().startingWith(keyword);
    }

    public List<String> getElements() {
        return new ArrayList<String>(elements().keySet());
    }

    public List<String> getWikiwords() {
        return new ArrayList<String>(words().keySet());
    }
/*
    public Map<Date, Wikiword> lastChanged() {
        Calendar calLimit = (Calendar) Calendar.getInstance().clone();
        calLimit.add(Calendar.DAY_OF_MONTH, -getAdmin().getLastChangesCount());

        return lastChangedAfter(calLimit.getTime());
    }

    /** returns the last changed elements after the date. 
    public SortedMap<Date, Wikiword> lastChangedAfter(Date date) {
        SortedMap<Date, Wikiword> map = new TreeMap<Date, Wikiword>(Collections.reverseOrder());
        Date temp;

        for (Wikiword e : words().values()) {
        	if (getAdmin().isLastChangesOnlyNewPages()) {
        		if (e.hasFirstDefinitionDateAfter(date)) {
        			map.put(e.getFirstDefinitionDate(), e);
        		}
        	} else {
                if (e.hasLastDefinitionDateAfter(date)) {
                    map.put(e.getLastDefinitionDate(), e);
                } else if ((temp = e.hasCommentAfter(date)) != null) {
                    map.put(temp, e);
                }
        	}
        }
        return map;
    }*/
    
    /** returns the last changed elements after the date. */
    public SortedMap<Date, Wikiword> lastChangedFull(String user) {
        SortedMap<Date, Wikiword> map = new TreeMap<Date, Wikiword>(Collections.reverseOrder());
        

    	if (getAdmin().isLastChangesOnlyNewPages()) {
    		for (Wikiword e : words().values()) {
    			if (e.hasDefinition() && getAdmin().getAccessManager().iCanRead(user, e)) {
        			map.put(e.getFirstDefinitionDate(), e);
        		}
    		}
    	} else {
    		for (Wikiword e : words().values()) {
    			if (e.hasDefinition() && getAdmin().getAccessManager().iCanRead(user, e)) {
    				Date lastDef = e.getLastDefinitionDate();
    				Date lastComm = e.getLastCommentPostDate();
    				
    				Date last;
    				
    				if (lastComm == null)
    					last = lastDef;
    				else
    				    last = lastDef.after(lastComm) ? lastDef : lastComm;
    				
    				map.put(last, e);
    			}
        	}
        }
    
        return map;
    }    
    

    /** returns the last changed elements after the date. */
    public SortedBag<Wikiword> getGlobalTags() {
    	SortedBag<Wikiword> bag = new SortedBag<Wikiword>();

        for (Wikiword e : words().values()) {
        	if (e.hasDefinition()) {
        		bag.addAll(e.getTags());
        	}
        }
        return bag;
    }    

    public SortedMap<Date, Wikiword> changedByUser(String user) {
        SortedMap<Date, Wikiword> map = new TreeMap<Date, Wikiword>();
        Date date;

        for (Wikiword e : words().values()) {
            if (user.equalsIgnoreCase(e.getLastDefinitionUser())) {
                map.put(e.getLastDefinitionDate(), e);
            } else if ((date = e.hasCommentBy(user)) != null) {
                map.put(date, e);
            }
        }

        return map;
    }

    public List<Wikiword> wikiwordsChangedAfter(Date date) {
        List<Wikiword> ret = new ArrayList<Wikiword>();

        for (Wikiword e : words().values()) {
            if (e.hasLastDefinitionDateAfter(date)) {
                ret.add(e);
            }
        }

        return ret;
    }

    public String fixWikiRefers() {
    	String result = "";
    	for (Wikiword w : words().values()) {
    		for (Wikiword related : new ArrayList<Wikiword>(w.getRelated())) {
    			if (!related.hasDefinition()) {
    				w.removeRelated(related);
    				related.clearTags();
    				related.clearComments();
    				result += "\"" + w.getKeyword() + "\" has a related page called \"" + related.getKeyword() + "\", but it does not has any definition. Removing related word.\n";
    			} else if (!related.isTalkingAbout(w)) {
    				w.removeRelated(related);
    				result += "\""+ w.getKeyword() + "\" has a related page called \"" + related.getKeyword() + "\", but it is not used in the current text and it is not cited in tags. Removing related word.\n";
    			}
    		}
    	}
    	return result;
    }
    
    // cleaning unrelated words.
    // called after each post
    public void cleanUnusedWikiwords() {
    	List<Wikiword> toRemove = new ArrayList<Wikiword>();
    	for (Wikiword w : words().values()) {
    		if (!w.isUsed())
    			toRemove.add(w);
    	}
    	for (Wikiword w : toRemove) {
    		words().remove(w.getKeyword());
    	}
    }
    
    /** Search by an element */
    public Collection<Wikiword> search(String keyword) {
        return words().searchByAnythingWith(keyword);
    }

    /** RollBack all posts that were posted after the date, by the user ip 
     *  Mark the removed posts as bad posts in the police. */
    public void setBadUser(Date since, String userIP) {
        List<Wikiword> changes = wikiwordsChangedAfter(since);

        List<Text> bads = new ArrayList<Text>();
        List<Text> temp;
        for (Wikiword changed : changes) {
            temp = changed.removeDefinitions(since, userIP);
            if (temp != null && temp.size() > 0)
                bads.addAll(temp);
        }

        getPolice().setBadTexts(bads);
        getPolice().setBadUser(userIP);
    }
}
