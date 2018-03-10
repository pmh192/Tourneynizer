//
//  TeamRequestService.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/9/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;

class TeamRequestService : Service {
    static let shared = TeamRequestService();

    func getRequestsForCurrentUser(cb: @escaping ((String?, [TeamRequest]?) -> Void)) {
        makeRequest(url: Constants.route.user.requests, type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            let teamRequests: [TeamRequest]? = self.decode(data!);
            if(teamRequests == nil) {
                return cb(Constants.error.genericError, nil);
            }

            return cb(nil, teamRequests);
        }
    }

    func requestToJoinTeam(_ team: Team, cb: @escaping ((String?) -> Void)) {
        makeRequest(url: Constants.route.team.joinTeam(id: team.id), type: .POST, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            return cb(error);
        }
    }

    func requestUserToJoinTeam(team: Team, user: User, cb: @escaping ((String?) -> Void)) {
        makeRequest(url: Constants.route.team.userJoinTeam(userId: user.id, teamId: team.id), type: .POST, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            return cb(error);
        }
    }
};
