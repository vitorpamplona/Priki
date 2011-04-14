package org.priki.bo;

import junit.framework.TestCase;

public class AccessManagerTest extends TestCase {
    public void testUsers() {
        AccessManager manager = new AccessManager();

        User vitor = new User("Vitor", "passwd", "Vitor Fernando Pamplona", "vitor@babaxp.org");

        manager.addUser(vitor);

        assertFalse(manager.isAdmin(vitor));
        assertTrue(manager.isEditor(vitor));
        assertTrue(manager.isReader(vitor));

        User paulo = new User("Paulo", "passwd", "Paulo Roberto Pamplona", "paulo@algo.org");

        manager.setSignUp(AccessManager.SignUp.AsUser);
        manager.addUser(paulo);

        assertFalse(manager.isAdmin(vitor));
        assertTrue(manager.isEditor(vitor));
        assertTrue(manager.isReader(vitor));

        assertFalse(manager.isAdmin(paulo));
        assertFalse(manager.isEditor(paulo));
        assertFalse(manager.isReader(paulo));

        User klaus = new User("Klaus", "passwd", "Klaus Wuestefeld", "klaus@algo.org");

        manager.setSignUp(AccessManager.SignUp.AsReader);
        manager.addUser(klaus);

        assertFalse(manager.isAdmin(klaus));
        assertFalse(manager.isEditor(klaus));
        assertTrue(manager.isReader(klaus));

        User klaus2 = new User("Klaus", "passwd 2", "Klaus Wuestefeld", "klaus@algo.org");

        manager.addUser(klaus2);

        assertFalse(manager.isAdmin(klaus));
        assertFalse(manager.isEditor(klaus));
        assertTrue(manager.isReader(klaus));
        assertEquals(manager.getUser("Klaus").getPassword(), "passwd");

        manager.updateUser(klaus2);

        assertEquals(klaus2.getPassword(), manager.getUser("Klaus").getPassword());
    }

    public void testCheckLogin() {

        // Bug fix when check login without add users
        AccessManager manager = new AccessManager();
        assertFalse(manager.checkLogin("Giovane", "pass"));
    }
}
