package org.priki.bo;

import junit.framework.TestCase;

public class EscortTest extends TestCase {
	Wiki all;

	protected void setUp() throws Exception {
		super.setUp();

		all = new Wiki();
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		all = null;
	}

	public User newUser(String name) {
		User u = all.getAdmin().getAccessManager().getUser(name);
		if (u == null) {
			u = new User(name, "password", "completeName", "email"); 
			all.getAdmin().getAccessManager().addUser(u);
			u = all.getAdmin().getAccessManager().getUser(name);
		}
		return u;
	}
	
	public void testEscortAutomaticInclusion() {
		Wikiword scorpion = all.newWikiword("Scorpion");
		User vitor = newUser("Vitor");
		User paulo = newUser("Paulo");
		
        Text definitionOfScorpion = new Text(vitor);
        definitionOfScorpion.add(all.newWikiword("Venom"));
        definitionOfScorpion.add(all.newWikiword("Animal"));
        definitionOfScorpion.add(all.newElement("."));
        
        scorpion.setDefinition(definitionOfScorpion);
        
        assertEquals(1, scorpion.getEscortWikiWord().size());
        assertTrue(scorpion.getEscortWikiWord().contains(vitor));

        // seting another text
        Text anotherDefinitionOfScorpion = new Text("127.0.0.1");
        anotherDefinitionOfScorpion.add(all.newWikiword("Animal"));
        anotherDefinitionOfScorpion.add(all.newWikiword("with"));
        anotherDefinitionOfScorpion.add(all.newWikiword("six"));

        scorpion.setDefinition(anotherDefinitionOfScorpion);
        
        assertEquals(1, scorpion.getEscortWikiWord().size());
        assertTrue(scorpion.getEscortWikiWord().contains(vitor));

        // backing the history
        scorpion.setDefinition(anotherDefinitionOfScorpion);
        
        assertEquals(1, scorpion.getEscortWikiWord().size());
        assertTrue(scorpion.getEscortWikiWord().contains(vitor));        
        
        // adding a comment
        Text comment = new Text(paulo);
        comment.add(all.newWikiword("Simple"));
        comment.add(all.newWikiword("comment"));

        scorpion.addComment(comment);
        
        assertEquals(2, scorpion.getEscortWikiWord().size());
        assertTrue(scorpion.getEscortWikiWord().contains(vitor));
        assertTrue(scorpion.getEscortWikiWord().contains(paulo));
        
        scorpion.removeComment(0);
        
        assertEquals(2, scorpion.getEscortWikiWord().size());
        assertTrue(scorpion.getEscortWikiWord().contains(vitor));
        assertTrue(scorpion.getEscortWikiWord().contains(paulo));        
	}
	
	public void testEscortManuallyInclusion() {
		Wikiword fish = all.newWikiword("Fish");
		User vitor = newUser("Vitor");
		User paulo = newUser("Paulo");
		
		fish.addEscortWikiword(vitor);
		
        assertEquals(1, fish.getEscortWikiWord().size());
        assertTrue(fish.getEscortWikiWord().contains(vitor));

        fish.addEscortWikiword(paulo);
        
        assertEquals(2, fish.getEscortWikiWord().size());
        assertTrue(fish.getEscortWikiWord().contains(vitor));
        assertTrue(fish.getEscortWikiWord().contains(paulo));
        
        fish.removeEscortWikiword(paulo);
        
        assertEquals(1, fish.getEscortWikiWord().size());
        assertTrue(fish.getEscortWikiWord().contains(vitor));
        assertFalse(fish.getEscortWikiWord().contains(paulo));        
	}	
	
	public void testCheckEscortingGlobal() {
		User vitor = newUser("Vitor");
		
		assertEquals(0, all.getAdmin().getAccessManager().getEscortingUsers().size());
		
		vitor.setEscortWiki(true);
		
		assertEquals(1, all.getAdmin().getAccessManager().getEscortingUsers().size());
	}
	
	public void testCheckEscortingWithPermittions() {
		Wikiword fish = all.newWikiword("Fish");
		User vitor = newUser("Vitor");
		User paulo = newUser("Paulo");
		User pedro = newUser("Pedro");
		
		vitor.setEscortWiki(true);
		
		Text definition = new Text(paulo);
        definition.add(all.newWikiword("Test"));
		fish.setDefinition(definition);

		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(paulo, fish).contains(vitor));
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(paulo, fish).contains(paulo));
		assertEquals(2, all.getAdmin().getAccessManager().getEscortingUsers(paulo, fish).size());
		
		definition = new Text(vitor);
        definition.add(all.newWikiword("Test"));
		fish.setDefinition(definition);
		
		assertEquals(2, all.getAdmin().getAccessManager().getEscortingUsers(vitor, fish).size());
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(vitor, fish).contains(vitor));
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(vitor, fish).contains(paulo));
		
		definition = new Text(pedro);
        definition.add(all.newWikiword("Test"));
		fish.setDefinition(definition);
		
		assertEquals(3, all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).size());
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).contains(vitor));
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).contains(paulo));
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).contains(pedro));
		
		fish.setVisibility(Wikiword.Visibility.Admin);
		
		assertEquals(0, all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).size());
		
		all.getAdmin().getAccessManager().addAdmin(pedro.getIdentifier());
		
		assertEquals(1, all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).size());
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).contains(pedro));		
	}
	
	public void testCheckEscortingInComments() {
		Wikiword fish = all.newWikiword("Fish");
		User vitor = newUser("Vitor");
		User paulo = newUser("Paulo");
		User pedro = newUser("Pedro");
		
		vitor.setEscortWiki(true);
		
		Text definition = new Text(paulo);
        definition.add(all.newWikiword("Test"));
		fish.setDefinition(definition);

		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(paulo, fish).contains(vitor));
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(paulo, fish).contains(paulo));
		assertEquals(2, all.getAdmin().getAccessManager().getEscortingUsers(paulo, fish).size());
		
		definition = new Text(vitor);
        definition.add(all.newWikiword("Test"));
		fish.addComment(definition);
		
		assertEquals(2, all.getAdmin().getAccessManager().getEscortingUsers(vitor, fish).size());
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(vitor, fish).contains(vitor));
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(vitor, fish).contains(paulo));
		
		definition = new Text(pedro);
        definition.add(all.newWikiword("Test"));
		fish.addComment(definition);
		
		assertEquals(3, all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).size());
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).contains(vitor));
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).contains(paulo));
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).contains(pedro));
		
		fish.setVisibility(Wikiword.Visibility.Admin);
		
		assertEquals(0, all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).size());
		
		all.getAdmin().getAccessManager().addAdmin(pedro.getIdentifier());
		
		assertEquals(1, all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).size());
		assertTrue(all.getAdmin().getAccessManager().getEscortingUsers(pedro, fish).contains(pedro));		
	}	
}
