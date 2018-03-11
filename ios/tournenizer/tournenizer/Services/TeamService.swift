//
//  TeamService.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/9/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation

class TeamService : Service {
    static let shared = TeamService();

    func getAllTeamsForTournament(id: CUnsignedLong, cb: @escaping ((String?, [Team]?) -> Void)) {
        makeRequest(url: Constants.route.tournament.teamAll(id), type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            let teamList: [Team]? = self.decode(data!);
            if(teamList == nil) {
                return cb(error, nil);
            }

            return cb(nil, teamList);
        };
    }

    func createTeam(tournament: Tournament, team: Team, cb: @escaping ((String?, Team?) -> Void)) {
        let data = encode(team);
        if(data == nil) {
            return cb(Constants.error.genericError, nil);
        }

        makeRequest(url: Constants.route.tournament.createTeam(tournament.id), type: .POST, body: data!) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            let newTeam: Team? = self.decode(data!);
            if(newTeam == nil) {
                return cb(Constants.error.genericError, nil);
            }

             return cb(nil, newTeam);
        };
    }

    func getAllTeamsForCurrentUser(cb: @escaping ((String?, [Team]?, [Tournament]?) -> Void)) {
        makeRequest(url: Constants.route.team.current, type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil, nil);
            }

            let teamList: [Team]? = self.decode(data!);
            if(teamList == nil) {
                return cb(error, nil, nil);
            }

            var tournaments = [Tournament?](repeating: nil, count: teamList!.count);

            let group = DispatchGroup();
            var errorOccurred = AtomicBoolean();

            for (index, team) in teamList!.enumerated() {
                if(errorOccurred.value) {
                    return cb(Constants.error.serverError, nil, nil);
                }

                group.enter();

                TournamentService.shared.getTournament(team.tournamentId, cb: { (error: String?, tournament: Tournament?) in
                    if(error != nil) {
                        errorOccurred.value = true;
                        return;
                    }

                    tournaments[index] = tournament;
                    group.leave();
                });
            }

            group.notify(queue: .main) {
                if(errorOccurred.value) {
                    return cb(Constants.error.serverError, nil, nil);
                }

                return cb(nil, teamList, tournaments as? [Tournament]);
            }
        }
    }

    func getTeam(_ id: CUnsignedLong, cb: @escaping ((String?, Team?) -> Void)) {
        makeRequest(url: Constants.route.team.get(id), type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            let team: Team? = self.decode(data!);
            if(team == nil) {
                return cb(Constants.error.genericError, nil);
            }

            return cb(nil, team);
        }
    }

    func getTeamMembers(_ id: CUnsignedLong, cb: @escaping ((String?, [User]?) -> Void)) {
        return cb(nil, [UserService.shared.getCurrentUser()!]);

        makeRequest(url: Constants.route.team.getMembers(id), type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            let members: [User]? = self.decode(data!);
            if(members == nil) {
                return cb(Constants.error.genericError, nil);
            }

            return cb(nil, members);
        }
    }

    func getTeamForTournament(_ tournamentId: CUnsignedLong, cb: @escaping ((String?, Team?) -> Void)) {
        return cb(nil, nil);
    }
};
