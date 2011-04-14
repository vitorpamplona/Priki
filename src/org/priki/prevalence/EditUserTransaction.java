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
import org.priki.prevalence.exceptions.InvalidPasswordException;
import org.priki.prevalence.exceptions.UserNotFoundException;

public class EditUserTransaction extends PrikiTransaction {

    public static final long serialVersionUID= 1L;
    
    private String login;
	private String oldPassword;
	private String newPassword;
	private String email;
	private String completeName;
	private boolean escortWiki;
    
    public EditUserTransaction(String login, String oldPassword, String newPassword, String completeName, String email, boolean escortWiki) {
    	this.login = login;
    	this.oldPassword = oldPassword;
    	this.newPassword = newPassword;
    	this.email = email;
    	this.completeName = completeName;
    	this.escortWiki = escortWiki;
    }
    
    public void executeOn(Wiki wiki) {
    	User user = new User(login, newPassword, completeName, email, escortWiki);

    	User registered = wiki.getAdmin().getAccessManager().getUser(login);
    	
    	if (registered == null) throw new UserNotFoundException();
    	
    	if (oldPassword.equals(registered.getPassword())) {
    		wiki.getAdmin().getAccessManager().updateUser(user);
    	} else {
    		throw new InvalidPasswordException();
    	}
    		
    }
    
}
