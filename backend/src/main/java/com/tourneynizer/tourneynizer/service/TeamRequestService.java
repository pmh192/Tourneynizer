package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.RosterDao;
import com.tourneynizer.tourneynizer.dao.TeamDao;
import com.tourneynizer.tourneynizer.dao.TeamRequestDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.UserMustBeLoggedInException;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.User;

public class TeamRequestService {

    private final TeamRequestDao teamRequestDao;
    private final TeamDao teamDao;
    private final RosterDao rosterDao;

    public TeamRequestService(TeamRequestDao teamRequestDao, TeamDao teamDao, RosterDao rosterDao) {
        this.teamRequestDao = teamRequestDao;
        this.teamDao = teamDao;
        this.rosterDao = rosterDao;
    }

    public void requestTeam(long id, User requester) throws BadRequestException {
        if (requester == null) {
            throw new UserMustBeLoggedInException();
        }

        Team team = teamDao.findById(id);
        if (team == null) {
            throw new BadRequestException("No team exists with id " + id);
        }
        // TODO don't allow requests for users already on a team

        try {
            this.teamRequestDao.requestTeam(requester, team);
        }
        catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void acceptUserRequest(User user, long teamId, long requestId) throws BadRequestException {
        Team team = teamDao.findById(teamId);
        TeamRequest teamRequest = teamRequestDao.findById(requestId);

        if (teamRequest == null) {
            throw new BadRequestException("Cannot find teamRequest with id " + requestId);
        }

        if (teamRequest.getRequesterId() != teamRequest.getUserId()) {
            throw new BadRequestException("You can't accept this request. The user must do that");
        }
        if (user.getId() != team.getCreatorId()) {
            throw new BadRequestException("You must be the creator of the team to accept requests");
        }

        teamRequestDao.removeRequest(teamRequest);
        rosterDao.registerUser(user, team);

    }
}
