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
 * @author Giovane Roslindo Kuhn - grkuhn at gmail dot com
 *
 */
package org.priki.utils;

import junit.framework.TestCase;

import org.priki.bo.Wikiword;

/**
 * @author Giovane.Kuhn
 * @since 13/02/2007
 */
public class WikiwordIndexTest extends TestCase {

    private static Wikiword newWikiword(String keyword, boolean caseSensitive) {
        Wikiword ret = new Wikiword(keyword);
        ret.setCaseSensitive(caseSensitive);
        return ret;
    }

    public void testPut() {
        WikiwordIndex map = new WikiwordIndex();

        // add wikiwords
        map.put("Giovane", new Wikiword("Giovane"));
        map.put("Priki", new Wikiword("Priki"));
        map.put("Wiki", new Wikiword("Wiki"));

        assertEquals(3, map.size());
    }

    public void testPutAll() {
        WikiwordIndex map = new WikiwordIndex();

        // add wikiwords
        map.put("Giovane", new Wikiword("Giovane"));
        map.put("Priki", new Wikiword("Priki"));
        map.put("Wiki", new Wikiword("Wiki"));
        assertEquals(3, map.size());

        // create new indexes
        WikiwordIndex newMap = new WikiwordIndex();
        newMap.putAll(map);
        assertEquals(3, map.size());
    }

    public void testCaseSensitiveWikiwords() {
        WikiwordIndex map = new WikiwordIndex();

        // add many case sensitive wikiwords
        map.put("giovane", newWikiword("giovane", true));
        map.put("GIOVANE", newWikiword("GIOVANE", true));
        map.put("Giovane", newWikiword("Giovane", true));
        map.put("gIoVaNe", newWikiword("gIoVaNe", true));
        map.put("Giorgia", newWikiword("Giorgia", true));
        map.put("Giorgio", newWikiword("Giorgio", true));
        map.put("Priki", newWikiword("Priki", true));
        map.put("Wiki", newWikiword("Wiki", true));

        assertEquals(8, map.size());
        assertEquals(3, map.startingWith("Gio").size());
    }

    public void testCaseInsensitiveWikiwords() {
        WikiwordIndex map = new WikiwordIndex();

        // add many wikiwords with same case insensitive keywords
        map.put("giovane", newWikiword("giovane", false));
        map.put("GIOVANE", newWikiword("GIOVANE", false));
        map.put("Giovane", newWikiword("Giovane", false));
        map.put("gIoVaNe", newWikiword("gIoVaNe", false));
        map.put("Giorgia", newWikiword("Giorgia", false));
        map.put("Giorgio", newWikiword("Giorgio", false));
        map.put("Priki", newWikiword("Priki", false));
        map.put("Wiki", newWikiword("Wiki", false));

        assertEquals(8, map.size());
        assertEquals(6, map.startingWith("GIO").size());
    }

    public void testGetIgnoreCase() {
        WikiwordIndex map = new WikiwordIndex();
        assertEquals(0, map.getIgnoreCase("GIOVANE").size());

        // add many wikiwords with same case insensitive keywords
        map.put("giovane", newWikiword("giovane", false));
        map.put("GIOVANE", newWikiword("GIOVANE", false));
        map.put("Giovane", newWikiword("Giovane", false));
        map.put("gIoVaNe", newWikiword("gIoVaNe", false));
        map.put("Giorgia", newWikiword("Giorgia", false));
        map.put("Giorgio", newWikiword("Giorgio", false));
        map.put("Priki", newWikiword("Priki", false));
        map.put("Wiki", newWikiword("Wiki", false));

        assertEquals(8, map.size());
        assertEquals(4, map.getIgnoreCase("GIOVANE").size());
    }

    public void testRemove() {
        WikiwordIndex map = new WikiwordIndex();

        // add wikiwords
        map.put("Giovane", new Wikiword("Giovane"));
        map.put("Priki", new Wikiword("Priki"));
        map.put("Wiki", new Wikiword("Wiki"));
        assertEquals(3, map.size());
        assertEquals(1, map.startingWith("Gio").size());

        // remove wikiword
        map.remove("Giovane");
        assertEquals(2, map.size());
        assertEquals(0, map.startingWith("Gio").size());
    }
    
    public void testClear() {
        WikiwordIndex map = new WikiwordIndex();

        // add wikiwords
        map.put("Giovane", new Wikiword("Giovane"));
        map.put("Priki", new Wikiword("Priki"));
        map.put("Wiki", new Wikiword("Wiki"));
        assertEquals(3, map.size());
        assertEquals(1, map.startingWith("Gio").size());

        // clear
        map.clear();
        assertEquals(0, map.size());
        assertEquals(0, map.startingWith("Gio").size());
    }
    
    public void testClone() {
        WikiwordIndex map = new WikiwordIndex();

        // Add wikiwords
        map.put("Giovane", new Wikiword("Giovane"));
        map.put("Priki", new Wikiword("Priki"));
        assertEquals(2, map.size());
        assertEquals(1, map.startingWith("Gio").size());

        // Check clone
        WikiwordIndex clone = (WikiwordIndex) map.clone();
        assertEquals(2, clone.size());
        assertEquals(1, clone.startingWith("Gio").size());
        
        // Check deep copy
        map.put("Giorgio", new Wikiword("Giorgio"));
        assertEquals(3, map.size());
        assertEquals(2, map.startingWith("Gio").size());
        assertEquals(2, clone.size());
        assertEquals(1, clone.startingWith("Gio").size());
        
        // clear
        clone.put("Giorgia", new Wikiword("Giorgia"));
        assertEquals(3, map.size());
        assertEquals(2, map.startingWith("Gio").size());
        assertEquals(3, clone.size());
        assertEquals(2, clone.startingWith("Gio").size());
    }
}
