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

import java.io.Serializable;


/**
 * <p>Each Element is basically a word with a definition text. The word, symbol, or htmltag is 
 * putted into keyword attribute. Symbols and HTML tags doesn't have definitions. 
 * An element is a simple word (Letters + Numbers + "_") and can has or hasn't definition. The 
 * definition is a list of Text class, a History. The first Text is the official definition
 * and the others are only histories. This can help the rollback process is cases of DoS attack.    
 * </p> 
 * 
 * @author <a href="mailto:vitor@babaxp.org">Vitor Fernando Pamplona</a>
 *
 * @since 31/05/2005
 * @version $Id: Element.java,v 1.1 2005/07/21 16:04:16 vfpamp Exp $
 * 
 * @see Text
 * @see HistoryText
 */
public class Element implements Serializable {

    public static final long serialVersionUID= 1L;
    
    /* A wiki element */
    private String keyword;
    
  
    /* Creates a new element with a key. */
    public Element(String key) {
        this.keyword = key;
    }


    /** Gets the keywork to this definition. */
    public String getKeyword() {
        return this.keyword;
    }
    
    public String toString() {
        return keyword;
    }
    
    public boolean isALink() {
        return getKeyword().matches("(https://|https://|http://|file://|ftp://).+");
    }

    public boolean isAnEmail() {
        return getKeyword().matches("(\\w|\\d).+@(\\w|\\d).+\\.(\\.|\\w|\\d).+");
    }
    
    public boolean isACodeTag() {
        return getKeyword().startsWith("<code");
    }    
}

