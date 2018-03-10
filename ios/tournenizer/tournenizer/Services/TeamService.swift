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
};
