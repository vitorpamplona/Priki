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

import junit.framework.TestCase;

public class WikiwordElementTest extends TestCase {

    private Wikiword e;
    private Text text;
    private String keyword = "Vitor";
    
    public void setUp() {
        e = new Wikiword(keyword);
        text = new Text();
        text.add(e);
        
        e.setDefinition(text);
    }
    
    /*
     * Test method for 'org.priki.bo.Wikiword.getDefinition()'
     */
    public void testGetDefinition() {
        assertSame(text, e.getDefinition());
        assertTrue(e.hasDefinition());
    }

    /*
     * Test method for 'org.priki.bo.Wikiword.getKeyword()'
     */
    public void testGetKeyword() {
        assertSame(keyword, e.getKeyword());
    }

    /*
     * Test method for 'org.priki.bo.Wikiword.toString()'
     */
    public void testToString() {
        assertEquals(keyword, e.toString());
    }

    public void testAddRemoveRelated(){
        Wikiword vitor = new Wikiword("Vitor");
        
        Wikiword um = new Wikiword("Um");
        Wikiword cara = new Wikiword("cara");
        Wikiword legal = new Wikiword("legal");
        Element ponto = new Element(".");
        
        Text vitorText = new Text();
        vitorText.add(um);
        vitorText.add(cara);
        vitorText.add(legal);
        vitorText.add(ponto);
        
        vitor.setDefinition(vitorText);

        assertEquals(vitor, um.getRelated().iterator().next());
        assertEquals(vitor, cara.getRelated().iterator().next());
        assertEquals(vitor, legal.getRelated().iterator().next());
        
        Text vitorText2 = new Text();
        vitorText2.add(um);
        vitorText2.add(ponto);
        vitor.setDefinition(vitorText2);
        
        assertEquals(vitor, um.getRelated().iterator().next());
        assertEquals(1, um.getRelated().size());
        assertEquals(0, cara.getRelated().size());
        assertEquals(0, legal.getRelated().size());
    }

    public void testCaseSensitive() {
        // default case sensitive
        assertTrue(e.isCaseSensitive());

        // non case sensitive
        Wikiword other = new Wikiword("Teste");
        other.setCaseSensitive(false);
        assertFalse(other.isCaseSensitive());
    }

}
