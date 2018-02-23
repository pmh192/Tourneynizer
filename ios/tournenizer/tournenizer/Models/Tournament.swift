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

    init(_ tournament: Tournament) {
        self.id = tournament.id;
        self.name = tournament.name;
        self.description = tournament.description;
        self.address = tournament.address;
        self.startTime = tournament.startTime;
        self.endTime = tournament.endTime;
        self.maxTeams = tournament.maxTeams;
        self.currentTeams = tournament.currentTeams;
        self.timeCreated = tournament.timeCreated;
        self.tournamentType = tournament.tournamentType;
        self.logo = tournament.logo;
        self.courts = tournament.courts;
        self.creatorId = tournament.creatorId;
        self.cancelled = tournament.cancelled;
        self.teamSize = tournament.teamSize;
    }

    init() {
        self.id = 0;
        self.name = "";
        self.description = "";
        self.address = "";
        self.startTime = Date();
        self.endTime = Date();
        self.maxTeams = -1;
        self.currentTeams = -1;
        self.timeCreated = Date();
        self.tournamentType = TournamentType.VOLLEYBALL_BRACKET;
        self.logo = nil;
        self.courts = -1;
        self.creatorId = 0;
        self.cancelled = false;
        self.teamSize = -1;
    }
};
