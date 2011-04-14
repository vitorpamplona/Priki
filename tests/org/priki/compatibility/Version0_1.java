package org.priki.compatibility;

import junit.framework.TestCase;

import org.priki.prevalence.Prevalence;

public class Version0_1 extends TestCase {
	public void test0_3() {
		Prevalence p = new Prevalence("Bases/0.1/");
		
		assertNotNull(p);
		assertNotNull(p.getWiki());
		assertNotNull(p.getWiki().getAdmin());
		
		assertNotNull(p.getWiki().getAdmin().getUserAdmin());
		assertEquals("admin", p.getWiki().getAdmin().getUserAdmin());
		assertNotNull(p.getWiki().getAdmin().getPassAdmin());
		assertEquals("priki", p.getWiki().getAdmin().getPassAdmin());
		
		assertTrue(p.getWiki().getAdmin().getAccessManager().isAdmin("admin"));
		assertTrue(p.getWiki().getAdmin().getAccessManager().checkLogin("admin", "priki"));
		
		assertNotNull(p.getWiki().getWikiword("Contato"));
		assertNotNull(p.getWiki().getWikiword("Contato").getDefinition());
		assertTrue(p.getWiki().getWikiword("Contato").isCaseSensitive());
	}
}
