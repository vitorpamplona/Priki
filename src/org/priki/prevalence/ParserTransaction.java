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
package org.priki.prevalence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.priki.bo.Text;
import org.priki.bo.User;
import org.priki.bo.Wiki;
import org.priki.bo.Wikiword;
import org.priki.format.FormatPrevalenceFactory;
import org.priki.format.Formatter;
import org.priki.format.Parser;
import org.priki.prevalence.exceptions.ConflictingWikiwordsException;
import org.priki.prevalence.exceptions.PoliceException;
import org.priki.utils.StringUtils;


public class ParserTransaction extends PrikiTransaction {

    public static final long serialVersionUID = 1L;

    private String user;
    private Date postDate = new Date();
    private String wikiword;
    private String text;
    private boolean caseSensitive;
    private Wikiword.Visibility visibility;
    private String newWikiword;
    private List<String> tags;
    
    private Formatter wikiFormatter;
    private Parser wikiParser;
    
    public ParserTransaction(String user, String wikiword, String text, boolean caseSensitive) {
        this.user = user;
        this.wikiword = wikiword;
        this.text = text;
        this.caseSensitive = caseSensitive;
        tags = new ArrayList<String>();
    }
    
    public ParserTransaction(String user, String wikiword, String text, boolean caseSensitive, Wikiword.Visibility vis) {
        this.user = user;
        this.wikiword = wikiword;
        this.text = text;
        this.caseSensitive = caseSensitive;
        this.visibility = vis;
        tags = new ArrayList<String>();
    }
    
    public ParserTransaction(String user, String wikiword, String newWikiword, String text, boolean caseSensitive, Wikiword.Visibility vis, List<String> tags) {
        this.user = user;
        this.wikiword = wikiword;
        this.text = text;
        this.caseSensitive = caseSensitive;
        this.visibility = vis;
        this.newWikiword = newWikiword;
        this.tags = tags;
    }    

    public Parser getParser(Wiki wiki) {
    	if (wikiParser == null)
    		wikiParser = FormatPrevalenceFactory.createDefaultParser(wiki);
        return wikiParser;
    }

    public Formatter getFormatter(Wiki wiki) {
    	if (wikiFormatter == null)
    		wikiFormatter = FormatPrevalenceFactory.createDefaultFormatter(wiki);
        return wikiFormatter;
    }

    public void checkCanPost(Wiki wiki, String user, Wikiword page) {
    	 if (!wiki.getAdmin().getAccessManager().iCanPost(user, page)) { 
    	     throw new PoliceException();
         }
    }
    
    public Text textFactory(Wiki wiki, String userByString, Date postDate) {
    	User reggisteredUser = wiki.getAdmin().getAccessManager().getUser(user);
        
        if (reggisteredUser==null)
        	return new Text(userByString, postDate);
        else 
        	return new Text(reggisteredUser, postDate);
    }
    
    public void executeOn(Wiki wiki) {
    	// INSERT
    	if (StringUtils.isNullAndBlank(wikiword)) {
    		wikiword = newWikiword;
    	}
    	// Using first Constructor
    	if (StringUtils.isNullAndBlank(newWikiword)) {
    		newWikiword = wikiword;
    	}

    	Wikiword wOriginal = wiki.getWikiword(wikiword);
    	checkCanPost(wiki, user, wOriginal);
    	checkCanPost(wiki, user, wiki.getWikiword(newWikiword));    	
    	
    	// rename first.
    	if (wOriginal != null && !wikiword.equals(newWikiword)) {
    		// Rename.
   			wiki.rename(wikiword, newWikiword);
    	}
    	
   		executeSimple(wiki, newWikiword);
    }

    private void executeSimple(Wiki wiki, String wikiword) {
    	Parser parser = getParser(wiki);

        // Converts a simple wikiword in a composite wikiword.
        List<Wikiword> wikiwordsOfTitle = parser.getWikiwords(wikiword);
                
        // Creating the text and identifyng wiki words.
        Text textObject = textFactory(wiki, user, postDate);
        parser.parseText(text, textObject);
        
        if (!wiki.getPolice().isBadText(textObject)) {

        	Wikiword e = wiki.newWikiword(wikiword);
            e.setCaseSensitive(caseSensitive);
            e.setVisibility(visibility);
            
            // check Case Sensitive Conflicts
            checkCaseSensitiveConflicts(wiki, e);            
            
            e.clearTags();
            for (String tag : tags) {
            	e.addTag(wiki.newWikiword(tag));
            }
            
            // Hold the wikiwords to be recompiled
            Set<Wikiword> toRecompile = new HashSet<Wikiword>();
            toRecompile.addAll(handleCompositeWikiword(wiki, e, wikiwordsOfTitle));
            toRecompile.addAll(handleCaseInsensitiveWikiword(wiki, e));

            // Definition must be the last updating
            
            // Creating the text and identifyng wiki words. 
            textObject = textFactory(wiki, user, postDate);
            parser.parseText(text, textObject);
        	e.setDefinition(textObject);
        	
            // Recompile the old wikiwords
            for (Wikiword word : toRecompile) {
           		recompileWikiwordAndComments(wiki,word);
            }
            
            // clean unreferred wikiwords
            wiki.cleanUnusedWikiwords(); 
        }
    }
    
