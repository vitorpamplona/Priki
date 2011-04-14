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
package org.priki.format;

import junit.framework.TestCase;

import org.priki.bo.Element;
import org.priki.bo.Text;
import org.priki.bo.Wikiword;

public class TextFormatterTest extends TestCase {

    public void testFormat() {
        Text text = new Text(); 
        text.add(new Wikiword("The"));
        text.add(new Wikiword("Book"));
        text.add(new Wikiword("is"));
        text.add(new Wikiword("on"));
        text.add(new Wikiword("the"));
        text.add(new Wikiword("table"));
        text.add(new Element("."));
        
        assertEquals("The Book is on the table.", new HtmlFormatter().format(text));
    }

    public void testLink() {
        Text text = new Text(); 
        text.add(new Element("http://www.javafree.org"));
        
        assertEquals("<a href=\"http://www.javafree.org\" target=\"_blank\">http://www.javafree.org</a>", new HtmlFormatter().format(text));
        
        text = new Text(); 
        text.add(new Element("ftp://www.javafree.org"));
        
        assertEquals("<a href=\"ftp://www.javafree.org\" target=\"_blank\">ftp://www.javafree.org</a>", new HtmlFormatter().format(text));
        
        text = new Text(); 
        text.add(new Element("file://www.javafree.org"));
        
        assertEquals("<a href=\"file://www.javafree.org\" target=\"_blank\">file://www.javafree.org</a>", new HtmlFormatter().format(text));
        
        text = new Text(); 
        text.add(new Element("https://www.javafree.org"));
        
        assertEquals("<a href=\"https://www.javafree.org\" target=\"_blank\">https://www.javafree.org</a>", new HtmlFormatter().format(text));
        
        text = new Text(); 
        text.add(new Element("<code>Teste Teste</code>"));
        
        assertEquals("<code>Teste Teste</code>", new HtmlFormatter().format(text));        
    }
    
    public void testEmail() {
        Text text = new Text(); 
        text.add(new Element("vitor@vitorpamplona.com"));
        
        assertEquals("<a href=\"mailto:vitor@vitorpamplona.com\">vitor@vitorpamplona.com</a>", new HtmlFormatter().format(text));
    }
    
    public void testAspas() {
        Text text = new Text(); 
        text.add(new Element("\""));
        text.add(new Wikiword("The"));
        text.add(new Wikiword("Book"));
        text.add(new Element("\""));
        text.add(new Wikiword("is"));
        text.add(new Wikiword("on"));
        text.add(new Wikiword("the"));
        text.add(new Element("\""));
        text.add(new Wikiword("table"));
        text.add(new Element("."));
        text.add(new Element("\""));
        
        assertEquals("\"The Book\" is on the \"table.\"", new HtmlFormatter().format(text));
    } 
    
    public void testAspas2() {
        Text text = new Text(); 
        text.add(new Element("\""));
        text.add(new Wikiword("The"));
        text.add(new Wikiword("Book"));
        text.add(new Element("\'"));
        text.add(new Wikiword("is"));
        text.add(new Wikiword("on"));
        text.add(new Wikiword("the"));
        text.add(new Element("\'"));
        text.add(new Wikiword("table"));
        text.add(new Element("."));
        text.add(new Element("\""));
        
        assertEquals("\"The Book \'is on the\' table.\"", new HtmlFormatter().format(text));
    }
    public void testAspas3() {
        Text text = new Text(); 
        text.add(new Wikiword("The"));
        text.add(new Wikiword("Book"));
        text.add(new Element("\'"));
        text.add(new Wikiword("is"));
        text.add(new Wikiword("on"));
        text.add(new Wikiword("the"));
        text.add(new Element("\'"));
        text.add(new Wikiword("table"));
        text.add(new Element("."));
        text.add(new Element("\""));
        text.add(new Element("\""));
        
        assertEquals("The Book \'is on the\' table. \"\"", new HtmlFormatter().format(text));
    }
}
