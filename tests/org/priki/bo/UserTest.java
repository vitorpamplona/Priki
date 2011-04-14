package org.priki.bo;

import junit.framework.TestCase;

public class UserTest extends TestCase {
	Wiki all;

	protected void setUp() throws Exception {
		super.setUp();

		all = new Wiki();
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		all = null;
	}

	public void testUsersAndRelatedWords() {
		Wikiword scorpion = all.newWikiword("Scorpion");
		User vitor = all.getAdmin().getAccessManager().getUser("Vitor");
		// TODO: Test for related works for users. 
	}
}
