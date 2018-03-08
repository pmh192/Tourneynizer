package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.RosterDao;
import com.tourneynizer.tourneynizer.dao.TeamDao;
import com.tourneynizer.tourneynizer.dao.TeamRequestDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.User;

import java.util.List;

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
        Team team = teamDao.findById(id);
        if (team == null) {
            throw new BadRequestException("No team exists with id " + id);
        }
        // TODO don't allow requests for users already on a team

        try {
            this.teamRequestDao.requestTeam(requester, team);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void requestUser(long teamId, User requested, User user) throws BadRequestException {
        Team team = teamDao.findById(teamId);
        if (team == null) {
            throw new BadRequestException("No team exists with id " + teamId);
        }

        try {
            this.teamRequestDao.requestUser(requested, team, user);
        } catch (IllegalArgumentException e) {
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

    public void acceptTeamRequest(long requestId, User user) throws BadRequestException, InternalErrorException {
        TeamRequest teamRequest = teamRequestDao.findById(requestId);
        if (teamRequest == null) {
            throw new BadRequestException("Cannot find teamRequest with id " + requestId);
        }

        if (teamRequest.getUserId() != user.getId()) {
            throw new BadRequestException("This request isn't for you");
        }

        Team team = teamDao.findById(teamRequest.getTeamId());
        if (team == null) {
            throw new InternalErrorException("teamRequest references non existant team");
        }

        teamRequestDao.removeRequest(teamRequest);
        rosterDao.registerUser(user, team);
    }

    public List<TeamRequest> findAllRequestsForUser(User user) {
        return teamRequestDao.getRequestsForUser(user);
    }

    public List<TeamRequest> findAllRequestsForTeam(long teamId, User user) throws BadRequestException {
        Team team = teamDao.findById(teamId);
        if (team == null) {
            throw new BadRequestException("No team exists with id " + teamId);
        }

        if (team.getCreatorId() != user.getId()) {
            throw new BadRequestException("You are not the creator of this team");
        }

        return teamRequestDao.getRequestsForTeam(team);
    }

    public List<TeamRequest> findAllRequestsByUser(User user) {
        return teamRequestDao.getRequestsByUser(user);
    }

    public List<TeamRequest> findAllRequestsByTeam(long teamId, User user) throws BadRequestException {
        Team team = teamDao.findById(teamId);
        if (team == null) {
            throw new BadRequestException("No team exists with id " + teamId);
        }

        if (team.getCreatorId() != user.getId()) {
            throw new BadRequestException("You are not the creator of this team");
        }

        return teamRequestDao.getRequestsByTeam(team);
    }

    public void deleteRequest(User user, long id) throws BadRequestException {
        TeamRequest teamRequest = teamRequestDao.findById(id);
        if (teamRequest == null) {
            throw new BadRequestException("Couldn't find a team request with id " + id);
        }
        if (teamRequest.getRequesterId() != user.getId()) {
            throw new BadRequestException("You are not the owner of this request");
        }

        teamRequestDao.removeRequest(teamRequest);
    }

    public void declineRequest(User user, long id) throws BadRequestException {
        TeamRequest teamRequest = teamRequestDao.findById(id);
        if (teamRequest == null) {
            throw new BadRequestException("Couldn't find a team request with id " + id);
        }
        if (teamRequest.getRequesterId() == teamRequest.getUserId()) { // User request
            Team team = teamDao.findById(teamRequest.getTeamId());
            if (team.getCreatorId() != user.getId()) {
                throw new BadRequestException("You are not the owner of this team");
            }
        } else { // Team requests user
            if (teamRequest.getUserId() != user.getId()) {
                throw new BadRequestException("This is not your request");
            }
        }

        try {
            teamRequestDao.declineRequest(teamRequest);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
