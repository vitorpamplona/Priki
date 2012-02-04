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
 * @author Giovane Roslindo Kuhn - grkuhn at gmail dot com
 *
 */

package org.priki.prevalence;

import org.priki.bo.Wiki;

/**
 * Transaction to clear the wiki database and settings.<br>
 * Take care my fellow.<br>
 * 
 * @author Giovane.Kuhn
 * @since 27/02/2007
 */
public class RemoveAllCommentsTransaction extends PrikiTransaction {

    private static final long serialVersionUID = 1L;

    @Override
    public void executeOn(Wiki wiki) {
        wiki.removeAllComments();
    }

}
