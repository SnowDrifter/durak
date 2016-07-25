package ru.lam.durak.users.service.models;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import ru.lam.durak.users.models.User;

import java.util.HashSet;
import java.util.Set;


public class UserTest {
    User admin;
    User user1;
    User user2;

    @Before
    public void testInit() {
        admin = new User();
        admin.setUsername("admin");
        admin.setPassword("1234");
        admin.setCreatingDate(new DateTime(2016, 3, 31, 0, 0, 0));
        admin.setBirthDate(new DateTime(2014, 2, 12, 0, 0, 0));

        user1 = new User();
        user1.setUsername("user");
        user1.setPassword("qwerty");

        user2 = new User();
        user2.setUsername("user");
        user2.setPassword("qwerty");
    }


    @Test
    public void testEquals() {
        assertFalse(admin.equals(user1));
        assertFalse(user1.equals(admin));

        assertTrue(user1.equals(user2));
        assertTrue(user2.equals(user1));

        assertFalse(admin.equals(null));
    }

    @Test
    public void testHashCode() {
        Set<User> set = new HashSet<>();

        set.add(user1);
        set.add(user2);
        assertEquals(1, set.size());

        set.add(admin);
        assertEquals(2, set.size());
    }


    @Test
    public void testCreatingDateString() {
        assertEquals("31 03 2016", admin.getCreatingDateString());
    }

    @Test
    public void testBirthDateString() {
        assertEquals("12 02 2014", admin.getBirthDateString());
    }

}
