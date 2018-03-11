//
//  Tournament.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class Tournament : Codable {
    var id: CUnsignedLong;
    var name: String;
    var description: String?;
    var lat: Double;
    var lng: Double;
    var startTime: Date;
    var maxTeams: Int;
    var currentTeams: Int?;
    var timeCreated: Date;
    var type: TournamentType?;
    var creatorId: CUnsignedLong;
    var cancelled: Bool?;
    var teamSize: Int;

    init(id: CUnsignedLong, name: String, description: String?, lat: Double, lng: Double, startTime: Date, maxTeams: Int, currentTeams: Int?, timeCreated: Date, tournamentType: TournamentType?, creatorId: CUnsignedLong, cancelled: Bool?, teamSize: Int) {
        self.id = id;
        self.name = name;
        self.description = description;
        self.lat = lat;
        self.lng = lng;
        self.startTime = startTime;
        self.maxTeams = maxTeams;
        self.currentTeams = currentTeams;
        self.timeCreated = timeCreated;
        self.type = tournamentType;
        self.creatorId = creatorId;
        self.cancelled = cancelled;
        self.teamSize = teamSize;
    }

    init(_ tournament: Tournament) {
        self.id = tournament.id;
        self.name = tournament.name;
        self.description = tournament.description;
        self.lat = tournament.lat;
        self.lng = tournament.lng;
        self.startTime = tournament.startTime;
        self.maxTeams = tournament.maxTeams;
        self.currentTeams = tournament.currentTeams;
        self.timeCreated = tournament.timeCreated;
        self.type = tournament.type;
        self.creatorId = tournament.creatorId;
        self.cancelled = tournament.cancelled;
        self.teamSize = tournament.teamSize;
    }

    init() {
        self.id = 0;
        self.name = "";
        self.description = "";
        self.lat = 0;
        self.lng = 0;
        self.startTime = Date();
        self.maxTeams = -1;
        self.currentTeams = -1;
        self.timeCreated = Date();
        self.type = TournamentType.VOLLEYBALL_BRACKET;
        self.creatorId = 0;
        self.cancelled = false;
        self.teamSize = -1;
    }
};
