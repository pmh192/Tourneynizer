package com.dreamteam.tourneynizer.service;

import com.dreamteam.tourneynizer.dao.UserDao;
import com.dreamteam.tourneynizer.error.BadRequestException;
import com.dreamteam.tourneynizer.error.EmailTakenException;
import com.dreamteam.tourneynizer.error.InternalErrorException;
import com.dreamteam.tourneynizer.model.User;
import org.junit.Test;

import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Test
    public void createUser() throws Exception {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserService(userDao);

        User user = userService.insertUser("person@place.com", "name", "plainTextPass");

        verify(userDao).insert(any(User.class));
        User expected = new User("person@place.com", "name", "");
        expected.setPlaintextPassword("plainTextPass");

        assertEquals(expected, user);
        assertTrue(user.correctPassword("plainTextPass"));
    }

    @Test(expected = InternalErrorException.class)
    public void sqlError() throws Exception {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserService(userDao);

        doThrow(new SQLException()).when(userDao).insert(any());

        userService.insertUser("person@place.com", "name", "plainTextPass");
    }

    @Test(expected = BadRequestException.class)
    public void invalidUserError() throws Exception {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserService(userDao);

        doThrow(new EmailTakenException("", null)).when(userDao).insert(any());

        userService.insertUser("person@place.com", "name", "plainTextPass");
    }

    @Test(expected = BadRequestException.class)
    public void invalidEmailAdressError() throws Exception {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserService(userDao);

        userService.insertUser("InvalidEMail_ADdress", "name", "plainTextPass");
    }
}