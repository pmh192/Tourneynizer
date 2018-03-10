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

    func getRequestsForCurrentUser(cb: @escaping ((String?, [TeamRequest]?, [Tournament]?, [User]?) -> Void)) {
        makeRequest(url: Constants.route.user.requests, type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil, nil, nil);
            }

            let teamRequests: [TeamRequest]? = self.decode(data!);
            if(teamRequests == nil) {
                return cb(Constants.error.genericError, nil, nil, nil);
            }

            print(data?.toUtf8String());

            var users = [User?](repeating: nil, count: teamRequests!.count);

            let userGroup = DispatchGroup();
            var errorOccured = false;
            for (index, req) in teamRequests!.enumerated() {
                userGroup.enter();
                UserService.shared.getUser(req.userId, cb: { (error: String?, user: User?) in
                    if(error != nil) {
                        errorOccured = true;
                        return;
                    }

                    users[index] = user!;
                    userGroup.leave();
                });
            }

            var tournaments = [Tournament?](repeating: nil, count: teamRequests!.count);

            userGroup.notify(queue: .main) {
                if(errorOccured) {
                    return cb(Constants.error.serverError, nil, nil, nil);
                }

                let tournamentGroup = DispatchGroup();
                for (index, req) in teamRequests!.enumerated() {
                    tournamentGroup.enter();
                    tournaments[index] = Tournament();
                    tournamentGroup.leave();
                }

                tournamentGroup.notify(queue: .main) {
                    if(errorOccured) {
                        return cb(Constants.error.serverError, nil, nil, nil);
                    }

                    return cb(nil, teamRequests, tournaments as! [Tournament], users as! [User]);
                }
            }
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
