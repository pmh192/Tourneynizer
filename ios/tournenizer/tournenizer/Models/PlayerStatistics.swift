//
//  PlayerStatistics.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/10/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;

class PlayerStatistics : Codable {
    var wins: Int;
    var losses: Int;
    var tournaments: Int;
    var matches: Int;

    init(wins: Int = 0, losses: Int = 0, tournaments: Int = 0, matches: Int = 0) {
        self.wins = wins;
        self.losses = losses;
        self.tournaments = tournaments;
        self.matches = matches;
    }
};
