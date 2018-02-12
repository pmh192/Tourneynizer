package com.tourneynizer.tourneynizer;

import com.tourneynizer.tourneynizer.dao.TournamentDao;
import com.tourneynizer.tourneynizer.dao.UserDao;
import com.tourneynizer.tourneynizer.helper.ContextHelper;
import com.tourneynizer.tourneynizer.service.TournamentService;
import com.tourneynizer.tourneynizer.service.UserService;
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

    @Bean
    public TournamentService tournamentService() {
        TournamentDao tournamentDao = context.getBean("TournamentDao", TournamentDao.class);
        return new TournamentService(tournamentDao);
    }
}
