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
package org.priki.prevalence;

import org.priki.bo.User;
import org.priki.bo.Wiki;
import org.priki.prevalence.exceptions.UserNotFoundException;

public class ChangeUserRoleTransaction extends PrikiTransaction {

    public static final long serialVersionUID= 1L;
    
    private String login;
    private String email;
    private String completeName;
    private boolean isEditor;
    private boolean isReader;
    private boolean isAdmin;
    private boolean escortWiki;
    
    public ChangeUserRoleTransaction(String login, String email, String completeName, 
    		boolean  isReader, boolean isEditor, boolean isAdmin, boolean escortWiki) {
    	this.login = login;
    	this.isEditor = isEditor;
    	this.isReader = isReader;
    	this.isAdmin = isAdmin;
    	this.email = email;
    	this.escortWiki = escortWiki;
    	this.completeName = completeName;
    }
    
    public void executeOn(Wiki wiki) {
    	User user = wiki.getAdmin().getAccessManager().getUser(login);

    	if (user == null) throw new UserNotFoundException();
    	 
    	wiki.getAdmin().getAccessManager().setRoles(login, isReader, isEditor, isAdmin);
    	
    	user.setEscortWiki(escortWiki);
    	user.setCompleteName(completeName);
    	user.setEmail(email);
    }
    
}
