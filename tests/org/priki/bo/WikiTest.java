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

public class WikiTest extends TestCase {

    Wiki all;

    protected void setUp() throws Exception {
        super.setUp();

        all = new Wiki();
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        all = null;
    }

    public void testElement() {
        assertEquals(0, all.getElementCount());

        Element e = all.newElement("Vitor");
        all.newElement("Paulo");

        elementTestHelper("Vitor");
        elementTestHelper("Paulo");

        assertEquals(2, all.getElementCount());
        assertEquals(0, all.getWikiwordCount());
        assertEquals("Vitor", all.getElements().get(1));
        assertEquals("Paulo", all.getElements().get(0));

        assertTrue(all.hasElement("Vitor"));
        assertFalse(all.hasElement("vitor"));

        all.removeElement(e);

        assertNull(all.getWikiword("Vitor"));
        assertEquals(1, all.getElementCount());
        assertEquals(0, all.getWikiwordCount());
    }

    public void testWikiword() {
        assertEquals(0, all.getWikiwordCount());

        Wikiword e = all.newWikiword("Vitor");
        all.newWikiword("Paulo");

        wikiwordTestHelper("Vitor");
        wikiwordTestHelper("Paulo");

        assertEquals(2, all.getWikiwordCount());
        assertEquals(0, all.getElementCount());
        assertEquals("Vitor", all.getWikiwords().get(1));
        assertEquals("Paulo", all.getWikiwords().get(0));

        assertTrue(all.hasWikiword("Vitor"));
        assertFalse(all.hasWikiword("vitor"));

        all.removeWikiword(e);

        assertNull(all.getWikiword("Vitor"));
        assertEquals(1, all.getWikiwordCount());
        assertEquals(0, all.getElementCount());
    }

    public void elementTestHelper(String name) {
        assertEquals(name, all.getElement(name).getKeyword());
    }

    public void wikiwordTestHelper(String name) {
        assertEquals(name, all.getWikiword(name).getKeyword());
        assertNull(name, all.getWikiword(name).getDefinition());
    }

    public void testRollBack() {
        Wikiword e = all.newWikiword("Vitor");
        Wikiword e1 = all.newWikiword("Vitor");
        Wikiword e2 = all.newWikiword("vai");
        Wikiword e3 = all.newWikiword("pescar");

        Text text1 = new Text("User1");
        text1.add(e1);
        text1.add(e2);
        text1.add(e3);

        //Thread.sleep(1500);

        Text text2 = new Text("User2");
        text2.add(e2);
        text2.add(e1);
        text2.add(e3);

        //Thread.sleep(1500);

        Text text3 = new Text("User3");
        text3.add(e1);
        text3.add(e2);
        text3.add(e3);

        Text text4 = new Text("User3");
        text4.add(e2);
        text4.add(e1);
        text4.add(e3);
        text4.add(e1);
        text4.add(e1);

        e.setDefinition(text1);
        e.setDefinition(text2);
        e.setDefinition(text3);

        e.addComment(text2);
        e.addComment(text4);

        all.setBadUser(text3.getPostDate(), "User3");
        assertTrue(all.getPolice().isBadUser("User3"));

        assertEquals(text2, e.getDefinition());
        assertEquals(1, e.getComments().size());

    }
}
