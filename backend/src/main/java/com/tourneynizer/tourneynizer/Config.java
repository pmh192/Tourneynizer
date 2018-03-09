package com.tourneynizer.tourneynizer;

import com.tourneynizer.tourneynizer.dao.*;
import com.tourneynizer.tourneynizer.helper.ContextHelper;
import com.tourneynizer.tourneynizer.service.*;
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
        TeamDao teamDao = context.getBean("TeamDao", TeamDao.class);
        MatchDao matchDao = context.getBean("MatchDao", MatchDao.class);
        return new TournamentService(tournamentDao, teamDao, matchDao);
    }

    @Bean
    public SessionService sessionService() {
        UserDao userDao = context.getBean("UserDao", UserDao.class);
        SessionDao sessionDao = context.getBean("SessionDao", SessionDao.class);
        return new SessionService(userDao, sessionDao);
    }

    @Bean
    public TeamRequestService teamRequestService() {
        TeamRequestDao teamRequestDao = context.getBean("TeamRequestDao", TeamRequestDao.class);
        TeamDao teamDao = context.getBean("TeamDao", TeamDao.class);
        RosterDao rosterDao = context.getBean("RosterDao", RosterDao.class);
        return new TeamRequestService(teamRequestDao, teamDao, rosterDao);
    }

    @Bean
    public TeamService teamService() {
        TeamDao teamDao = context.getBean("TeamDao", TeamDao.class);
        TournamentDao tournamentDao = context.getBean("TournamentDao", TournamentDao.class);
        RosterDao rosterDao = context.getBean("RosterDao", RosterDao.class);
        return new TeamService(teamDao, tournamentDao, rosterDao);
    }

    @Bean
    public TournamentRequestService tournamentRequestService() {
        TournamentRequestDao tournamentRequestDao = context.getBean("TournamentRequestDao", TournamentRequestDao.class);
        TeamDao teamDao = context.getBean("TeamDao", TeamDao.class);
        RosterDao rosterDao = context.getBean("RosterDao", RosterDao.class);
        TournamentDao tournamentDao = context.getBean("TournamentDao", TournamentDao.class);
        return new TournamentRequestService(tournamentRequestDao, teamDao, rosterDao, tournamentDao);
    }
}
