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

public class PoliceTest extends TestCase {

    private Police police;
    
    public void setUp() {
        police = new Police(2);
    }

    private Text createBadText1() {
        Text text = new Text("192.168.0.25");
        
        text.add(new Element("http://javafree.org/teste/teste"));
        text.add(new Element("http://javafree.org/teste/teste"));
        text.add(new Element("http://javafree.org/teste/teste"));
        text.add(new Element("http://javafree.org/teste/teste"));
        text.add(new Element("http://javafree.org/teste/teste"));
        text.add(new Element("http://javafree.org/teste/teste"));
        text.add(new Element("http://javafree.org/teste/teste"));
        text.add(new Element("http://babaxp.org"));
        text.add(new Element("http://babaxp.org"));
        text.add(new Element("http://babaxp.org"));
        text.add(new Element("http://babaxp.org"));
        text.add(new Element("http://babaxp.org"));
        text.add(new Element("http://babaxp.org"));
        text.add(new Element("http://snaildb.org"));
        text.add(new Element("http://snaildb.org"));
        text.add(new Element("http://snaildb.org"));
        text.add(new Element("http://snaildb.org"));
        text.add(new Element("http://snaildb.org"));
        text.add(new Element("http://snaildb.org"));
        
        return text;
    }
    
    private Text createGoodText() {
        Text text = new Text("192.168.0.25");
        
        text.add(new Element("http://javafree.org"));
        text.add(new Element("http://babaxp.org"));
        text.add(new Element("http://snaildb.org"));
        
        return text;
    }
    
    private Text createBadText2() {
        Text text = new Text("192.168.0.29");
        
        text.add(new Element("http://javafree.org"));
        text.add(new Element("http://babaxp.org"));
        text.add(new Element("http://snaildb.org"));
        
        return text;
    }
    
    public void testGoodLink() {
    		police.setBadText(createBadText1());
    		assertTrue(police.isBadLink("http://javafree.org"));
    		police.setGoodLink("http://javafree.org");
    		assertFalse(police.isBadLink("http://javafree.org"));
    }
        
        
    
    public void testSetBadText() {
        police.setBadText(createBadText1());
        
        assertTrue(police.isBadUser("192.168.0.25"));
        assertTrue(police.isBadLink("http://javafree.org"));
        assertTrue(!police.isBadLink("http://javafree.org/teste/teste"));
        assertTrue(police.isBadLink("http://babaxp.org"));
        assertTrue(police.isBadLink("http://snaildb.org"));
    }

    public void testIsBadText() {
        police.setBadText(createBadText1());
        
        assertTrue(police.isBadText(createBadText2()));
    }

    public void testSetGoodLink() {
        police.setBadText(createBadText1());
        police.setGoodLink("http://javafree.org");
        
        assertFalse(police.isBadText(createBadText2()));
    }

    public void testSetGoodIP() {
        police.setBadText(createBadText1());
        police.setGoodLink("http://javafree.org");
        police.setGoodLink("http://babaxp.org");
        police.setGoodLink("http://snaildb.org");
        
        // Missed IP
        assertTrue(police.isBadText(createBadText1()));
        
        police.setGoodUser("192.168.0.25");
        
        assertTrue(police.isBadText(createBadText1()));
        
        police.setGoodUser("192.168.0.25");
        police.setGoodLink("http://javafree.org");
        police.setGoodLink("http://babaxp.org");
        police.setGoodLink("http://snaildb.org");
        
        assertFalse(police.isBadText(createGoodText()));
    }

    public void testSetBadIP() {
        police.setBadUser("192.168.0.29");
        assertTrue(police.isBadUser("192.168.0.29"));
        assertTrue(police.isBadText(createBadText2()));
    }

}
