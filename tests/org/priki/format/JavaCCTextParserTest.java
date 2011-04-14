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

import java.util.List;

import junit.framework.TestCase;

import org.priki.bo.AnonymousUser;
import org.priki.bo.Text;
import org.priki.bo.Wiki;
import org.priki.bo.Wikiword;

public class JavaCCTextParserTest extends TestCase {

    private Text text;
    private int i;
    private HtmlParser parser;
    private Wiki wiki;
    
    public void setUp() {
        wiki = new Wiki();
        text = null;
        i = 0;
        parser = new HtmlParser(wiki);
    }
    
    public String nextKeyword() {
    	return text.getElement(i++).getKeyword();
    }
    
    Text parseText(String text) {
    	return parser.parseText(text, new AnonymousUser("Vitor"));
    }
    
    public void testHtmlText() {        
        text = parseText("<HTML>Paulo</HTML>");
        
        assertNotNull(text);
        assertEquals(3, text.getElementCount());
        assertEquals("<HTML>", nextKeyword());
        assertEquals("Paulo", nextKeyword());
        assertEquals("</HTML>", nextKeyword());                 
    }
    
    public void testLinkText() {        
        text = parseText(" http://www.javafree.org ");
        
        assertNotNull(text);
        assertEquals(1, text.getElementCount());
        assertEquals("http://www.javafree.org", nextKeyword());
        
        text = parseText(" <b>http://www.javafree.org</b> ");
        
        assertNotNull(text);
        assertEquals(3, text.getElementCount());
        assertEquals("http://www.javafree.org", text.getElement(1).getKeyword());
        
        text = parseText("http://www.javafree.org.");
        assertNotNull(text);
        assertEquals(2, text.getElementCount());
        assertEquals("http://www.javafree.org", text.getElement(0).getKeyword());
        assertEquals(".", text.getElement(1).getKeyword());
    }
    
    public void testNormalHTMLText() {        
        text = parseText(" <p>Algum texto</p> ");
                
        assertNotNull(text);
        assertEquals(4, text.getElementCount());
        assertEquals("<p>", nextKeyword());
        assertEquals("Algum", nextKeyword());
        assertEquals("texto", nextKeyword());
        assertEquals("</p>", nextKeyword());
        
        text = parseText("<b>Teste</b>");
        
        assertNotNull(text);
        i=0;
        assertEquals(3, text.getElementCount());
        assertEquals("<b>", nextKeyword());
        assertEquals("Teste", nextKeyword());
        assertEquals("</b>", nextKeyword());   
        
        text = parseText("&lt;b&gt;t<em>este</em>&lt;/b&gt;");
        
        assertNotNull(text);
        i=0;
        //assertEquals(8, text.getElementCount());
        assertEquals("&lt;", nextKeyword());
        assertEquals("b", nextKeyword());
        assertEquals("&gt;", nextKeyword()); 
        
        text = parseText("<pre>&lt; b &gt;t<em>este</em>&lt;/b&gt;</pre>");
        
        assertNotNull(text);
        i=0;
        //assertEquals(8, text.getElementCount());
        assertEquals("<pre>&lt; b &gt;t<em>este</em>&lt;/b&gt;</pre>", nextKeyword());

    }
    
    public void testEmailText() {        
        text = parseText(" vitor@vitorpamplona.com ");
        
        assertNotNull(text);
        assertEquals(1, text.getElementCount());
        assertEquals("vitor@vitorpamplona.com", nextKeyword());
        
        text = parseText(" <b>vitor@vitorpamplona.com</b> ");
        
        assertNotNull(text);
        assertEquals(3, text.getElementCount());
        assertEquals("vitor@vitorpamplona.com", text.getElement(1).getKeyword());
        
        text = parseText("vitor@vitorpamplona.com.");
        assertNotNull(text);
        assertEquals(2, text.getElementCount());
        assertEquals("vitor@vitorpamplona.com", text.getElement(0).getKeyword());
        assertEquals(".", text.getElement(1).getKeyword());
    }
        
    
    public void testHTMLCODEText() {        
        text = parseText(" Vítor ");
        
        assertNotNull(text);
        assertEquals(1, text.getElementCount());
        assertEquals("Vítor", nextKeyword());
    }
    
