package org.priki.format;

import java.util.Collection;

import junit.framework.TestCase;

import org.priki.bo.Text;
import org.priki.bo.Wiki;
import org.priki.bo.Wikiword;

public final class WikiwordsValidatorTest extends TestCase {

    private Wiki wiki;

    private WikiwordsValidator validator;

    @Override
    protected void setUp() throws Exception {
        wiki = new Wiki();
        validator = new WikiwordsValidator(wiki);
    }

    private Wikiword newWikiword(String keyword, boolean definition, boolean caseSensitive) {
        Wikiword word = wiki.newWikiword(keyword);
        if (definition) {
            word.setDefinition(new Text());
        }
        word.setCaseSensitive(caseSensitive);
        return word;
    }

    private boolean containsWikiword(String keyword, Collection<Wikiword> words) {
        for (Wikiword wikiword : words) {
            if (wikiword.getKeyword().equals(keyword)) {
                return true;
            }
        }
        return false;
    }

    public void testWikiwordCaseSensitive() {
        newWikiword("TESTE", false, true);
        newWikiword("teste", false, false);
        newWikiword("Teste", true, true);
        newWikiword("TestE", true, false);
        
        // conflicting with "TestE"
        Collection<Wikiword> ret = validator.getConflictingWikiwords("TESTE", true);
        assertEquals(1, ret.size());
        assertTrue(containsWikiword("TestE", ret));
        
        // conflicting with "TestE"
        ret = validator.getConflictingWikiwords("teste", true);
        assertEquals(1, ret.size());
        assertTrue(containsWikiword("TestE", ret));
        
        // conflicting with "TestE"
        ret = validator.getConflictingWikiwords("Teste", true);
        assertEquals(1, ret.size());
        assertTrue(containsWikiword("TestE", ret));
        
        // none conflicting
        ret = validator.getConflictingWikiwords("TestE", true);
        assertEquals(0, ret.size());
    }

    public void testWikiwordCaseInsensitive() {
        newWikiword("TESTE", false, true);
        newWikiword("teste", false, false);
        newWikiword("Teste", true, true);
        newWikiword("TestE", true, false);
        
        // conflicting with "Teste", "TestE"
        Collection<Wikiword> ret = validator.getConflictingWikiwords("TESTE", false);
        assertEquals(2, ret.size());
        assertTrue(containsWikiword("Teste", ret));
        assertTrue(containsWikiword("TestE", ret));
        
        // conflicting with "Teste", "TestE"
        ret = validator.getConflictingWikiwords("teste", false);
        assertEquals(2, ret.size());
        assertTrue(containsWikiword("Teste", ret));
        assertTrue(containsWikiword("TestE", ret));
        
        // conflicting with "TestE"
        ret = validator.getConflictingWikiwords("Teste", false);
        assertEquals(1, ret.size());
        assertTrue(containsWikiword("TestE", ret));
        
        // conflicting with "Teste"
        ret = validator.getConflictingWikiwords("TestE", false);
        assertEquals(1, ret.size());
        assertTrue(containsWikiword("Teste", ret));
    }
    
    public void testMatchWikiwords() {
    	assertTrue(validator.matchWikiwords("Something é Test"));	
    	assertTrue(validator.matchWikiwords("Something ç Test"));
    	assertTrue(validator.matchWikiwords("Something ã Test"));	

    
    }
    
}
