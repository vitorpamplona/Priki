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

import org.priki.bo.Police;

public class GoodUserTransactionTest extends TestCase {

    public void testExecuteOnWiki() {
        Prevalence p = Prevalence.getInstance();
        GoodUserTransaction transaction = new GoodUserTransaction("user IP");
        p.execute(transaction);
        
        Police police = p.getWiki().getPolice();
        
        assertTrue(!police.isBadUser("user IP"));
    }

}
