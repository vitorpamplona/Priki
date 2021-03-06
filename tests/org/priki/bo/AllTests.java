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
package org.priki.bo;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for " + AllTests.class.getPackage().getName());
        //$JUnit-BEGIN$
        suite.addTestSuite(WikiTest.class);
        suite.addTestSuite(HistoryTextTest.class);
        suite.addTestSuite(WikiwordElementTest.class);
        suite.addTestSuite(TextTest.class);
        suite.addTestSuite(PoliceTest.class);
        suite.addTestSuite(AccessManagerTest.class);
        suite.addTestSuite(CaseSenstiviveTest.class);
        suite.addTestSuite(LastChangesTest.class);
        suite.addTestSuite(CommentTest.class);
        suite.addTestSuite(EscortTest.class);
        suite.addTestSuite(TagsTest.class);
        suite.addTestSuite(UserTest.class);
        //$JUnit-END$
        return suite;
    }

}
