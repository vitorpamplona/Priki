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
import java.util.List;

/**
 * A Text with history.
 *
 * @author <a href="mailto:vitor@babaxp.org">Vitor Fernando Pamplona</a>
 *
 * @since 31/05/2005
 * @version $Id: CommunityText.java,v 1.1 2005/07/21 16:04:16 vfpamp Exp $
 */
public class HistoryText implements Serializable {

    public static final long serialVersionUID= 1L;
    
    /** Holds the history in descending order **/
    private List<Text> history;
    /** Who is this hitory? */
    private Wikiword wikiword;
 
    public HistoryText(Wikiword word) {
        this.wikiword = word;
    }
    
    public HistoryText clone(Wikiword newParent) {
    	HistoryText history = new HistoryText(newParent);
    	history.history().addAll(history());
    	return history;
    }
    
    /** Always returns a intance of history. */ 
    private List<Text> history() {
        if (history == null) {
            history = new ArrayList<Text>();
        }
        return history;
    }
        
    /** Get the actual text. */
    public Text getCurrent() {
        if (history().size() == 0) return null;
        
        return history().get(0);
    }
    
    /** Remove the elements reference from actual text if it exists.  
     *  Puts in position 0 of the history list as the actual text.  */
    public void setText(Text text) {
        if (text == null)
            throw new IllegalArgumentException("Text can't be null");
        
        if (hasCurrent()) {
            disableText(getCurrent());
        }
        
        history().add(0, text);
        
        enableText(getCurrent());
    }
    
    /**
     * Called when a Text is replaced by a better. 
     * Causes remove elements using reference  
     */
    private void disableText(Text text) {
        for (Element e : text) {
            if (e instanceof Wikiword){
                ((Wikiword)e).removeRelated(wikiword);
            }
        }
    }
    
    /**
     * Called when a Text is setted as a definition element. 
     */
    private void enableText(Text text) {
        for (Element e : text) {
            if (e instanceof Wikiword){
                ((Wikiword)e).addRelated(wikiword);
            }
        }
    }
    
    /** returns the record count of this history. */ 
    public int getHistoryCount() {
        if (history().size() == 0) return 0;
        
        return history().size()-1;
    }

    /**
     * Returns the History in descending mode. 
     * Index = 0 is the most actual text. 
     * @param index
     */
    public Text getHistory(int index) {
        return history().get(index+1);
    }

    /** returns if the text has a definition */
    public boolean hasCurrent() {
        return getCurrent() != null;
    }

    /** gets the complete history */
    public List<Text> getHistory() {
        return history().subList(1, history.size()-1);
    }
      
    /**
     * Gets the first Text that isn't posted by te user in param. 
     * @param user User to disconsider. 
     * @return The first text posted by another user.
     */
    public List<Text> removeTextByUser(Date fromDate, String byUser) {
        List<Text> ret = new ArrayList<Text>();
        for (Text text : history()) {
            if (text.isAfter(fromDate, byUser)) {
                ret.add(text);
            }
        }
        history().removeAll(ret);
        return ret;
    }
    
}
