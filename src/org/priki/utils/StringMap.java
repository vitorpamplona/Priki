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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * A Sorted Map with String keys and the startingWith method searcher.  
 *
 * @author <a href="mailto:vitor@babaxp.org">Vitor Fernando Pamplona</a>
 *
 * @since 18/11/2005
 * @version $Id: $
 */
public class StringMap<T> extends TreeMap<String, T> implements Serializable {
	public static final long serialVersionUID = 170L;
	
    /** Define the maximum of elements returned in searches */
    protected static int MAX_ELEMENTS_IN_SEARCH = 100;
    
    public Collection<T> startingWith(String key) {
        String from = key;
        String to = from.substring(0, from.length()-1) + (char)(from.charAt(from.length() -1)+1);
                
        return super.subMap(from, to).values();
    }
    
    public Collection<T> searchByAnythingWith(String key) {
    	    Map<String,T> keywords = new HashMap<String,T>();
    	    
    	    key = key.toUpperCase();
    		for (String tempKey : this.keySet()) {
    			if (tempKey.toUpperCase().contains(key)) {
    				if (keywords.size() > MAX_ELEMENTS_IN_SEARCH) return keywords.values();
    				
    				keywords.put(tempKey, this.get(tempKey));
    			}
    		}
    		
    		if (keywords.size() > 0) {
    			return keywords.values();
    		}

    		String[] keys = key.split(" ");
    		
    		if (keys.length < 2) return keywords.values(); 
    		
    		int contKeys = 0;
    		for (String tempKey : this.keySet()) {
    			boolean hasAllKeys = true;
    			for (contKeys=0; contKeys<keys.length; contKeys++) {
    				if (!tempKey.toUpperCase().contains(keys[contKeys])) {
    					hasAllKeys = false;
    				}
    			}
    			if (hasAllKeys) {
    				if (keywords.size() > MAX_ELEMENTS_IN_SEARCH) return keywords.values();
    				keywords.put(tempKey, this.get(tempKey));
    			}
    		}
    		
    		return keywords.values();
    }
}
