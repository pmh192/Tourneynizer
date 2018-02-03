package com.dreamteam.tourneynizer.service;

import com.dreamteam.tourneynizer.dao.UserDao;
import com.dreamteam.tourneynizer.error.BadRequestException;
import com.dreamteam.tourneynizer.error.EmailTakenException;
import com.dreamteam.tourneynizer.error.InternalErrorException;
import com.dreamteam.tourneynizer.model.User;

import java.sql.SQLException;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User insertUser(String email, String name, String plainTextPass) throws BadRequestException, InternalErrorException {
        User user;
        try {
            user = new User(email, name, "");
            user.setPlaintextPassword(plainTextPass);
        }
        catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage(), e);
        }

        try {
            userDao.insert(user);
        } catch (EmailTakenException e) {
            throw new BadRequestException("That email address is taken", e);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }

        return user;
    }
}
