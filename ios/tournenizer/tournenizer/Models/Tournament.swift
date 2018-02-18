//
//  Tournament.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class Tournament {
    var id: CUnsignedLong;
    var name: String;
    var description: String?;
    var address: String;
    var startTime: Date;
    var endTime: Date?;
    var maxTeams: Int;
    var currentTeams: Int?;
    var timeCreated: Date;
    var tournamentType: TournamentType?;
    var logo: UIImage?;
    var courts: Int?;
    var creatorId: CUnsignedLong;
    var cancelled: Bool?;
    var teamSize: Int;

    init(id: CUnsignedLong, name: String, description: String?, address: String, startTime: Date, endTime: Date?, maxTeams: Int, currentTeams: Int?, timeCreated: Date, tournamentType: TournamentType?, logo: UIImage?, courts: Int?, creatorId: CUnsignedLong, cancelled: Bool?, teamSize: Int) {
        self.id = id;
        self.name = name;
        self.description = description;
        self.address = address;
        self.startTime = startTime;
        self.endTime = endTime;
        self.maxTeams = maxTeams;
        self.currentTeams = currentTeams;
        self.timeCreated = timeCreated;
        self.tournamentType = tournamentType;
        self.logo = logo;
        self.courts = courts;
        self.creatorId = creatorId;
        self.cancelled = cancelled;
        self.teamSize = teamSize;
    }
};
