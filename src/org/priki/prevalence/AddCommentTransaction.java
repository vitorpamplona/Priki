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

import java.util.Date;

import org.priki.bo.Text;
import org.priki.bo.User;
import org.priki.bo.Wiki;
import org.priki.bo.Wikiword;
import org.priki.format.FormatPrevalenceFactory;
import org.priki.format.Formatter;
import org.priki.format.Parser;
import org.priki.prevalence.exceptions.WikiwordNotFoundException;

public class AddCommentTransaction extends PrikiTransaction {

    public static final long serialVersionUID= 1L;
    
    private String user;
    private Date postDate = new Date();
    private String wikiword; 
    private String text;
    
    public AddCommentTransaction(String user, String wikiword, String text) {
        this.user = user;
        this.wikiword = wikiword;
        this.text = text;
    }
    
    public Parser getParser(Wiki wiki) {
        return FormatPrevalenceFactory.createDefaultParser(wiki);
    }

    public Formatter getFormatter(Wiki wiki) {
        return FormatPrevalenceFactory.createDefaultFormatter(wiki);
    }
    
    public void executeOn(Wiki wiki) {
        Parser parser = getParser(wiki);

        Wikiword word = wiki.getWikiword(wikiword);
        
        if (word ==null) throw new WikiwordNotFoundException();
        
        User reg = wiki.getAdmin().getAccessManager().getUser(user);
        
        // Creating the text and identifyng wiki words.
        Text textObject;
        if (reg==null)
        	textObject = new Text(user, postDate);
        else 
        	textObject = new Text(reg, postDate);
        
        parser.parseText(text, textObject);

        if (!wiki.getPolice().isBadText(textObject)) {
            word.addComment(textObject);
        }
    }
}
