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

import junit.framework.TestCase;

import org.priki.bo.AccessManager;
import org.priki.bo.Wikiword;
import org.priki.prevalence.exceptions.PoliceException;

public class SecurityTransactionTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        Prevalence.getInstance().execute(new ClearWikiTransaction());
    }
    
    public void testReadonly() {
        Prevalence p = Prevalence.getInstance();

        AdminTransaction admTransaction = new AdminTransaction("novoSite", "o novo site", "", "en_US", 10, true,false, true, false, false, AccessManager.SignUp.AsEditor, Wikiword.Visibility.Public, false);
        p.execute(admTransaction);

        Wikiword word = p.getWiki().getWikiword("NovaWikiword");
        assertNull(word);

        ParserTransaction transaction = new ParserTransaction("user vitor", "NovaWikiword", "Java on-line community", true);
        
        try {
        	p.execute(transaction);
        	fail("It must throw an exception");
        } catch (PoliceException e) {
        	
        }

        word = p.getWiki().getWikiword("NovaWikiword");
        assertNull(word);
    }

    public void testSecurityAnonymous() {
        Prevalence p = Prevalence.getInstance();

        AdminTransaction admTransaction = new AdminTransaction("novoSite", "o novo site", "", "en_US", 10, true, false, false, false, false, AccessManager.SignUp.AsEditor, Wikiword.Visibility.Public, false);
        p.execute(admTransaction);

        Wikiword word = p.getWiki().getWikiword("NovaWikiword 2");
        assertNull(word);

        ParserTransaction transaction = new ParserTransaction("user vitor", "NovaWikiword 2", "Java on-line community", true);
        try {
        	p.execute(transaction);
        	fail("It must throw an exception");
        } catch (PoliceException e) {
        	
        }

        word = p.getWiki().getWikiword("NovaWikiword 2");

        assertNull(word);

        admTransaction = new AdminTransaction("novoSite", "o novo site", "", "en_US", 10, true, false, false, true, false, AccessManager.SignUp.AsEditor, Wikiword.Visibility.Public, false);
        p.execute(admTransaction);

        p.execute(transaction);

        word = p.getWiki().getWikiword("NovaWikiword 2");

        assertNotNull(word);
    }

    public void testSecurityNonEditor() {
        Prevalence p = Prevalence.getInstance();

        AdminTransaction admTransaction = new AdminTransaction("novoSite", "o novo site", "", "en_US", 10,true,false, false, false, false, AccessManager.SignUp.AsEditor, Wikiword.Visibility.Public, false);
       	p.execute(admTransaction);

        Wikiword word = p.getWiki().getWikiword("NovaWikiword 3");
        assertNull(word);

        ParserTransaction transaction = new ParserTransaction("user vitor", "NovaWikiword 3", "Java on-line community", true);
        try { 
        	p.execute(transaction);
        	fail("It must throw an exception");
        } catch (PoliceException e) {
        	
        }

        word = p.getWiki().getWikiword("NovaWikiword 3");

        assertNull(word);

        AddUserTransaction userTransaction = new AddUserTransaction("user vitor", "1234", "Vitor", "vitor@babaxp.org");
        p.execute(userTransaction);

        p.execute(transaction);

        word = p.getWiki().getWikiword("NovaWikiword 3");

        assertNotNull(word);
    }

}
