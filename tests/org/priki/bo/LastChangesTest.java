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
package org.priki.bo;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import junit.framework.TestCase;

public class LastChangesTest extends TestCase {

    Wiki all;

    protected void setUp() throws Exception {
        super.setUp();

        all = new Wiki();
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        all = null;
    }

    public void testLastChanged() {
    	all.getAdmin().setLastChangesOnlyNewPages(false);
    	
        Wikiword e = all.newWikiword("Vitor");
        Wikiword e1 = all.newWikiword("Vitor");
        Wikiword e2 = all.newWikiword("vai");
        Wikiword e3 = all.newWikiword("pescar");

        Text text = new Text();
        text.add(e1);
        text.add(e2);
        text.add(e3);

        e.setDefinition(text);

        assertEquals(1, all.lastChangedFull("test user").size());
        assertEquals("Vitor", all.lastChangedFull("test user").values().iterator().next().getKeyword());
    }

    public void testLastChangedWithComments() {
    	all.getAdmin().setLastChangesOnlyNewPages(false);
    	
        Wikiword e = all.newWikiword("Vitor");
        Wikiword e1 = all.newWikiword("Vitor");
        Wikiword e2 = all.newWikiword("vai");
        Wikiword e3 = all.newWikiword("pescar");

        Text textOld = new Text();
        textOld.add(e1);
        e.setDefinition(textOld);
        
        try {
			Thread.sleep(100);
		} catch (InterruptedException e4) {
			e4.printStackTrace();
		}
        
        Text text = new Text();
        text.add(e1);
        text.add(e2);
        text.add(e3);

        e.addComment(text);
        
        assertEquals(1, all.lastChangedFull("test user").size());
        assertEquals(text.getPostDate(), all.lastChangedFull("test user").values().iterator().next().getLastCommentPostDate());
    }

    public void testFilterWords() {
        Wikiword e = all.newWikiword("Vitor");
        Wikiword e1 = all.newWikiword("Vitor");
        Wikiword e2 = all.newWikiword("vai");
        Wikiword e3 = all.newWikiword("pescar");

        Text text1 = new Text("User1");
        text1.add(e1);
        text1.add(e2);
        text1.add(e3);

        //Thread.sleep(100);

        Text text2 = new Text("User2");
        text2.add(e2);
        text2.add(e1);
        text2.add(e3);

        //Thread.sleep(100);

        Text text3 = new Text("User3");
        text3.add(e1);
        text3.add(e2);
        text3.add(e3);

        e.setDefinition(text1);
        e.setDefinition(text2);
        e.setDefinition(text3);

        List<Wikiword> list = all.wikiwordsChangedAfter(text2.getPostDate());
        assertEquals(1, list.size());
    }

    public void testFilterWordsWithUser() {
        User paulo = new User("Paulo");

        Wikiword e = all.newWikiword("Vitor");
        Wikiword e1 = all.newWikiword("Vitor");
        Wikiword e2 = all.newWikiword("vai");
        Wikiword e3 = all.newWikiword("pescar");

        Text text1 = new Text(paulo);
        text1.add(e1);
        text1.add(e2);
        text1.add(e3);

        //Thread.sleep(100);

        Text text2 = new Text(paulo);
        text2.add(e2);
        text2.add(e1);
        text2.add(e3);

        //Thread.sleep(100);

        Text text3 = new Text(paulo);
        text3.add(e1);
        text3.add(e2);
        text3.add(e3);

        e.setDefinition(text1);
        e.setDefinition(text2);
        e.setDefinition(text3);

        SortedMap<Date, Wikiword> list = all.changedByUser("Paulo");
        assertEquals(1, list.size());
    }
}
