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
 * @since 01/03/2007
 */
public class WikiwordUtilsTest extends TestCase {

    public void testEqualsKeyword() {
        
        // Case sensitive
        Wikiword word = new Wikiword("Giovane");
        word.setCaseSensitive(true);
        assertTrue(WikiwordUtils.equalsKeyword("Giovane", word));
        assertFalse(WikiwordUtils.equalsKeyword("giovane", word));
        assertFalse(WikiwordUtils.equalsKeyword("Giorgio", word));
        
        // Case insensitive
        word.setCaseSensitive(false);
        assertTrue(WikiwordUtils.equalsKeyword("Giovane", word));
        assertTrue(WikiwordUtils.equalsKeyword("giovane", word));
        assertFalse(WikiwordUtils.equalsKeyword("Giorgio", word));
    }
    
}