    public void testHTMLText() {        
        text = parseText(" <dsfasd> ");
        
        assertNotNull(text);
        assertEquals(1, text.getElementCount());
        assertEquals("<dsfasd>", text.getElement(i).getKeyword());
        
        
        text = parseText(" <dsfasd>sfsd ");
        
        assertNotNull(text);
        assertEquals(2, text.getElementCount());
        assertEquals("<dsfasd>", text.getElement(0).getKeyword());
        assertEquals("sfsd", text.getElement(1).getKeyword());
        
        text = parseText(" <dsfasd>sfsd7 ��&*( ");
        
        assertNotNull(text);
        assertEquals(7, text.getElementCount());
        assertEquals("<dsfasd>", text.getElement(0).getKeyword());
        assertEquals("sfsd7", text.getElement(1).getKeyword());
        assertEquals("�", text.getElement(2).getKeyword());
        assertEquals("�", text.getElement(3).getKeyword());
        assertEquals("&", text.getElement(4).getKeyword());
        assertEquals("*", text.getElement(5).getKeyword());
        assertEquals("(", text.getElement(6).getKeyword());
    }

    public void testHTMLPowerText() {
        text = parseText(" 6<dsfasd>sfsd7( --- __��&*( <");
        
        assertNotNull(text);
        assertEquals("6", nextKeyword());
        assertEquals("<dsfasd>", nextKeyword());
        assertEquals("sfsd7", nextKeyword());
        assertEquals("(", nextKeyword());
        assertEquals("-", nextKeyword());
        assertEquals("-", nextKeyword());
        assertEquals("-", nextKeyword());
        assertEquals("__", nextKeyword());
        assertEquals("�", nextKeyword());
        assertEquals("�", nextKeyword());
        assertEquals("&", nextKeyword());
        assertEquals("*", nextKeyword());
        assertEquals("(", nextKeyword());
        assertEquals("<", nextKeyword());
        assertEquals(14, text.getElementCount());
        
        text = parseText(" < asdf sad fasdf > </ OI > </OI> < / OI >");
        
        i=0;
        assertEquals("<", nextKeyword());
        assertEquals("asdf", nextKeyword());
        assertEquals("sad", nextKeyword());
        assertEquals("fasdf", nextKeyword());
        assertEquals(">", nextKeyword());
        assertEquals("</ OI >", nextKeyword());
        assertEquals("</OI>", nextKeyword());
        assertEquals("<", nextKeyword());
        assertEquals("/", nextKeyword());
        assertEquals("OI", nextKeyword());
        assertEquals(">", nextKeyword());
        
    }
    
    public void testNormalTextParser() {
        text = parseText("The book is on the table.");
        
        assertEquals(1, wiki.getElementCount());
        assertEquals(6, wiki.getWikiwordCount());
        assertEquals("The", nextKeyword());
        assertEquals("book", nextKeyword());
        assertEquals("is", nextKeyword());
        assertEquals("on", nextKeyword());
        assertEquals("the", nextKeyword());
        assertEquals("table", nextKeyword());
        assertEquals(".", nextKeyword());
    }

    public void testSimbolicText() {
        text = parseText("The- book. is on *&the table: Hey");
                
        assertEquals(5, wiki.getElementCount());
        assertEquals(7, wiki.getWikiwordCount());
        
        assertEquals("The", nextKeyword());
        assertEquals("-", nextKeyword());
        assertEquals("book", nextKeyword());
        assertEquals(".", nextKeyword());
        assertEquals("is", nextKeyword());
        assertEquals("on", nextKeyword());
        assertEquals("*", nextKeyword());
        assertEquals("&", nextKeyword());
        assertEquals("the", nextKeyword());
        assertEquals("table", nextKeyword());
        assertEquals(":", nextKeyword());
        assertEquals("Hey", nextKeyword());
    }
    
