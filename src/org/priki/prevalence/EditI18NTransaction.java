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

import org.priki.bo.I18N;
import org.priki.bo.Wiki;

public class EditI18NTransaction extends PrikiTransaction {

    public static final long serialVersionUID= 1L;
    
	private String oldLanguage;
	private String oldKey;
	private String language;
	private String key;
	private String text;
    
    public EditI18NTransaction(String oldLanguage, String oldKey,
    		String language, String key, String text) {
    	this.language = language;
    	this.key = key;
    	this.oldLanguage = oldLanguage;
    	this.oldKey = oldKey;
    	this.text = text;
    }
    
    public void executeOn(Wiki wiki) {
    	I18N i18nItem = wiki.getAdmin().getI18NOverride(oldLanguage, oldKey);
    	i18nItem.setKey(key);
    	i18nItem.setLanguage(language);
    	i18nItem.setText(text);
    }
    
}
