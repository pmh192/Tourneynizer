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

    func getValidMatches(_ id: CUnsignedLong, cb: @escaping (String?, [Match]?) -> Void) {
        makeRequest(url: Constants.route.match.validMatches(id), type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error!, nil);
            }

            let matches: [Match]? = self.decode(data!);
            if(matches == nil) {
                return cb(error, nil);
            }

            return cb(nil, matches);
        }
    }
};