    public void testCompositeWikiWord() {
        Wikiword vitor = new Wikiword("Vitor");
        text = parseText("Vitor Fernando Pamplona");
        vitor.setDefinition(text);
        
        assertEquals("Vitor", nextKeyword());
        assertEquals("Fernando", nextKeyword());
        assertEquals("Pamplona", nextKeyword());
        
        wiki.newWikiword("Vitor Fernando");
        
        assertEquals(4, wiki.getWikiwordCount());
        text = vitor.getDefinition();
        
        // Testing composite parser
        text = parseText("Vitor Fernando Pamplona");
        
        i=0;
        assertEquals("Vitor Fernando", nextKeyword());
        assertEquals("Pamplona", nextKeyword());
        assertEquals(4, wiki.getWikiwordCount());
        
        // Testing composite parser
        text = parseText("Vitor Pamplona");
        
        i=0;
        assertEquals("Vitor", nextKeyword());
        assertEquals("Pamplona", nextKeyword());
        assertEquals(4, wiki.getWikiwordCount());
        
        text = parseText("Paulo Roberto Pamplona");
        
        assertEquals(6, wiki.getWikiwordCount());

        wiki.newElement("Paulo Roberto Pamplona");
        
        assertEquals(6, wiki.getWikiwordCount());
        assertEquals(1, wiki.getElementCount());
        
        wiki.newWikiword("Paulo Roberto Pamplona");
        
        assertEquals(7, wiki.getWikiwordCount());
        
        text = parseText("Paulo Roberto Pamplona");
        i=0;
        assertEquals("Paulo Roberto Pamplona", nextKeyword());
        assertEquals(7, wiki.getWikiwordCount());
        
        text = parseText("Paulo Vitor Roberto Pamplona");
        i=0;
        assertEquals("Paulo", nextKeyword());
        assertEquals("Vitor", nextKeyword());
        assertEquals("Roberto", nextKeyword());
        assertEquals("Pamplona", nextKeyword());
        assertEquals(7, wiki.getWikiwordCount());
        
        // Testing non composite parser
        text = parseText("Paulo Roberto 12 Pamplona");
        i=0;
        assertEquals("Paulo", nextKeyword());
        assertEquals("Roberto", nextKeyword());
        assertEquals("12", nextKeyword());
        assertEquals("Pamplona", nextKeyword());
        assertEquals(8, wiki.getWikiwordCount());
    }
    
    public void testParserInNotChangeTags() {        
        text = parseText("<pre>Paulo</pre> Oi");
        
        assertNotNull(text);
        assertEquals("<pre>Paulo</pre>", nextKeyword());
        assertEquals("Oi", nextKeyword());
        assertEquals(2, text.getElementCount());        
       
        text = parseText("<pre>Paulo<br> vitor</pre> Oi");
        
        assertNotNull(text);
        i=0;
        assertEquals("<pre>Paulo<br> vitor</pre>", nextKeyword());
        assertEquals("Oi", nextKeyword());
        assertEquals(2, text.getElementCount());        
        
        text = parseText("<a href='teste'></a> Oi");
        
        assertNotNull(text);
        i=0;        
        assertEquals("<a href='teste'></a>", nextKeyword());
        assertEquals(2, text.getElementCount());
        assertEquals("Oi", nextKeyword());     
        
        text = parseText("<a href='teste/teste/teste'>aeee</a> Oi");
        
        assertNotNull(text);
        i=0;        
        assertEquals("<a href='teste/teste/teste'>aeee</a>", nextKeyword());
        assertEquals("Oi", nextKeyword());        
        assertEquals(2, text.getElementCount());
           
        
        text = parseText("<a href='<pre></pre> sdfas'> 4545454 </a> Oi");
        
        assertNotNull(text);
        i=0;        
        assertEquals("<a href='<pre></pre> sdfas'> 4545454 </a>", nextKeyword());
        assertEquals(2, text.getElementCount());
        assertEquals("Oi", nextKeyword());       
        
        text = parseText("<code href='<a></pre> sdfas'> </a> <>></code> Oi");
        
        assertNotNull(text);
        i=0;        
        assertEquals("<code href='<a></pre> sdfas'> </a> <>></code>", nextKeyword());
        assertEquals(2, text.getElementCount());
        assertEquals("Oi", nextKeyword());     
        
        
        text = parseText("<a href=\"teste/teste/teste\">aeee</a> Oi");
        
        assertNotNull(text);
        i=0;        
        assertEquals("<a href=\"teste/teste/teste\">aeee</a>", nextKeyword());
        assertEquals("Oi", nextKeyword());        
        assertEquals(2, text.getElementCount());
                      
    }
    
