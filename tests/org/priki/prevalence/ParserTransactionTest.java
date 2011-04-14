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

import java.awt.List;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.priki.bo.Wikiword;
import org.priki.bo.Wikiword.Visibility;
import org.priki.prevalence.exceptions.ConflictingWikiwordsException;

public class ParserTransactionTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        Prevalence.getInstance().execute(new ClearWikiTransaction());
    }

    public void testExecuteOnWiki() {
        Prevalence p = Prevalence.getInstance();
        ParserTransaction transaction = new ParserTransaction("user vitor", "JavaFree", "Java on-line community", true);
        p.execute(transaction);

        Wikiword word = p.getWiki().getWikiword("JavaFree");

        assertNotNull(word);
        assertEquals("JavaFree", word.getKeyword());
        assertEquals("user vitor", word.getLastDefinitionUser());
        assertNotNull(word.getDefinition());
    }

    public void testCompositeWikiword() {
        Prevalence p = Prevalence.getInstance();

        ParserTransaction transaction = new ParserTransaction("user vitor", "JavaFree", "Java on-line community. Also Know as Java Free", true);
        p.execute(transaction);

        transaction = new ParserTransaction("user vitor", "Java Free", "The same of JavaFree", true);
        p.execute(transaction);

        Wikiword word = p.getWiki().getWikiword("Java Free");

        assertNotNull(word);
        assertEquals("Java Free", word.getKeyword());
        assertEquals("user vitor", word.getLastDefinitionUser());
        assertNotNull(word.getDefinition());
        assertEquals(4, word.getDefinition().getElementCount());
        assertNotNull(((Wikiword) word.getDefinition().getElement(3)).getDefinition());

        word = p.getWiki().getWikiword("JavaFree");
        assertEquals(8, word.getDefinition().getElementCount());
        assertNotNull(((Wikiword) word.getDefinition().getElement(7)).getDefinition());
        assertEquals("Java Free", ((Wikiword) word.getDefinition().getElement(7)).getKeyword());
    }
    
    public void testCompositeRenamingSelfLinkedWikiword() {
        Prevalence p = Prevalence.getInstance();

        // Inserting pages. 
        ParserTransaction transaction = new ParserTransaction("user vitor", null, "Nova Página Composta", "Nova Página Composta. Java on-line community. Also Know as Java Free", true, Visibility.Public, new ArrayList<String>());
        p.execute(transaction);

        Wikiword word = p.getWiki().getWikiword("Nova Página Composta");

        assertNotNull(word);
        assertEquals("Nova Página Composta", word.getKeyword());
        assertEquals("user vitor", word.getLastDefinitionUser());
        assertNotNull(word.getDefinition());
        assertEquals(1, word.getRelated().size());
        assertEquals("Nova Página Composta", word.getRelated().iterator().next().getKeyword());
        
        transaction = new ParserTransaction("user vitor", "Nova Página Composta", "Nova Página Composta 2", "Nova Página Composta. Java on-line community. Also Know as Java Free", true, Visibility.Public, new ArrayList<String>());
        p.execute(transaction);

        word = p.getWiki().getWikiword("Nova Página Composta");
        assertNotNull(word);
        assertNull(word.getDefinition());
        
        word = p.getWiki().getWikiword("Nova Página Composta 2");
        
        assertNotNull(word);
        assertNotNull(word.getDefinition());
        assertEquals(0, word.getRelated().size());

        transaction = new ParserTransaction("user vitor", "Nova Página Composta 2", "Nova Página Composta 3", "Nova Página Composta 3. Java on-line community. Also Know as Java Free", true, Visibility.Public, new ArrayList<String>());
        p.execute(transaction);

        word = p.getWiki().getWikiword("Nova Página Composta 2");
        // The word should be deleted
        assertNull(word);
        
        word = p.getWiki().getWikiword("Nova Página Composta 3");
        
        assertNotNull(word);
        assertNotNull(word.getDefinition());
        assertEquals(1, word.getRelated().size());    
        
        transaction = new ParserTransaction("user vitor", "Non-exist", "Nova Página Composta 3", "Nova Página Composta 3. Java on-line community. Also Know as Java Free", true, Visibility.Public, new ArrayList<String>());
        p.execute(transaction);

        word = p.getWiki().getWikiword("Non-exist");
        assertNull(word);
        
        word = p.getWiki().getWikiword("Nova Página Composta 3");
        
        assertNotNull(word);
        assertNotNull(word.getDefinition());
        assertEquals(1, word.getRelated().size());           
    }    
    
    public void testCompositeWikiwordSelfRelationship() {
        Prevalence p = Prevalence.getInstance();

        ParserTransaction transaction = new ParserTransaction("user vitor", "JavaFree is a virtual community", "JavaFree is a virtual community. Java on-line community. Also Know as Java Free", true, Visibility.Public);
        p.execute(transaction);

        Wikiword word = p.getWiki().getWikiword("JavaFree is a virtual community");

        assertNotNull(word);
        assertEquals("JavaFree is a virtual community", word.getDefinition().getElement(0).getKeyword());
        assertEquals(".", word.getDefinition().getElement(1).getKeyword());
        assertEquals("Java", word.getDefinition().getElement(2).getKeyword());
        assertEquals("on-line", word.getDefinition().getElement(3).getKeyword());
    }
    
    public void testCommentRecompilation() {
        Prevalence p = Prevalence.getInstance();

        PrikiTransaction transaction = new ParserTransaction("user vitor", "Page", "JavaFree is a virtual community. ", true, Visibility.Public);
        p.execute(transaction);

        transaction = new AddCommentTransaction("user vitor", "Page", "JavaFree is a virtual community. ");
        p.execute(transaction);
        
        transaction = new ParserTransaction("user vitor", "JavaFree is a virtual community", "JavaFree is a virtual community. Java on-line community. Also Know as Java Free", true, Visibility.Public);
        p.execute(transaction);

        Wikiword word = p.getWiki().getWikiword("JavaFree is a virtual community");

        assertNotNull(word);
        assertEquals("JavaFree is a virtual community", word.getDefinition().getElement(0).getKeyword());
        assertEquals(".", word.getDefinition().getElement(1).getKeyword());
        assertEquals("Java", word.getDefinition().getElement(2).getKeyword());
        assertEquals("on-line", word.getDefinition().getElement(3).getKeyword());
        
        word = p.getWiki().getWikiword("Page");

        assertNotNull(word);
        assertEquals("JavaFree is a virtual community", word.getDefinition().getElement(0).getKeyword());
        assertEquals("user vitor", word.getDefinition().getPostUser());
        
        assertEquals(1, word.getComments().size());
        assertEquals("JavaFree is a virtual community", word.getComments().get(0).getElement(0).getKeyword());
        assertEquals("user vitor", word.getComments().get(0).getPostUser());
    }
    
    public void testRelatedContentRecompilationCaseInsensitive() {
        Prevalence p = Prevalence.getInstance();

        PrikiTransaction transaction = new ParserTransaction("user vitor", "Page", "JavaFree. ", false, Visibility.Public);
        p.execute(transaction);

        transaction = new AddCommentTransaction("user vitor", "Page", "JavaFree cool. ");
        p.execute(transaction);
        
        transaction = new ParserTransaction("user vitor", "JavaFree", "Some text", false, Visibility.Public);
        p.execute(transaction);

        Wikiword page = p.getWiki().getWikiword("Page");
        Wikiword javafree = p.getWiki().getWikiword("JavaFree");
        
        assertEquals(1, javafree.getRelated().size());
        assertEquals("Page", ((Wikiword)javafree.getRelated().toArray()[0]).getKeyword());
        
        // renaming with case insensitive
        transaction = new ParserTransaction("user vitor", "Page", "Another Page", "JavaFree is a virtual community. ", false, Visibility.Public, new ArrayList<String>());
        p.execute(transaction);
        
        page = p.getWiki().getWikiword("Page");
        // page should be deleted
        assertNull(page);
        

        Wikiword anotherPage = p.getWiki().getWikiword("Another Page");
        assertNotNull(anotherPage);
        assertTrue(anotherPage.hasDefinition());
        assertEquals(0, anotherPage.getTags().size());
        assertEquals(1, anotherPage.getComments().size());
        assertEquals(0, anotherPage.getRelated().size());
        
        assertEquals("Another Page", ((Wikiword)javafree.getRelated().toArray()[0]).getKeyword());
        assertEquals(1, javafree.getRelated().size());

        transaction = new ParserTransaction("user vitor", "Another Page", "Page", "JavaFree is a virtual community. ", true, Visibility.Public, new ArrayList<String>());
        p.execute(transaction);

        assertEquals("Page", ((Wikiword)javafree.getRelated().toArray()[0]).getKeyword());
        assertEquals(1, javafree.getRelated().size());
        
        page = p.getWiki().getWikiword("Page");
        assertNotNull(page);
        assertTrue(page.hasDefinition());
        assertNotNull(anotherPage);
        assertFalse(anotherPage.hasDefinition());
        
        transaction = new ParserTransaction("user vitor", "Page", "page", "JavaFree is a virtual community. ", true, Visibility.Public, new ArrayList<String>());
        p.execute(transaction);  
        
        assertNotNull(p.getWiki().getWikiword("page"));
        assertTrue(p.getWiki().getWikiword("page").hasDefinition());
        assertEquals("page", ((Wikiword)javafree.getRelated().toArray()[0]).getKeyword());
        assertNull(p.getWiki().getWikiword("Page"));

        // Case insensitive
        transaction = new ParserTransaction("user vitor", "page", "Page", "JavaFree is a virtual community. ", false, Visibility.Public, new ArrayList<String>());
        p.execute(transaction);  
        
        assertNotNull(p.getWiki().getWikiword("Page"));
        assertTrue(p.getWiki().getWikiword("Page").hasDefinition());
        assertEquals("Page", ((Wikiword)javafree.getRelated().toArray()[0]).getKeyword());
        assertNull(p.getWiki().getWikiword("page"));
       

        transaction = new ParserTransaction("user vitor", "Page", "page", "JavaFree is a virtual community. ", false, Visibility.Public, new ArrayList<String>());
        p.execute(transaction);  
        
        assertNotNull(p.getWiki().getWikiword("page"));
        assertTrue(p.getWiki().getWikiword("page").hasDefinition());
        assertEquals("page", ((Wikiword)javafree.getRelated().toArray()[0]).getKeyword());
        assertNull(p.getWiki().getWikiword("Page"));
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
    
    public void testStrangeNullPointerException() {
    	Prevalence p = Prevalence.getInstance();
    	
    	ArrayList<String> tags = new ArrayList<String>();
    	tags.add("História da Ciência");
    	
        ParserTransaction transaction = new ParserTransaction("vfpamp", "", "Não Sabemos Mais Pensar", "Com", true, Visibility.Public, tags);
        p.execute(transaction);
    }

}
