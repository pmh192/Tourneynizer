//
//  MatchService.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/12/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;

class MatchService : Service {
    static let shared = MatchService();

    func getValidMatches(_ id: CUnsignedLong, cb: @escaping (String?, [Match]?, [[Team]]?, [User]?) -> Void) {
        makeRequest(url: Constants.route.match.validMatches(id), type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error!, nil, nil, nil);
            }

            let matches: [Match]? = self.decode(data!);
            if(matches == nil) {
                return cb(error, nil, nil, nil);
            }

            var teams = [[Team?]](repeating: [nil, nil], count: matches!.count);
            var refs = [User?](repeating: nil, count: matches!.count);

            let group = DispatchGroup();
            var errorOccurred = AtomicBoolean();

            for (index, match) in matches!.enumerated() {
                if(errorOccurred.value) {
                    return cb(Constants.error.serverError, nil, nil, nil);
                }

                group.enter();
                group.enter();
                group.enter();

                let teamIds = match.matchChildren.teams as! [IdElement];

                TeamService.shared.getTeam(teamIds[0].id, cb: { (error: String?, team: Team?) in
                    if(error != nil) {
                        errorOccurred.value = true;
                        return;
                    }

                    teams[index][0] = team!;
                    group.leave();
                });

                TeamService.shared.getTeam(teamIds[1].id, cb: { (error: String?, team: Team?) in
                    if(error != nil) {
                        errorOccurred.value = true;
                        return;
                    }

                    teams[index][1] = team!;
                    group.leave();
                });

                TeamService.shared.getTeam(match.refId!, cb: { (error: String?, team: Team?) in
                    if(error != nil) {
                        errorOccurred.value = true;
                        return;
                    }

                    UserService.shared.getUser(team!.creatorId, cb: { (error: String?, ref: User?) in
                        if(error != nil) {
                            errorOccurred.value = true;
                            return;
                        }

                        refs[index] = ref!;
                        group.leave();
                    });
                });
            }

            group.notify(queue: .main) {
                if(errorOccurred.value) {
                    return cb(Constants.error.serverError, nil, nil, nil);
                }

                return cb(nil, matches, teams as? [[Team]], refs as? [User]);
            }
        }
    }
};
