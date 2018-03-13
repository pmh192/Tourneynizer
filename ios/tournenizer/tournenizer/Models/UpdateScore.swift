//
//  UpdateScore.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/13/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;

class UpdateScore : Codable {
    var score1: Int;
    var score2: Int;

    init(score1: Int, score2: Int) {
        self.score1 = score1;
        self.score2 = score2;
    }
}
