package com.dreamteam.tourneynizer.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    @Test
    public void constructor() throws Exception {
        new User("person@place.com", "name", "hashedPassword");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorBadEmail() throws Exception {
        new User("invalidEmail", "name", "hashedPassword");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorEmptyName() throws Exception {
        new User("person@place.com", "", "hashedPassword");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorLongName() throws Exception {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            name.append("F");
        }
        new User("person@place.com", name.toString(), "hashedPassword");
    }

    @Test
    public void passwordCorrectness() throws Exception {
        User user = new User("person@place.com", "name", "hashedPassword");
        user.setPlaintextPassword("password");

        assertTrue(user.correctPassword("password"));
        assertFalse(user.correctPassword("incorrect"));
    }

    @Test
    public void passwordHashed() throws Exception {
        assertTrue(true); // change when ready
    }

}