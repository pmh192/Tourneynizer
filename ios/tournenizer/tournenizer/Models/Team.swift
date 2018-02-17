//
//  Team.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/16/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class Team {
    var id: CUnsignedLong;
    var name: String;
    var timeCreated: Date;
    var tournament: String;

    init(id: CUnsignedLong, name: String, timeCreated: Date, tournament: String) {
        self.id = id;
        self.name = name;
        self.timeCreated = timeCreated;
        self.tournament = tournament;
    }
}
