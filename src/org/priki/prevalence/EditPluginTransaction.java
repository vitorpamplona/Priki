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

import org.priki.bo.Plugin;
import org.priki.bo.Wiki;

public class EditPluginTransaction extends PrikiTransaction {

    public static final long serialVersionUID= 1L;
    
	private String oldName;
	private String newName;
	private int position;
	private int order;
	private String html;
    
    public EditPluginTransaction(String oldName, String newName, int position, String html, int order) {
    	this.oldName = oldName;
    	this.newName = newName;
    	this.position = position;
    	this.html = html;
    	this.order = order;
    }
    
    public void executeOn(Wiki wiki) {
    	Plugin plug = wiki.getAdmin().getPlugin(oldName);
    	plug.setName(newName);
    	plug.setHtml(html);
    	plug.setPosition(position);
    	plug.setOrder(order);
    }
    
}
