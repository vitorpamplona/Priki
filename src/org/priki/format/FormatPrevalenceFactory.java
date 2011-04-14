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

import org.priki.bo.Wiki;

public class FormatPrevalenceFactory implements AbstractFormatFactory {
    public static Parser createDefaultParser(Wiki wiki) {
        return new HtmlParser(wiki);
    }
    
    public static Formatter createDefaultFormatter(Wiki wiki) {
        return new HtmlFormatter(wiki.getAdmin().getBasePath());
    }

    public static WikiwordsValidator createDefaultWikiwordsValidator(Wiki wiki) {
        return new WikiwordsValidator(wiki);
    }
    
    public Parser createParser(Wiki wiki) {
        return new HtmlParser(wiki);
    }
    
    public Formatter createFormatter(String path) {
        return new HtmlFormatter(path);
    }
    
    public WikiwordsValidator createWikiwordsValidator(Wiki wiki) {
        return new WikiwordsValidator(wiki);
    }    
}
