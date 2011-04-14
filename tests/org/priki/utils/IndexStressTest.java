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

import java.util.Collection;

import junit.framework.TestCase;

import org.priki.bo.Wikiword;

/**
 * Stress tests for index classes.<br>
 * This aims to check the overhead of {@link WikiwordIndex} instead of {@link StringMap}.<br>
 * 
 * @author Giovane.Kuhn
 * @since 13/02/2007
 */
public class IndexStressTest extends TestCase {

    private static final int ELEMENTS_COUNT = 100000;

    private static final String ELEMENTS_PREFIX = "1";
        
    private static final int LOOPS_COUNT = 100;
    

    private static void populateMap(final StringMap<Wikiword> map, final int count) {
        for (int i = 0; i < count; i++) {
            String key = Integer.toString(i);
            map.put(key, new Wikiword(key));
        }
    }

    private static int elementsStartingWith(final StringMap<Wikiword> map, final String prefix) {
        int count = 0;
        for (String key : map.keySet()) {
            if (key.startsWith(prefix)) {
                count++;
            }
        }
        return count;
    }

    public void testStringMapStress() {
        // debug
        StringMap<Wikiword> map = new StringMap<Wikiword>();
        System.out.println("=======================");
        System.out.println(StringMap.class);

        // populate map
        long start = System.currentTimeMillis();
        populateMap(map, ELEMENTS_COUNT);
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Size: " + map.size());
        System.out.println("Populate: " + elapsed + "ms.");

        // stress map with startingWith searches
        int count = elementsStartingWith(map, ELEMENTS_PREFIX);
        start = System.currentTimeMillis();
        for (int i = 0; i < LOOPS_COUNT; i++) {
            Collection<Wikiword> wikiwords = map.startingWith(ELEMENTS_PREFIX);
            assertEquals(count, wikiwords.size());
        }
        elapsed = System.currentTimeMillis() - start;
        System.out.println("Loops: " + LOOPS_COUNT);
        System.out.println("Count: " + count);
        System.out.println("Search: " + elapsed + "ms.");
    }

    public void testWikiwordIndexerStress() {
        // debug
        WikiwordIndex map = new WikiwordIndex();
        System.out.println("=======================");
        System.out.println(WikiwordIndex.class);

        // populate map
        long start = System.currentTimeMillis();
        populateMap(map, ELEMENTS_COUNT);
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Size: " + map.size());
        System.out.println("Populate: " + elapsed + "ms.");

        // stress map with startingWith searches
        int count = elementsStartingWith(map, ELEMENTS_PREFIX);
        start = System.currentTimeMillis();
        for (int i = 0; i < LOOPS_COUNT; i++) {
            Collection<Wikiword> wikiwords = map.startingWith(ELEMENTS_PREFIX);
            assertEquals(count, wikiwords.size());
        }
        elapsed = System.currentTimeMillis() - start;
        System.out.println("Loops: " + LOOPS_COUNT);
        System.out.println("Count: " + count);
        System.out.println("Search: " + elapsed + "ms.");
    }
}
