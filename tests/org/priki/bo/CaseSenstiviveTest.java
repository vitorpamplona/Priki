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

public class CaseSenstiviveTest extends TestCase {

    Wiki all;

    protected void setUp() throws Exception {
        super.setUp();

        all = new Wiki();
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        all = null;
    }


    public void testWikiwordsIgnoreCase() {
        assertEquals(0, all.getWikiwordCount());
        assertEquals(0, all.getWikiwordsIgnoreCase("GIOVANE").size());

        all.newWikiword("Giovane");
        all.newWikiword("GIOVANE");
        all.newWikiword("giovane");
        all.newWikiword("GiOvAnE");
        all.newWikiword("Giorgio");
        all.newWikiword("Giorgia");

        assertEquals(6, all.getWikiwordCount());
        assertEquals(4, all.getWikiwordsIgnoreCase("GIOVANE").size());
    }

    public void testWikiwordIgnoreCase() {
        assertEquals(0, all.getWikiwordCount());

        Wikiword word = all.newWikiword("Giovane");
        word.setCaseSensitive(false);
        all.newWikiword("Giorgio").setCaseSensitive(true);
        all.newWikiword("Giorgia").setCaseSensitive(true);

        assertSame(word, all.getWikiwordIgnoreCase("giovane"));
        assertNull(all.getWikiwordIgnoreCase("giorgio"));
    }

    public void testHasWikiwordIgnoreCase() {

        // Case sensitive
        Wikiword word = all.newWikiword("Giovane");
        word.setCaseSensitive(true);
        assertTrue(all.hasWikiwordIgnoreCase("Giovane"));
        assertFalse(all.hasWikiwordIgnoreCase("giovane"));
        assertFalse(all.hasWikiwordIgnoreCase("Giorgio"));

        // Case insensitive
        word.setCaseSensitive(false);
        assertTrue(all.hasWikiwordIgnoreCase("Giovane"));
        assertTrue(all.hasWikiwordIgnoreCase("giovane"));
        assertFalse(all.hasWikiwordIgnoreCase("Giorgio"));
    }

    public void elementTestHelper(String name) {
        assertEquals(name, all.getElement(name).getKeyword());
    }

    public void wikiwordTestHelper(String name) {
        assertEquals(name, all.getWikiword(name).getKeyword());
        assertNull(name, all.getWikiword(name).getDefinition());
    }
}
