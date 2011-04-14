package org.priki.bo;

import junit.framework.TestCase;

public class TagsTest extends TestCase {
	Wiki all;

	protected void setUp() throws Exception {
		super.setUp();

		all = new Wiki();
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		all = null;
	}

	public void testTagsAndRelatedWords() {
		Wikiword scorpion = all.newWikiword("Scorpion");
		Wikiword animal = all.newWikiword("Animal");
		Wikiword aaa = all.newWikiword("Aaa");
		User vitor = all.getAdmin().getAccessManager().getUser("Vitor");

		Text text = new Text(vitor);
		text.add(animal);
		scorpion.setDefinition(text);

		text = new Text(vitor);
		text.add(animal);
		aaa.setDefinition(text);

		text = new Text(vitor);
		text.add(animal);
		aaa.addComment(text);
		
		// Without tags
		assertEquals(0, scorpion.getTags().size());
		assertFalse(scorpion.getTags().contains(animal));
		// Related running
		assertTrue(animal.getRelated().contains(scorpion));
		assertTrue(animal.getRelated().contains(aaa));
		// Order of related running
		assertEquals(aaa, animal.getRelated().toArray()[0]);
		assertEquals(scorpion, animal.getRelated().toArray()[1]);
		
		scorpion.addTag(animal);
		text = new Text(vitor);
		text.add(animal);
		scorpion.addComment(text);
		
		// Has a tag
		assertEquals(1, scorpion.getTags().size());
		assertTrue(scorpion.getTags().contains(animal));
		// The tag is related 
		assertTrue(animal.getRelated().contains(scorpion));
		assertEquals(2, animal.getRelated().size());
		// Invert the order. Now scorpion has 3 related word "Animal" and 
		// aaa has only 2.
		assertEquals(scorpion, animal.getRelated().toArray()[0]);
		assertEquals(aaa, animal.getRelated().toArray()[1]);
		
		scorpion.removeTag(animal);

		// Remove tag
		assertFalse(scorpion.getTags().contains(animal));
		assertTrue(animal.getRelated().contains(scorpion));
		assertEquals(2, animal.getRelated().size());
		
		// Inverted
		scorpion.removeComment(0);
		
		assertEquals(aaa, animal.getRelated().toArray()[0]);
		assertEquals(scorpion, animal.getRelated().toArray()[1]);	
		
		// Adding other tag tag
		scorpion.addTag(all.newWikiword("Test"));
		
		assertTrue(all.getWikiword("Test").getRelated().contains(scorpion));
		assertEquals(2, animal.getRelated().size());
		assertEquals(aaa, animal.getRelated().toArray()[0]);
		assertEquals(scorpion, animal.getRelated().toArray()[1]);		
	}
}
