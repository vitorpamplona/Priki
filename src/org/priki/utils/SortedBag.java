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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The same class existing in Jakarta Commons Collections => BAG. 
 *  
 * Defines a collection that counts the number of times an object appears in the collection.
 * Suppose you have a List that contains {a, b, b, c}. Putting into this Bag, the order will be {b, a, c}  
 * 
 * @author <a href="mailto:vitor@babaxp.org">Vitor Fernando Pamplona</a>
 *
 * @since 22/12/2005
 * @version $Id: $
 */
public class SortedBag<E extends Comparable<E>> implements Collection<E>, Iterable<E>, Serializable {
	public static final long serialVersionUID = 171L;
    private Map<E, BagItem> items = new HashMap<E, BagItem>();
    
    public boolean add(E object) {
        BagItem item = items.get(object);
        if (item != null)
            item.times++;
        else 
            items.put(object, new BagItem(object));
        
        return true;
    }
    
    public boolean remove(Object object) {
        BagItem item = items.get(object);
        
        if (item == null) return false;
        
        if (item.times == 1)
            items.remove(object);
        else 
            item.times--;
        
        return true;
    }
    
    public int getCount(E object) {
        BagItem item = items.get(object);
        if (item == null) return 0;
        
        return item.times;
    }
    
    private List<BagItem> getSortedList() {
        List<BagItem> sorted = new ArrayList<BagItem>(items.values());
        Collections.sort(sorted);
        return sorted;
    }
    
    public Iterator<E> iterator() {
        List<BagItem> sorted = getSortedList();
        return new BagIterator(sorted.iterator());
    }

    public int size()  {
        return items.size();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean contains(Object o) {
        return items.containsKey(o);
    }

    public Object[] toArray() {
    	List<BagItem> list = getSortedList();
    	Object[] ret = new Object[list.size()];
    	
    	for (int i=0; i<list.size(); i++)  {
    		ret[i] = list.get(i).obj;
    	}
    	
    	return ret;
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) 
                return false;
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        for (Object e : c) {
            remove(e);
        }
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        items.clear();
    }
    
    /**
     * Each item of this collection
     * 
     * @author <a href="mailto:vitor@babaxp.org">Vitor Fernando Pamplona</a>
     *
     * @since 20/12/2005
     * @version $Id: $
     */
    class BagItem implements Comparable<BagItem>, Serializable {
    		public static final long serialVersionUID = 174L;	
    	
        public E obj;
        public int times = 1;
     
        public BagItem(E obj) {
            this.obj = obj;
        }
        
        public boolean equals(Object o) {
            return obj.equals(((BagItem)o).obj);
        }
        
        public int hashCode() {
            return obj.hashCode();
        }
        
        public int compareTo(BagItem o) {
            if (times < o.times) return 1;
            if (times > o.times) return -1;
            
            return obj.compareTo(o.obj);
        }         
    }

    
    class BagIterator implements Iterator<E>, Serializable {
    		public static final long serialVersionUID = 175L;    	
        private Iterator<BagItem> i;
        
        public BagIterator(Iterator<BagItem> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return i.hasNext();
        }
        
        public E next() {
            return i.next().obj;
        }

        public void remove() {
            i.remove();
        }
    }

}


