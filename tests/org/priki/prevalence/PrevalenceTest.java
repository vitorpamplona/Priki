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

import java.io.File;

import junit.framework.TestCase;

import org.priki.bo.AccessManager;
import org.priki.bo.Wikiword;

public class PrevalenceTest extends TestCase {

    public PrevalenceTest() {
        super();
    }

    public void testTransaction() {
        //Cleaning all...
        File dir = new File("BASE");
        
        if (!dir.exists()) {
        	dir.mkdir();
        }
        
        for (File journal : dir.listFiles()) {
            journal.delete();
        }
        dir.delete();
        assertFalse(dir.exists());
        
        Prevalence p = Prevalence.getInstance();
        
        assertTrue(dir.exists());
        assertEquals(0, dir.list().length);
        
        AdminTransaction admin = new AdminTransaction("siteName", "slogan", "adminBasePath", "en_US", 12, true, false,false, true, true, AccessManager.SignUp.AsEditor, Wikiword.Visibility.Public, false);
        p.execute(admin);
        
        for (File journal : dir.listFiles()) {
            assertTrue(journal.length()>0);
        }
    }
/* Muito lento
    public void testStressTransaction() throws IOException {
    	Prevalence.getInstance().close();
    	
        //Cleaning all...
        File dir = new File("BASE");
        
        if (!dir.exists()) {
        	dir.mkdir();
        }
        
        for (File journal : dir.listFiles()) {
            journal.delete();
        }
        dir.delete();
        assertFalse(dir.exists());
        
        Prevalence p = Prevalence.getNewInstance();
        
        assertTrue(dir.exists());
        assertEquals(0, dir.list().length);
        
        FileUtils file = new FileUtils();
        String text = file.readFile("../prevalence/stressFile.Power");
        
        ParserTransaction parser = new ParserTransaction("vitor", "StressTestWithMoreThan2MB", text, false);
        p.execute(parser);
        
        for (File journal : dir.listFiles()) {
            assertEquals(3694514,journal.length());
        }
        
        p.takeSnapshot();
    }*/
    
    public void testWiki() {
        assertNotNull(Prevalence.wiki());
    }

    public void testSingleton() {
        assertSame(Prevalence.getInstance(), Prevalence.getInstance());
        assertSame(Prevalence.getInstance().getWiki(), Prevalence.wiki());
    }
    
}
