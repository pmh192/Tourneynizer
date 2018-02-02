package com.dreamteam.tourneynizer;

import com.dreamteam.tourneynizer.dao.UserDao;
import com.dreamteam.tourneynizer.helper.ContextHelper;
import com.dreamteam.tourneynizer.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    private final ApplicationContext context;

    public Config() {
        context = ContextHelper.getContext();
    }

    @Bean
    public UserService userService() {
        UserDao userDao = context.getBean("UserDao", UserDao.class);
        return new UserService(userDao);
    }
}
