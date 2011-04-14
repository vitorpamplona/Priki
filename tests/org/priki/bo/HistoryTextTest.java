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

import java.util.List;

import junit.framework.TestCase;

public class HistoryTextTest extends TestCase {

    HistoryText history; 
    
    protected void setUp() throws Exception {
        super.setUp();
        
        Wikiword wiki = new Wikiword("TESTE");
        history = new HistoryText(wiki);
    }

    public void testTextActual() {
        Text t1 = new Text("Vitor");
        
        AnonymousUser localhost = new AnonymousUser("127.0.0.1");
        
        Text t2 = new Text(localhost);
        
        User paulo = new User("Paulo");
        Text t3 = new Text(paulo);
        
        history.setText(t1);
        
        assertEquals(t1, history.getCurrent());
        assertEquals("Vitor", history.getCurrent().getPostUser());
        
        history.setText(t2);
        
        assertEquals(t2, history.getCurrent());
        assertEquals(localhost, history.getCurrent().getWhoPosted());
        
        history.setText(t3);
        
        assertEquals(t3, history.getCurrent());
        assertEquals(paulo, history.getCurrent().getWhoPosted());
    }
    
    public void testHistory() {
        Text t1 = new Text();
        Text t2 = new Text();
        Text t3 = new Text();
        
        assertEquals(0, history.getHistoryCount());
        
        history.setText(t1);
        
        assertEquals(0, history.getHistoryCount());
        
        history.setText(t2);
        
        assertEquals(1, history.getHistoryCount());
        assertEquals(t1, history.getHistory(0));
        
        history.setText(t3);
        
        assertEquals(2, history.getHistoryCount());
        assertEquals(t2, history.getHistory(0));
        assertEquals(t1, history.getHistory(1));
    }
    
    public void testRollbackByUser() {
        Text t1 = new Text("User1");
        Text t2 = new Text("User2");
        Text t3 = new Text("User3");

        history.setText(t1);
        history.setText(t2);
        history.setText(t3);
        
        assertEquals(t3, history.getCurrent());
        
        List<Text> ret= history.removeTextByUser(t3.getPostDate(), "User3");
        
        assertEquals(1, ret.size());
        assertEquals(t2, history.getCurrent());
        assertEquals(t1, history.getHistory(0));
        
        history.removeTextByUser(t2.getPostDate(), "User2");
        
        assertEquals(t1, history.getCurrent());
    }
    
    public void testRollbackAll() {
        Text t1 = new Text("User1");
        Text t2 = new Text("User1");
        Text t3 = new Text("User1");

        history.setText(t1);
        history.setText(t2);
        history.setText(t3);
        
        assertEquals(t3, history.getCurrent());
        
        List<Text> ret = history.removeTextByUser(t1.getPostDate(), "User1");
        
        assertEquals(3, ret.size());
        assertFalse(history.hasCurrent());
        assertEquals(0, history.getHistoryCount());
    }
    
    public void testRollbackByDate() throws InterruptedException {
        Text t1 = new Text("User1");
        //Thread.sleep(1500);
        Text t2 = new Text("User2");
        //Thread.sleep(1500);
        Text t3 = new Text("User3");

        history.setText(t1);
        history.setText(t2);
        history.setText(t3);
        
        assertEquals(t3, history.getCurrent());
        
        List<Text> ret = history.removeTextByUser(t1.getPostDate(), "User1");
        
        assertEquals(1, ret.size());
        assertEquals(t3, history.getCurrent());
        assertEquals(t2, history.getHistory(0));
        assertEquals(1, history.getHistoryCount());
    }
}
