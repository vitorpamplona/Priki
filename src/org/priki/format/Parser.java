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
package org.priki.format;

import java.util.List;

import org.priki.bo.Someone;
import org.priki.bo.Text;
import org.priki.bo.Wikiword;

public interface Parser {
    
    /** 
     * Parses a text string into a Text class, separating the wikiwords and elements.   
     * @param textToParse The string to transform in a Text object. 
     * @param text The text object to be filled
     */
    public void parseText(String textToParse, Text text);

    /** 
     * Parses a text string into a Text class, separating the wikiwords and elements.   
     * @param textToParse The string to transform in a Text object. 
     * @returns The text object filled with the string. 
     */
    public Text parseText(String textToParse, Someone user);

    /**
     * Returns all existing wikiwords in a text.  
     * Can cause parse exception;
     * @param textToParse the text
     * @return A list of Wikiwords
     */
    public List<Wikiword> getWikiwords(String textToParse);

    /**
     * Returns all single wikiwords in a text.<br>
     * For example:<br>
     * The text "Giovane Roslindo Kuhn" will return "Giovane", "Roslindo",  "Kuhn",
     * even if exists a "Giovane Roslindo" wikiword.<br>  
     * Can cause parse exception;
     * @param textToParse the text
     * @return A list of words
     */
    public List<Wikiword> splitWikiword(String textToParse);
    
    /**
     * Returns all existing wikiwords in a text.  
     * @param textToParse the text
     * @return A list of Wikiwords
     */
    public List<Wikiword> getOnlyWikiwords(String textToParse);
}
