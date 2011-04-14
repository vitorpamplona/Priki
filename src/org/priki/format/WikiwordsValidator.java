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
import java.util.Collection;

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
public class WikiwordsValidator {

    private Wiki wiki;

    public WikiwordsValidator(Wiki wiki) {
        this.wiki = wiki;
    }

    /**
     * Detects a non-wikiword in the text and returns false. If there aren't non-wikiwords elements returns true.
     * @param keyword The string to analyse. 
     * @return true or false.
     */
    public boolean matchWikiwords(String text) {
        try {
            Text result = new Text();

            new HtmlTokenParser(wiki, text).parseStringInReadOnlyMode(result);
            for (int i = 0; i < result.getElementCount(); i++) {
                if (!(result.getElement(i) instanceof Wikiword)) {
                    return false;
                }
            }
            return true;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns conflicting wikiwords for the informed keyword and case senstiviness.<br>
     * @param keyword Keyword to be validated.<br>
     * @param caseSensitive Case sensitive of the wikiword to be validated.
     * @return {@link Collection} of conflicting {@link Wikiword},
     *          if any wikiword is conflicting returns an empty collection.
     */
    public Collection<Wikiword> getConflictingWikiwords(String keyword, boolean caseSensitive) {

        // check equals wikiwords ignoring case sensitive
        Collection<Wikiword> words = wiki.getWikiwordsIgnoreCase(keyword);
        Collection<Wikiword> ret = new ArrayList<Wikiword>();
        for (Wikiword wikiword : words) {

            // it has same keyword, its ok
            if (wikiword.getKeyword().equals(keyword)) {
                continue;
            }
                        
            // it have not definition and could be replaced, its ok
            if (wikiword.getDefinition() == null) {
                continue;
            }

            // both are case sensitive, its ok
            if (caseSensitive && wikiword.isCaseSensitive()) {
                continue;
            }
            
            // conflicting wikiword
            ret.add(wikiword);
        }
        return ret;
    }
}
