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
        makeRequest(url: Constants.route.tournament.teamAll(id: id), type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            guard let teamList = try? JSONDecoder().decode([Team].self, from: data!) else {
                return cb(error, nil);
            }

            return cb(nil, teamList);
        };
    }

    func createTeam(tournament: Tournament, team: Team, roster: [User], cb: @escaping ((String?, Bool?) -> Void)) {
        let data = encode(team);
        makeRequest(url: Constants.route.tournament.createTeam(id: tournament.id), type: .POST, body: data!) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            print(data?.toUtf8String());
        };
    }
};