    private Text recompileText(Wiki wiki, Text text) {
    	String temp = getFormatter(wiki).formatWithoutLinks(text);
    	Text otherText = new Text(text.getWhoPosted(), text.getPostDate());
        getParser(wiki).parseText(temp, otherText);
        return otherText;
    }
    
    private void recompileWikiwordAndComments(Wiki wiki,Wikiword word) {
    	if (word.hasDefinition())
    		word.setDefinition(recompileText(wiki, word.getDefinition()));
        
        List<Text> newComments = new ArrayList<Text>();
        for (Text comm : word.getComments()) {
        	if (comm != null && comm.getElementCount() > 0)
        		newComments.add(recompileText(wiki, comm));
        }
        
        word.clearComments();
        for (Text comm : newComments) {
        	word.addComment(comm);
        }        
        
    }
    
    /**
     * Handle the situation where an insensitive wikiword is posted.<br>
     * Old undefinied wikiwords with same keyword are replaced by the new one.<br>
     * 
     * @param wiki Wiki
     * @param word Wikiword
     * @return Set of wikiwords to be recompiled.
     */
    private Set<Wikiword> handleCaseInsensitiveWikiword(Wiki wiki, Wikiword word) {

        // Ensure that it's insensitive
        if (word.isCaseSensitive()) {
            return Collections.emptySet();
        }

        Set<Wikiword> toRecompile = new HashSet<Wikiword>();
        Collection<Wikiword> starts = wiki.getWikiwordsIgnoreCase(word.getKeyword());
        for (Wikiword old : starts) {

            // Itself, OK
            if (word == old) {
                continue;
            }

            // All related wikiwords to the old one, must be recompiled
            toRecompile.addAll(old.getRelated());
            wiki.removeWikiword(old);
        }
        return toRecompile;
    }

    /** 
     * Refresh wikiwords used in a composite wikiword. 
     * <br><br>
     *   If there are a text with this elements: "Vitor" "Fernando" "Pamplona"<br>
     *   And a new wikiword is putted into the system: "Vitor Fernando Pamplona"<br>
     *   This method will search the texts that are using "Vitor" "Fernando" "Pamplona" elements
     *    and replaces it to the composite wikiword: "Vitor Fernando Pamplona" <br>
     *   
     * @param wiki Wiki
     * @param word Wikiword
     * @param keyword List of keywords used in a composite keyword.
     * @return Set of wikiwords to be recompiled
     */
    private Set<Wikiword> handleCompositeWikiword(Wiki wiki, Wikiword word, List<Wikiword> keyword) {
        Set<Wikiword> toRecompile = new HashSet<Wikiword>();

        // Handle composite wikiword
        if (keyword.size() > 1) {

            // Related texts using the first word, must be recompiled
            if (word.isCaseSensitive()) {
                toRecompile.addAll(keyword.get(0).getRelated());
            } else {
                Collection<Wikiword> equals = wiki.getWikiwordsIgnoreCase(keyword.get(0).getKeyword());
                for (Wikiword old : equals) {
                    toRecompile.addAll(old.getRelated());
                }
            }
        }

        // Insensitive wikiword has an especial case
        // When wikiword "design patterns" is posted again and changed to insensitive,
        // and already exists a wikiword "Design", 
        // the texts using this wikiword must be recompiled
        if (!word.isCaseSensitive()) {
            List<Wikiword> words = getParser(wiki).splitWikiword(word.getKeyword());
            if (words.size() > keyword.size()) {
                Collection<Wikiword> equals = wiki.getWikiwordsIgnoreCase(words.get(0).getKeyword());
                for (Wikiword old : equals) {
                    toRecompile.addAll(old.getRelated());
                }
            }
        }
        return toRecompile;
    }

    /**
     * Check if the wikiword conflicts with another wikiword.<br>
     * Insensitive wikiword conflicts with any defined wikiword with same keyword.<br>
     * Sensitive wikiword conflicts with any insensitive wikiword with same keyword.<br>
     * 
     * @param wiki Wiki
     * @param word Wikiword
     * @throws @{@link ConflictingWikiwordsException} 
     */
    private static void checkCaseSensitiveConflicts(Wiki wiki, Wikiword word) {

        Collection<Wikiword> equals = wiki.getWikiwordsIgnoreCase(word.getKeyword());
        for (Wikiword old : equals) {

            // Itself, OK
            if (word == old) {
                continue;
            }

            // There is no way to conflict two sensitive wikiwords
            if (word.isCaseSensitive() && old.isCaseSensitive()) {
                continue;
            }

            // Insensitive doest'n conflict with undefined wikiword
            if (old.getDefinition() == null) {
                continue;
            }

            throw new ConflictingWikiwordsException(old, word);
        }
    }
}
