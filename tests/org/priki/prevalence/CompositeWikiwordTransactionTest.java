/*
 * Priki - Prevalent Wiki
 * Copyright (c) 2006 - Priki
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
package org.priki.prevalence;

import junit.framework.TestCase;

import org.priki.bo.Wikiword;
import org.priki.format.HtmlFormatter;
import org.priki.prevalence.exceptions.ConflictingWikiwordsException;

public class CompositeWikiwordTransactionTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        Prevalence.getInstance().execute(new ClearWikiTransaction());
    }
    
    public void testCaseSensitiveWikiword() {
        Prevalence p = Prevalence.getInstance();

        // Check case sensitive
        ParserTransaction transaction = new ParserTransaction("user giovane", "JavaFree", "Virtual Java community", true);
        p.execute(transaction);
        Wikiword word = p.getWiki().getWikiword("JavaFree");
        assertNotNull(word);
        assertTrue(word.isCaseSensitive());

        // Check case insensitive
        transaction = new ParserTransaction("user giovane", "JavaFree", "Virtual Java community", false);
        p.execute(transaction);
        word = p.getWiki().getWikiword("JavaFree");
        assertNotNull(word);
        assertFalse(word.isCaseSensitive());
    }

    public void testCaseInsensitiveRecompilation() {
        Prevalence p = Prevalence.getInstance();

        // Check case insensitive
        ParserTransaction transaction = new ParserTransaction("user vitor", "Java Free Cool", "Test Java Free Cool java free cool", false);
        p.execute(transaction);
        
        Wikiword word = p.getWiki().getWikiword("Java Free Cool");
        assertNotNull(word);
        assertFalse(word.isCaseSensitive());

        // Check case formatter.
        assertEquals("Test", word.getDefinition().getElement(0).getKeyword());
        assertEquals("Java Free Cool", word.getDefinition().getElement(1).getKeyword());
        assertEquals("Java Free Cool", word.getDefinition().getElement(2).getKeyword());
    }
    
    public void testCaseInsensitiveWikiword() {
        Prevalence p = Prevalence.getInstance();

        // Check case insensitive
        ParserTransaction transaction = new ParserTransaction("user giovane", "JavaFree", "Virtual Java community", false);
        p.execute(transaction);
        Wikiword word = p.getWiki().getWikiword("JavaFree");
        assertNotNull(word);
        assertFalse(word.isCaseSensitive());

        // Check case sensitive
        transaction = new ParserTransaction("user giovane", "JavaFree", "Virtual Java community", true);
        p.execute(transaction);
        word = p.getWiki().getWikiword("JavaFree");
        assertNotNull(word);
        assertTrue(word.isCaseSensitive());
    }

    public void testCaseInsensitiveCompositeWikiword() {
        Prevalence p = Prevalence.getInstance();

        // Create wikiword with definition "Design Patterns"
        ParserTransaction transaction = new ParserTransaction("user giovane", "Teste", "Testing insensitive wikiword Design Patterns", true);
        p.execute(transaction);

        // Create insensitive wikiword "design patterns" 
        transaction = new ParserTransaction("user giovane", "design patterns", "This wikiword must replace the Teste definition", false);
        p.execute(transaction);
        
        // The words Design should got be removed from wiki
        assertNull(p.getWiki().getWikiword("Design"));
        assertNull(p.getWiki().getWikiword("design"));
        
        // Check the link bewtween "design patterns" and "Teste"
        Wikiword patterns = p.getWiki().getWikiword("design patterns");
        Wikiword teste = p.getWiki().getWikiword("Teste");
        assertNotNull(patterns);
        assertNotNull(teste);
        assertEquals(1, patterns.getRelated().size());
        assertEquals(1, patterns.getRelated().getCount(teste));
        
        //FIXME This behavior is not desired, changing de user text to "design patterns"
        assertEquals("Testing insensitive wikiword design patterns", new HtmlFormatter().formatWithoutLinks(teste.getDefinition()));
    }

    public void testCaseSensitiveCompositeWikiwordChangingToInsensitive() {
        Prevalence p = Prevalence.getInstance();

        // Create wikiword with definition "Design Patterns"
        ParserTransaction transaction = new ParserTransaction("user giovane", "Teste", "Testing insensitive wikiword Design Patterns", true);
        p.execute(transaction);

        // Create sensitive wikiword "design patterns" 
        transaction = new ParserTransaction("user giovane", "design patterns", "This wikiword must replace the Teste definition", true);
        p.execute(transaction);

        // Change to insensitive 
        transaction = new ParserTransaction("user giovane", "design patterns", "This wikiword must replace the Teste definition", false);
        p.execute(transaction);
        
        // The words Design should got be removed from wiki
        assertNull(p.getWiki().getWikiword("Design"));
        assertNull(p.getWiki().getWikiword("design"));
        
        // Check the link between "design patterns" and "Teste"
        Wikiword patterns = p.getWiki().getWikiword("design patterns");
        Wikiword teste = p.getWiki().getWikiword("Teste");
        assertNotNull(patterns);
        assertNotNull(teste);
        assertEquals(1, patterns.getRelated().size());
        assertEquals(1, patterns.getRelated().getCount(teste));
        
        //FIXME This behavior is not desired, changing de user text to "design patterns"
        assertEquals("Testing insensitive wikiword design patterns", new HtmlFormatter().formatWithoutLinks(teste.getDefinition()));
    }
    
    public void testCaseInsensitiveWikiwordReplacingUndefiniedSensitiveWikiword() {
        Prevalence p = Prevalence.getInstance();

        // Create wikiword "JavaFree"
        // It instances many undefinied wikiwords "Virtual", "Java", "community" 
        ParserTransaction transaction = new ParserTransaction("user giovane", "JavaFree", "Virtual Java community", true);
        p.execute(transaction);
        assertNotNull(p.getWiki().getWikiword("Java"));

        // Create insensitive wikiword "java" to replace "Java" 
        transaction = new ParserTransaction("user giovane", "java", "Object Oriented Programming Language", false);
        p.execute(transaction);
        assertNull(p.getWiki().getWikiword("Java"));
        assertNotNull(p.getWiki().getWikiword("java"));

        // Check the link bewtween "JavaFree" and "java"
        Wikiword javafree = p.getWiki().getWikiword("JavaFree");
        Wikiword java = p.getWiki().getWikiword("java");
        assertEquals(1, java.getRelated().size());
        assertEquals(1, java.getRelated().getCount(javafree));

        //FIXME This behavior is not desired, changing de user text to "java"
        assertEquals("Virtual java community", new HtmlFormatter().formatWithoutLinks(javafree.getDefinition()));
    }

    public void testCaseSensitiveWikiwordChangingToInsensitive() {
        Prevalence p = Prevalence.getInstance();

        // Create wikiword "JavaFree" and definition with "javafree"
        ParserTransaction transaction = new ParserTransaction("user giovane", "JavaFree", "javafree is a virtual community", true);
        p.execute(transaction);
        assertNotNull(p.getWiki().getWikiword("JavaFree"));
        assertNotNull(p.getWiki().getWikiword("javafree"));

        // Changes wikiword "javafree" to insensitive 
        transaction = new ParserTransaction("user giovane", "JavaFree", "javafree is a virtual community", false);
        p.execute(transaction);
        assertNotNull(p.getWiki().getWikiword("JavaFree"));
        assertNull(p.getWiki().getWikiword("javafree"));

        // Check the link bewtween "JavaFree" and "JavaFree"
        Wikiword javafree = p.getWiki().getWikiword("JavaFree");
        assertEquals(1, javafree.getRelated().size());
        assertEquals(1, javafree.getRelated().getCount(javafree));

        //FIXME This behavior is not desired, changing de user text to "JavaFree"
        assertEquals("JavaFree is a virtual community", new HtmlFormatter().formatWithoutLinks(javafree.getDefinition()));
    }

    public void testConflictingWikiwords() {
        Prevalence p = Prevalence.getInstance();

        // Conflict between a sensitive and insensitive wikiword
        ParserTransaction transaction = new ParserTransaction("user giovane", "JavaFree", "Virtual Java community", false);
        p.execute(transaction);
        try {
            transaction = new ParserTransaction("user giovane", "JAVAFREE", "Virtual Java community", true);
            p.execute(transaction);
            fail();
        } catch (ConflictingWikiwordsException e) {
            // OK
        }

        // Conflict between a insensitive and sensitive wikiword
        transaction = new ParserTransaction("user giovane", "JavaFree", "Virtual Java community", true);
        p.execute(transaction);
        try {
            transaction = new ParserTransaction("user giovane", "javafree", "Virtual Java community", false);
            p.execute(transaction);
            fail();
        } catch (ConflictingWikiwordsException e) {
            // OK
        }
    }

}
