//
//  TournamentService.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/8/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;

class TournamentService : Service  {
    static let shared = TournamentService();

    func getAllTournaments(cb: @escaping (String?, [Tournament]?) -> Void) {
        makeRequest(url: Constants.route.tournament.all, type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            guard let tournamentList = try? JSONDecoder().decode([Tournament].self, from: data!) else {
                return;
            }

            return cb(nil, tournamentList);
        }
    }

    func createTournament(_ tournament: Tournament, cb: @escaping (String?, Tournament?) -> Void) {
        let data = encode(tournament);
        if(data == nil) {
            return cb(Constants.error.genericError, nil);
        }

        makeRequest(url: Constants.route.tournament.create, type: .POST, body: data) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            let tournament: Tournament? = self.decode(data!);
            if(tournament == nil) {
                return cb(Constants.error.genericError, nil);
            }

            return cb(nil, tournament!);
        }
    }
};
