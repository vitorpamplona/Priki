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
import org.priki.bo.Wikiword;
import org.priki.prevalence.exceptions.UserNotFoundException;
import org.priki.prevalence.exceptions.WikiwordNotFoundException;

public class RemoveEscortWikiwordTransaction extends PrikiTransaction {

    public static final long serialVersionUID= 1L;
    
    private String user;
    private String wikiword; 
    
    public RemoveEscortWikiwordTransaction(String user, String wikiword) {
        this.user = user;
        this.wikiword = wikiword;
    }
    
    public void executeOn(Wiki wiki) {
        Wikiword word = wiki.getWikiword(wikiword);
        User u = wiki.getAdmin().getAccessManager().getUser(user);
        
        if (word == null) throw new WikiwordNotFoundException();
        if (u == null) throw new UserNotFoundException();
        
        word.removeEscortWikiword(u);
            
    }
}
