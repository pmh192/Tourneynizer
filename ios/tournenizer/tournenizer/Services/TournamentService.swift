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
        let data = encodeTournament(tournament);
        if(data == nil) {
            return cb(Constants.error.genericError, nil);
        }

        makeRequest(url: Constants.route.tournament.create, type: .POST, body: data) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            let tournament = self.decodeTournament(data!);
            if(tournament == nil) {
                return cb(Constants.error.genericError, nil);
            }

            return cb(nil, tournament!);
        }
    }

    private func encodeTournament(_ tournament: Tournament) -> Data? {
        guard let data = try? JSONEncoder().encode(tournament) else {
            return nil;
        }

        return data;
    }

    private func decodeTournament(_ data: Data) -> Tournament? {
        guard let tournament = try? JSONDecoder().decode(Tournament.self, from: data) else {
            return nil;
        }

        return tournament;
    }
};
