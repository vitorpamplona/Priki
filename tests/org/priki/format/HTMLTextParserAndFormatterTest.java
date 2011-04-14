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

import junit.framework.TestCase;

import org.priki.bo.AnonymousUser;
import org.priki.bo.Text;
import org.priki.bo.Wiki;

public class HTMLTextParserAndFormatterTest extends TestCase {

    private void test(String sentence) {
    	AnonymousUser vitor = new AnonymousUser("Vitor");
        Text text = new HtmlParser(new Wiki()).parseText(sentence, vitor);
        assertEquals(sentence, new HtmlFormatter().format(text));
    }
    
    public void testByFields() throws IllegalArgumentException, IllegalAccessException {
        List<String> lista = new ArrayList<String>();

        lista.add("The book is on the table");
        lista.add("The book... is on the tabl & e.");
        lista.add("The - book... is on 4 the table.");
        lista.add("The <B> book... </B> is on 4 the table.");
        lista.add("The <B> book... </B> is on 4 &nbsp; the table.");
        
        for (String f : lista) {
            test(f);            
        }
    }
    
}
