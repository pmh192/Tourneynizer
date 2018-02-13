package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.SessionDao;
import com.tourneynizer.tourneynizer.dao.UserDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.User;

import java.sql.SQLException;
import java.util.Map;

public class SessionService {

    private final UserDao userDao;
    private final SessionDao sessionDao;

    public SessionService(UserDao userDao, SessionDao sessionDao) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }

    public String createSession(Map<String, String> auth) throws BadRequestException, InternalErrorException {
        String email = auth.getOrDefault("email", "");
        String password = auth.getOrDefault("password", "");
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new BadRequestException(email + " isn't found");
        }

        if (!user.correctPassword(password)) {
            throw new BadRequestException("Password is incorrect");
        }

        try {
            return sessionDao.createSession(user);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }

    public void destroySession(Map<String, String> auth) throws InternalErrorException {
        String session = auth.getOrDefault("session", "");
        try {
            sessionDao.destroySession(session);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }

    public User findByRequest(Map<String, String> values) {
        return sessionDao.findBySession(values.get("session"));
    }
}
