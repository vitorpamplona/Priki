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
package org.priki.format;

import org.priki.bo.Element;
import org.priki.bo.Text;
import org.priki.bo.Wikiword;

/** 
 * 
 * Format a Text object into a String.  
 *
 * @author <a href="mailto:vitor@babaxp.org">Vitor Fernando Pamplona</a>
 *
 * @since 14/10/2005
 * @version $Id: $
 */
public class HtmlFormatter implements Formatter {
   
    private String path;

    public HtmlFormatter() {
        this.path = "";
    }
    
    public HtmlFormatter(String path) {
        this.path = path;
    }
    
    public String formatWithoutLinks(Text text) {
        return format(text, false);
    }
    
    public String format(Text text) {
        return format(text, true);
    }
    
    public String format(Text text, boolean withLink) {
        StringBuilder builder = new StringBuilder();
        
        Element elem;
        Element prevElem = null;
        
        boolean simpleAspasOpened = false;
        boolean doubleAspasOpened = false;
        boolean nextWithoutSpace = false;
        for (int i=0; i<text.getElementCount(); i++) {
            elem = text.getElement(i);
                       
           	if (nextWithoutSpace) {
           		nextWithoutSpace = false;
           		//builder.append("");
           	} else if (withoutSpaceAfter(prevElem) || withoutSpaceBefore(elem)) {
           		//builder.append("");
           	} else if (isDoubleAspas(elem) && doubleAspasOpened) {
           		//builder.append("");
           	} else if (isSimpleAspas(elem) && simpleAspasOpened) {
           		//builder.append("");
           	} else {
           		if (i>0)
                   builder.append(" ");
           	}
           	
            if (isSimpleAspas(elem)) {
            	simpleAspasOpened = !simpleAspasOpened;
           		if (simpleAspasOpened) {
           			nextWithoutSpace = true;
           		}
           	} else if (isDoubleAspas(elem)) { 
           		doubleAspasOpened = !doubleAspasOpened;
           		if (doubleAspasOpened) {
           			nextWithoutSpace = true;
           		}
           	}           	
           	
            if (withLink) {
                if (elem.isALink()) {
                    builder.append("<a href=\"" + elem.getKeyword() + "\" target=\"_blank\">" + elem.getKeyword() + "</a>");
                } else if (elem.isAnEmail()) {   
                	builder.append("<a href=\"mailto:" + elem.getKeyword() + "\">" + elem.getKeyword() + "</a>");                	
                } else if (elem instanceof Wikiword && ((Wikiword)elem).hasDefinition()) {
                    builder.append("<a href=\"" + path + "/wiki/" + elem.getKeyword() + "\">" + elem.getKeyword() +"</a>");
                } else {
                	builder.append(elem.getKeyword());    
                }
            } else {
                builder.append(elem.getKeyword());                
            }
            
            prevElem = elem;
        }
               
        return builder.toString().trim();
    }
    
    public boolean isSimpleAspas(Element e) {
    	if (e == null) return false;
    	return e.getKeyword().equals("\'");
    }
    
    public boolean isDoubleAspas(Element e) {
    	if (e == null) return false;
    	return e.getKeyword().equals("\"");
    }
 
    public boolean withoutSpaceBefore (Element e) {
        if (e == null) return false;
        return e.getKeyword().matches("[\\!\\?\\,\\.\\;\\:\\]\\)\\}]");
    }
    
    public boolean withoutSpaceAfter (Element e) {
        if (e == null) return false;
        return e.getKeyword().matches("[\\(\\{\\[]");
    }
}
