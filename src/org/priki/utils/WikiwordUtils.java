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

import org.priki.bo.Wikiword;

/**
 * Utility class for wikiwords.<br>
 * 
 * @author Giovane.Kuhn
 * @since 01/03/2007
 */
public class WikiwordUtils {

    /**
     * Check if the informed key and wikiword are equals.<br>
     * Case sensitive wikiword uses {@link String#equals(Object)}.<br>
     * Case insensitive wikiword uses {@link String#equalsIgnoreCase(String)}.<br>
     * 
     * @param key String keyword, cannot be <code>null</code>.
     * @param word Wikiword, cannot be <code>null</code>.
     * @return <code>true</code> if key and wikiwords are equals, otherwise <code>false</code>
     */
    public static boolean equalsKeyword(String key, Wikiword word) {
        return (word.isCaseSensitive() && key.equals(word.getKeyword())) || (!word.isCaseSensitive() && key.equalsIgnoreCase(word.getKeyword()));
    }

}
