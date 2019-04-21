package ru.romanov.durak.users.service.models;

import org.junit.Before;
import org.junit.Test;
import ru.romanov.durak.user.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


public class UserTest {

    private User admin;
    private User user1;
    private User user2;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MM yyyy");

    @Before
    public void testInit() {
        admin = new User();
        admin.setUsername("admin");
        admin.setPassword("1234");
        admin.setCreationDate(new Date(1459382400000L));
        admin.setBirthDate(new Date(1392163200000L));

        user1 = new User();
        user1.setUsername("user");
        user1.setPassword("qwerty");

        user2 = new User();
        user2.setUsername("user");
        user2.setPassword("qwerty");
    }

    @Test
    public void testEquals() {
        assertNotEquals(admin, user1);
        assertNotEquals(user1, admin);

        assertEquals(user1, user2);
        assertEquals(user2, user1);

        assertNotEquals(admin, null);
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
        assertEquals("31 03 2016", DATE_FORMAT.format(admin.getCreationDate()));
    }

    @Test
    public void testBirthDateString() {
        assertEquals("12 02 2014", DATE_FORMAT.format(admin.getBirthDate()));
    }

}