    public void testGetOnlyWikiwords() {
    	List<Wikiword> wikiwords = parser.getOnlyWikiwords("Hi <a href=\"teste/teste/teste\">aeee</a> Oi");
    	
        i=0;        
        assertEquals(2, wikiwords.size());
        assertEquals("Hi", wikiwords.get(i++).getKeyword());        
        assertEquals("Oi", wikiwords.get(i++).getKeyword());   
        
        wikiwords = parser.getOnlyWikiwords("Hi <img href=\"teste/teste/teste\">aeee</img> Oi");
    	
        i=0;        
        assertEquals(3, wikiwords.size());
        assertEquals("Hi", wikiwords.get(i++).getKeyword());
        assertEquals("aeee", wikiwords.get(i++).getKeyword());
        assertEquals("Oi", wikiwords.get(i++).getKeyword());    
        
        wikiwords = parser.getOnlyWikiwords("Hi <a href=\"teste/teste/teste\" target=\"_blank\">aeee</a> Oi");
    	
        i=0;        
        assertEquals(2, wikiwords.size());
        assertEquals("Hi", wikiwords.get(i++).getKeyword());
        assertEquals("Oi", wikiwords.get(i++).getKeyword());      
        
        wikiwords = parser.getOnlyWikiwords("<a href=\"http://daltoncamargo.blogspot.com/\" target=\"_new\">Dalton Camargo</a><br><span style=\"font-size: 10px;\">JEE Software Architect<br>JavaFree.org Founder<br>JavaBB.org Owner<br>Technical Revisor of Spring in Action the Book to Portuguese</span>");
    	
        i=0;        
        assertEquals(17, wikiwords.size());
        assertEquals("JEE", wikiwords.get(i++).getKeyword());
        assertEquals("Software", wikiwords.get(i++).getKeyword());
        
        wikiwords = parser.getOnlyWikiwords("Vitor Fernando Pamplona.");
    	
        i=0;        
        assertEquals(3, wikiwords.size());
        assertEquals("Vitor", wikiwords.get(i++).getKeyword());
        assertEquals("Fernando", wikiwords.get(i++).getKeyword());
        assertEquals("Pamplona", wikiwords.get(i++).getKeyword());      
        
        wikiwords = parser.getOnlyWikiwords("JavaBB.org");
    	
        i=0;        
        assertEquals(1, wikiwords.size());
        assertEquals("JavaBB.org", wikiwords.get(i++).getKeyword());
        
    }

    public void testSplitWikiword() {
        wiki.newWikiword("design patterns");
        
        List<Wikiword> col = parser.getWikiwords("design patterns"); 
        assertEquals(1, col.size());
        assertEquals("design patterns", col.get(0).getKeyword());
        
        col = parser.splitWikiword("design patterns"); 
        assertEquals(2, col.size());
        assertEquals("design", col.get(0).getKeyword());
        assertEquals("patterns", col.get(1).getKeyword());
    }
    
    public void testHTMLAmbiguityProblem() {
        text = parseText("<A href=\"sjfkasfdj\">Paulo</ a > Oi <a href=\"sjfkasfdj\">Paulo</a>"); 
        
        assertEquals("<A href=\"sjfkasfdj\">Paulo</ a >", text.getElement(0).getKeyword());
        assertEquals("Oi", text.getElement(1).getKeyword());
        assertEquals("<a href=\"sjfkasfdj\">Paulo</a>", text.getElement(2).getKeyword());
        
        text = parseText("<code> some code 1</ code > Oi <code> some code 2</code>");
        
        assertEquals("<code> some code 1</ code >", text.getElement(0).getKeyword());
        assertEquals("Oi", text.getElement(1).getKeyword());
        assertEquals("<code> some code 2</code>", text.getElement(2).getKeyword());
        
        text = parseText("<pre> some code 1</pre> Oi <pre> some code 2</pre > <pre> some code 3</ pre> <pre> some code 4</pre>"); 
        
        assertEquals("<pre> some code 1</pre>", text.getElement(0).getKeyword());
        assertEquals("Oi", text.getElement(1).getKeyword());
        assertEquals("<pre> some code 2</pre >", text.getElement(2).getKeyword());
        assertEquals("<pre> some code 3</ pre>", text.getElement(3).getKeyword());
        assertEquals("<pre> some code 4</pre>", text.getElement(4).getKeyword());
    }
   
    
    public void testHTMLParser() {
    	text = parseText("<p>fabi &lt;a href=\"teste\"&gt;teste&lt;/a&gt; </p>");
    	
    	i=0;
    	assertEquals("<p>",nextKeyword());
    	assertEquals("fabi",nextKeyword());
    	assertEquals("&lt;",nextKeyword());
    	assertEquals("a",nextKeyword());
    	assertEquals("href",nextKeyword());
    	assertEquals("=",nextKeyword());
    	assertEquals("\"",nextKeyword());
    	assertEquals("teste",nextKeyword());
    	assertEquals("\"",nextKeyword());
    	assertEquals("&gt;",nextKeyword());
    	assertEquals("teste",nextKeyword());
    	assertEquals("&lt;",nextKeyword());
    	assertEquals("/",nextKeyword());
    	assertEquals("a",nextKeyword());
    	assertEquals("&gt;",nextKeyword());
    	assertEquals("</p>",nextKeyword());    	
    	
    	text = parseText("<h2>Titulo 2</h2><p>&nbsp;</p><h3>Título 3 </h3><p> aasdfasdfasdf </p>");
    	
    	i=0;
    	assertEquals("<h2>",nextKeyword());
    	assertEquals("Titulo",nextKeyword());
    	assertEquals("2",nextKeyword());
    	assertEquals("</h2>",nextKeyword());
    	assertEquals("<p>",nextKeyword());
    	assertEquals("&nbsp;",nextKeyword());
    	assertEquals("</p>",nextKeyword());
    	assertEquals("<h3>",nextKeyword());
    	assertEquals("Título",nextKeyword());
    	
    }
    
