//
//  Team.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/16/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class Team : Codable {
    var id: CUnsignedLong;
    var name: String;
    var timeCreated: Date;
    var checkedIn: Bool;
    var creatorId: Int;
    var tournamentId: CUnsignedLong;

    init(id: CUnsignedLong, name: String, timeCreated: Date, checkedIn: Bool, creatorId: Int, tournamentId: CUnsignedLong) {
        self.id = id;
        self.name = name;
        self.timeCreated = timeCreated;
        self.checkedIn = checkedIn;
        self.creatorId = creatorId;
        self.tournamentId = tournamentId;
    }

    convenience init() {
        self.init(id: 0, name: "", timeCreated: Date(), checkedIn: false, creatorId: 0, tournamentId: 0);
    }
}
