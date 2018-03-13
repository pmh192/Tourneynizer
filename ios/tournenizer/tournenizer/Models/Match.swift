//
//  Match.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/12/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;

class Match : Codable {
    var id: CUnsignedLong;
    var refId: CUnsignedLong?;
    var score1: CUnsignedLong?;
    var score2: CUnsignedLong?;
    var matchChildren: MatchChildren;
    var timeStart: Date?;
    var timeEnd: Date?;
    var matchStatus: MatchStatus;
    var round: CUnsignedLong;
    var tournamentId: CUnsignedLong;
    var matchOrder: CUnsignedLong;
};
