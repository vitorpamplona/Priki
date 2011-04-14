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
import org.priki.bo.Admin;
import org.priki.bo.Wikiword;

public class AdminTransactionTest extends TestCase {

    public void testExecuteOnWiki() {
        Prevalence p = Prevalence.getInstance();
        AdminTransaction adminTransaction = new AdminTransaction("siteName", "slogan", "adminBasePath", "en_US", 12, true, false, false, false, false, AccessManager.SignUp.AsUser, Wikiword.Visibility.Public, false);
        p.execute(adminTransaction);
        
        Admin admin = p.getWiki().getAdmin();
        
        assertEquals("siteName", admin.getSiteName());
        assertEquals("slogan", admin.getSlogan());
        assertEquals("adminBasePath", admin.getBasePath());
        assertEquals("en_US", admin.getDefaultI18n());
        assertEquals(12, admin.getLastChangesCount());
        assertEquals(true, admin.isLastChangesInItems());
        assertEquals(false, admin.isLastChangesOnlyNewPages());
        assertEquals(false, admin.getAccessManager().isReadonly());
        assertEquals(false, admin.getAccessManager().isAcceptAnonymousEditor());
        assertEquals(false, admin.getAccessManager().isAcceptAnonymousReader());
        assertEquals(Wikiword.Visibility.Public, admin.getAccessManager().getDefaultVisibility());
        assertEquals(false, admin.getAccessManager().isDefaultCaseSensitive());       
        assertEquals(AccessManager.SignUp.AsUser, admin.getAccessManager().getSignUp());
        
        
        adminTransaction = new AdminTransaction("siteName 2", "slogan 2", "adminBasePath 2", "pt_BR", 16, false, true, true, true, true, AccessManager.SignUp.AsEditor, Wikiword.Visibility.Reader, true);
        p.execute(adminTransaction);
        
        assertEquals("siteName 2", admin.getSiteName());
        assertEquals("slogan 2", admin.getSlogan());
        assertEquals("adminBasePath 2", admin.getBasePath());
        assertEquals("pt_BR", admin.getDefaultI18n());
        assertEquals(16, admin.getLastChangesCount());
        assertEquals(false, admin.isLastChangesInItems());
        assertEquals(true, admin.isLastChangesOnlyNewPages());
        assertEquals(true, admin.getAccessManager().isReadonly());
        assertEquals(true, admin.getAccessManager().isAcceptAnonymousEditor());
        assertEquals(true, admin.getAccessManager().isAcceptAnonymousReader());
        assertEquals(Wikiword.Visibility.Reader, admin.getAccessManager().getDefaultVisibility());
        assertEquals(true, admin.getAccessManager().isDefaultCaseSensitive());        
        assertEquals(AccessManager.SignUp.AsEditor, admin.getAccessManager().getSignUp());        
    }
    
}
