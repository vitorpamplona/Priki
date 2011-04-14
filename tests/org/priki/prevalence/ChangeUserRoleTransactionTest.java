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

public class ChangeUserRoleTransactionTest extends TestCase {

    public void testChangeUserRole() {
        Prevalence p = Prevalence.getInstance();
        AdminTransaction adminTransaction = new AdminTransaction("siteName", "slogan", "adminBasePath", "en_US", 12, false, false, false, false, false, AccessManager.SignUp.AsUser, Wikiword.Visibility.Public, false);
        p.execute(adminTransaction);
        
        Admin admin = p.getWiki().getAdmin();
        
        AddUserTransaction trans = new AddUserTransaction("vfpamp", "1234", "Vitor", "vitor@babaxp.org");
        p.execute(trans);
        
        assertTrue(admin.getAccessManager().isUser("vfpamp"));
        assertFalse(admin.getAccessManager().isReader("vfpamp"));
        assertFalse(admin.getAccessManager().isEditor("vfpamp"));
        assertFalse(admin.getAccessManager().isAdmin("vfpamp"));
        
        ChangeUserRoleTransaction change = new ChangeUserRoleTransaction("vfpamp", "", "", true, false, false, false);
        p.execute(change);

        assertTrue(admin.getAccessManager().isUser("vfpamp"));
        assertTrue(admin.getAccessManager().isReader("vfpamp"));
        assertFalse(admin.getAccessManager().isEditor("vfpamp"));
        assertFalse(admin.getAccessManager().isAdmin("vfpamp"));
        
        change = new ChangeUserRoleTransaction("vfpamp",  "", "", true, true, false, false);
        p.execute(change);
        
        assertTrue(admin.getAccessManager().isUser("vfpamp"));
        assertTrue(admin.getAccessManager().isReader("vfpamp"));
        assertTrue(admin.getAccessManager().isEditor("vfpamp"));
        assertFalse(admin.getAccessManager().isAdmin("vfpamp"));
        assertFalse(admin.getAccessManager().getUser("vfpamp").isEscortWiki());
        
        change = new ChangeUserRoleTransaction("vfpamp",  "", "", true, true, true, true);
        p.execute(change);
        
        assertTrue(admin.getAccessManager().isUser("vfpamp"));
        assertTrue(admin.getAccessManager().isReader("vfpamp"));
        assertTrue(admin.getAccessManager().isEditor("vfpamp"));
        assertTrue(admin.getAccessManager().isAdmin("vfpamp"));
        assertTrue(admin.getAccessManager().getUser("vfpamp").isEscortWiki());
        
        change = new ChangeUserRoleTransaction("vfpamp",  "", "", false, false, false, true);
        p.execute(change);

        change = new ChangeUserRoleTransaction("vfpamp",  "", "", false, false, true, false);
        p.execute(change);
        
        assertTrue(admin.getAccessManager().isUser("vfpamp"));
        assertTrue(admin.getAccessManager().isReader("vfpamp"));
        assertTrue(admin.getAccessManager().isEditor("vfpamp"));
        assertTrue(admin.getAccessManager().isAdmin("vfpamp"));        
    }
    
}
