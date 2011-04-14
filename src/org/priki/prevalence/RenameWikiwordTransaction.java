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

import org.priki.bo.Wiki;
import org.priki.prevalence.exceptions.WikiwordAlreadyExistsException;
import org.priki.prevalence.exceptions.WikiwordNotFoundException;

public class RenameWikiwordTransaction extends PrikiTransaction {

    public static final long serialVersionUID= 1L;
    
    private String oldWikiword;
	private String newWikiword;
    
    public RenameWikiwordTransaction(String oldWikiword, String newWikiword) {
    	this.oldWikiword = oldWikiword;
    	this.newWikiword = newWikiword;
    }
    
    public void executeOn(Wiki wiki) {
    	if (wiki.getWikiword(oldWikiword) == null)
    		throw new WikiwordNotFoundException();
    	
    	if (wiki.getWikiword(newWikiword) != null
    	&&	wiki.getWikiword(newWikiword).hasDefinition())
    		throw new WikiwordAlreadyExistsException();
    	
    	wiki.rename(oldWikiword, newWikiword);
    }
    
}