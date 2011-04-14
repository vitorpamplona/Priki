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

import junit.framework.TestCase;

public class TextTest extends TestCase {

    public void testAdd() {
        Text text = new Text();
        
        text.add(new Element("vitor"));
    }

    public void testGetElementCount() {
        Text text = new Text();
        
        text.add(new Element("vitor"));
        
        assertEquals(1, text.getElementCount());
    }

    public void testGetElement() {
        Text text = new Text("127.0.0.1");
        Element e = new Element("Paulo");
        Element e2 = new Element("Pedro");
              
        text.add(e);
        
        assertEquals(e, text.getElement(0));
        
        text.add(e2);
        
        assertEquals(e, text.getElement(0));
        assertEquals(e2, text.getElement(1));
        assertEquals("127.0.0.1", text.getPostUser());
        assertTrue(text.getWhoPosted() instanceof AnonymousUser);
    }

}
