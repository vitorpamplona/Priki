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

import java.util.ArrayList;
import java.util.List;

import org.priki.bo.Someone;
import org.priki.bo.Text;
import org.priki.bo.Wiki;
import org.priki.bo.Wikiword;
import org.priki.format.compiler.HtmlTokenParser;
import org.priki.format.compiler.ParseException;

/** 
 * 
 * Parse a String to a Text object.  Iternamente armazena BB code. 
 *
 * @author <a href="mailto:vitor@babaxp.org">Vitor Fernando Pamplona</a>
 *
 * @since 14/10/2005
 * @version $Id: $
 */
public class HtmlParser implements Parser {

    private Wiki wiki;

    public HtmlParser(Wiki wiki) {
        this.wiki = wiki;
    }

    public Text parseText(String textToParse, Someone user) {
        try {
            Text text = new Text(user);
            new HtmlTokenParser(wiki, textToParse).parseString(text);
            return text;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseText(String textToParse, Text text) {
        try {
            new HtmlTokenParser(wiki, textToParse).parseString(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Wikiword> getWikiwords(String textToParse) {
        List<Wikiword> list = new ArrayList<Wikiword>();
        try {
            new HtmlTokenParser(wiki, textToParse).getWikiwords(list);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Wikiword> splitWikiword(String textToParse) {
        List<Wikiword> list = new ArrayList<Wikiword>();
        try {
            new HtmlTokenParser(wiki, textToParse).splitWikiword(list);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
    public List<Wikiword> getOnlyWikiwords(String textToParse) {
        try {
            Text result = new Text();
           
            List<Wikiword> ret = new ArrayList<Wikiword>();
            
            new HtmlTokenParser(wiki, textToParse).parseStringInReadOnlyMode(result);
            for (int i=0; i<result.getElementCount(); i++) {
                if (result.getElement(i) instanceof Wikiword) {
                        ret.add((Wikiword)result.getElement(i));
                }
            }
            return ret;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