    public void testCompositeAlmostEqualsWikiword() {
        wiki.newWikiword("Design Patterns do Vitor");
        
        text = parseText("Design Patterns do Vitor Olá!"); 
        assertEquals(3, text.getElementCount());
        assertEquals("Design Patterns do Vitor", text.getElement(0).getKeyword());
        
        wiki.newWikiword("Design Patterns do");
        
        text = parseText("Design Patterns do Olá! Design Patterns do Vitor");

        i=0;
        assertEquals("Design Patterns do", nextKeyword());
        assertEquals("Olá", nextKeyword());
        assertEquals("!", nextKeyword());
        assertEquals("Design Patterns do Vitor", nextKeyword());
    }
    
    public void testTime() {
    	text = parseText("It's 9:00 o'clock!");
    	
        assertEquals("It's", text.getElement(0).getKeyword());
        assertEquals("9:00", text.getElement(1).getKeyword());
        assertEquals("o'clock", text.getElement(2).getKeyword());
        
        // 
        text = parseText("My friend said: \"Hello Vitor!\"");

        assertEquals("My", text.getElement(0).getKeyword());
        assertEquals("friend", text.getElement(1).getKeyword());
        assertEquals("said", text.getElement(2).getKeyword());
        assertEquals(":", text.getElement(3).getKeyword());
        assertEquals("\"", text.getElement(4).getKeyword());
        assertEquals("Hello", text.getElement(5).getKeyword());
        assertEquals("Vitor", text.getElement(6).getKeyword());
        assertEquals("!", text.getElement(7).getKeyword());
        assertEquals("\"", text.getElement(8).getKeyword());
    }
    
    public void testDate() {
    	text = parseText("It will be in 07/12/2007!");
    	
        assertEquals("It", text.getElement(0).getKeyword());
        assertEquals("will", text.getElement(1).getKeyword());
        assertEquals("be", text.getElement(2).getKeyword());
        assertEquals("in", text.getElement(3).getKeyword());
        assertEquals("07/12/2007", text.getElement(4).getKeyword());
    }
    
    public void testNumber() {
    	text = parseText("Numbers: 2.52, 2,35 and 2.356,22");
    	
        assertEquals("Numbers", text.getElement(0).getKeyword());
        assertEquals(":", text.getElement(1).getKeyword());
        assertEquals("2.52", text.getElement(2).getKeyword());
        assertEquals(",", text.getElement(3).getKeyword());
        assertEquals("2,35", text.getElement(4).getKeyword());
        assertEquals("and", text.getElement(5).getKeyword());
        assertEquals("2.356,22", text.getElement(6).getKeyword());
    }
    
    public void testPercent() {
    	text = parseText("25% of those guys");
    	
        assertEquals("25%", text.getElement(0).getKeyword());
        assertEquals("of", text.getElement(1).getKeyword());
        assertEquals("those", text.getElement(2).getKeyword());
        assertEquals("guys", text.getElement(3).getKeyword());
    }

    public void tokenizerCheck(String toParse, String... keywords) {
    	text = parseText(toParse);
    	
    	int cont = 0;
    	for (String temp : keywords) {
    		assertEquals(temp, text.getElement(cont++).getKeyword());	
    	}
    }
    
    public void testAnd2() {
    	tokenizerCheck("Alou & crazy", "Alou", "&", "crazy");
    }
    
    public void testAspas() {
    	tokenizerCheck("Chande maromi's", "Chande", "maromi's");
    	tokenizerCheck("Chande maromi'", "Chande", "maromi'");
    }    
    
    public void testPlusPlus() {
    	tokenizerCheck("C++", "C++");
    }
    
    public void testDirectory() {
    	tokenizerCheck("Vitor/Vitor/Teste/Crazy", "Vitor/Vitor/Teste/Crazy");
    }
    
    public void testSamba() {
    	tokenizerCheck("\\\\Vitor\\Teste\\Vitor\\Halst", "\\\\Vitor\\Teste\\Vitor\\Halst");
    }
    
    
    public void testCode() {
    	tokenizerCheck("Pamp<i>lo</i>na", "Pamp<i>lo</i>na");
    }

}
