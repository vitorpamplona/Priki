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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.priki.bo.Wiki;
import org.priki.bo.Wikiword;

/**
 * Class used to index a {@link Collection} of {@link Wikiwords}, 
 * that supports case insensitive keywords.<br>
 * This class holds a case sensitive {@link StringMap} of {@link Wikiwords}, 
 * used in the default {@link Map} methods.<br>
 * This class also holds a case insensitive {@link TreeMap}, 
 * used to accelerate the {@link WikiwordIndex#startingWith(String)} searches.<br> 
 *
 * TODO Synchronize modification operations from the StringMap.iterator() with the insensitive map<br>
 * 
 * @author Giovane.Kuhn
 * @since 13/02/2007
 */
public final class WikiwordIndex extends StringMap<Wikiword> {

    private static final long serialVersionUID = 1L;

    /**
     * Index wikiwords by their lower case keyword.<br>  
     * Key: A case insensitive {@link String}<br>
     * Value: {@link Map} of {@link Wikiwords} from the {@link Wiki}<br>
     * TODO May be this could be transient
     */
    private TreeMap<String, Map<String, Wikiword>> insensitiveMap;

    private TreeMap<String, Map<String, Wikiword>> insensitiveMap() {
        if (insensitiveMap == null) {
            insensitiveMap = new TreeMap<String, Map<String, Wikiword>>();
        }
        return insensitiveMap;
    }

    /**
     * Returns a {@link Collection} of {@link Wikiword} equals to the 
     * informed {@link key} ignoring the case sensitive.<br>
     * @param key Key whose associated values is to be returned.
     * @return {@link Collection} of {@link Wikiword} equals to 
     *          the informed key ignoring case sensitive.<br>
     *          If any value match the informed key, returns an empty collection.
     */
    public Collection<Wikiword> getIgnoreCase(String key) {
        Map<String, Wikiword> ret = insensitiveMap().get(key.toLowerCase());
        if (ret == null) {
            return Collections.emptyList();
        }
        return new ArrayList<Wikiword>(ret.values());
    }

    @Override
    public Collection<Wikiword> startingWith(String key) {

        // Define the range of search (lower case)
        String from = key.toLowerCase();
        String to = from.substring(0, from.length() - 1) + (char) (from.charAt(from.length() - 1) + 1);

        // Analyze each entry
        List<Wikiword> ret = new ArrayList<Wikiword>();
        for (Map<String, Wikiword> innerMap : insensitiveMap().subMap(from, to).values()) {
            for (Wikiword wikiword : innerMap.values()) {

                // Case insensitive wikiwods is ensured by the lower case map
                if (!wikiword.isCaseSensitive()) {
                    ret.add(wikiword);
                    continue;
                }

                // Test case sensitivies wikiwords against informed key
                if (wikiword.getKeyword().startsWith(key)) {
                    ret.add(wikiword);
                }
            }
        }
        return ret;
    }

    @Override
    public Wikiword put(String key, Wikiword value) {

        // Add to the insensitive map
        String lowerKey = key.toLowerCase();
        Map<String, Wikiword> innerMap = insensitiveMap().get(lowerKey);
        if (innerMap == null) {
            innerMap = new HashMap<String, Wikiword>();
            insensitiveMap.put(lowerKey, innerMap);
        }
        innerMap.put(key, value);

        // Add to the default map
        return super.put(key, value);
    }

    @Override
    public void putAll(Map< ? extends String, ? extends Wikiword> map) {
        // Add to the insensitive map, the method put add to the default map
        for (Map.Entry< ? extends String, ? extends Wikiword> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Wikiword remove(Object key) {

        // Remove from the insensitive map
        String lowerKey = ((String) key).toLowerCase();
        Map<String, Wikiword> innerMap = insensitiveMap().get(lowerKey);
        if (innerMap != null) {
            innerMap.remove(key);

            // Check if inner map is empty
            if (innerMap.isEmpty()) {
                insensitiveMap.remove(lowerKey);
            }
        }

        // Remove from the default map
        return super.remove(key);
    }

    @Override
    public void clear() {
        insensitiveMap().clear();
        super.clear();
    }

    @Override
    public Object clone() {
        WikiwordIndex ret = (WikiwordIndex) super.clone();
        ret.insensitiveMap = new TreeMap<String, Map<String, Wikiword>>();
        ret.insensitiveMap.putAll(insensitiveMap());
        return ret;
    }

}
