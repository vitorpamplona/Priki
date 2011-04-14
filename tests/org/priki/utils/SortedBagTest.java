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

import java.util.Iterator;

import junit.framework.TestCase;

public class SortedBagTest extends TestCase {
    public void testCollection() {
        SortedBag<String> bag = new SortedBag<String>();
        
        bag.add("Teste");
        bag.add("Vitor");
        bag.add("Vitor");
        bag.add("Paulo");
        bag.add("Pedro");
        bag.add("Pedro");
        bag.add("Vitor");
        
        Iterator<String> i = bag.iterator();
        assertEquals("Vitor", i.next());
        assertEquals("Pedro", i.next());
        assertEquals("Paulo", i.next());
        assertEquals("Teste", i.next());        
        
        assertEquals(3, bag.getCount("Vitor"));
        assertEquals(2, bag.getCount("Pedro"));
        assertEquals(1, bag.getCount("Teste"));
        assertEquals(1, bag.getCount("Paulo"));
        
        bag.remove("Vitor");
        
        assertEquals(2, bag.getCount("Vitor"));
    }
}
