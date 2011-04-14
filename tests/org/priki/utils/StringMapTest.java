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
package org.priki.utils;

import junit.framework.TestCase;

public class StringMapTest extends TestCase {

    public void testStartingWith() {
        StringMap<String> map = new StringMap<String>();
        
        map.put("Vitor", "Vitor");
        map.put("Vitor Fernando", "Vitor Fernando");
        map.put("Vitos", "Vitos");
        map.put("Zurita", "Zurita");
        
        assertEquals(2, map.startingWith("Vitor").size());
    }
    
    public void testSearch() {
        StringMap<String> map = new StringMap<String>();
        
        map.put("Vitor", "Vitor");
        map.put("Vitor Fernando", "Vitor Fernando");
        map.put("Vitos", "Vitos");
        map.put("Zurita", "Zurita");
        
        assertEquals(3, map.searchByAnythingWith("Vit").size());
        assertEquals(2, map.searchByAnythingWith("tor").size());
        assertEquals(1, map.searchByAnythingWith("Zurita").size());
        assertEquals(1, map.searchByAnythingWith("Fernando").size());
        assertEquals(1, map.searchByAnythingWith("Vitor Fernando").size());
        assertEquals(1, map.searchByAnythingWith("Zur ita").size());
        assertEquals(0, map.searchByAnythingWith("Vitor Vitos").size());        
    }

}
