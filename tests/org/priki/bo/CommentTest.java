package org.priki.bo;

import junit.framework.TestCase;

public class CommentTest extends TestCase {
	Wiki all;

	protected void setUp() throws Exception {
		super.setUp();

		all = new Wiki();
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		all = null;
	}

	public void testCommentAndRelatedWords() {
		Wikiword scorpion = all.newWikiword("Scorpion");
		User vitor = all.getAdmin().getAccessManager().getUser("Vitor");
		
        Text definitionOfScorpion = new Text(vitor);
        definitionOfScorpion.add(all.newWikiword("Venom"));
        definitionOfScorpion.add(all.newWikiword("Animal"));
        definitionOfScorpion.add(all.newElement("."));
        
        scorpion.setCaseSensitive(true);
        scorpion.setDefinition(definitionOfScorpion);
        
        // Checking Definition
        assertEquals(scorpion.getDefinition(), definitionOfScorpion);
        // Checking Related by Definition
        assertTrue(all.getWikiword("Venom").getRelated().contains(scorpion));

        Text commentTest = new Text(vitor);
        commentTest.add(all.newWikiword("Test"));
        
        scorpion.addComment(commentTest);

        // Checking Comment
        assertTrue(scorpion.getComments().contains(commentTest));
        // Checking Related by Comment
        assertNotNull(all.getWikiword("Test"));
        assertNotNull(all.getWikiword("Test").getRelated());
        assertEquals(1, all.getWikiword("Test").getRelated().size());
        assertTrue(all.getWikiword("Test").getRelated().contains(scorpion));
        
        
        scorpion.removeComment(0);
        
        // Checking Remove Comment
        assertFalse(scorpion.getComments().contains(commentTest));
        // Checking Remove Related by Remove Comment
        assertFalse(all.getWikiword("Test").getRelated().contains(scorpion));
        
        scorpion.addComment(commentTest);

        Text anotherCommentTest = new Text(vitor);
        anotherCommentTest.add(all.newWikiword("Test"));
        
        scorpion.addComment(anotherCommentTest);
        
        assertTrue(all.getWikiword("Test").getRelated().contains(scorpion));
        
        scorpion.removeComment(0);

        // Two comment with the same word: "Test"
        // Checking if it has the related word when we remove 
        // one of those comments. 
        assertTrue(all.getWikiword("Test").getRelated().contains(scorpion));
	}
}
