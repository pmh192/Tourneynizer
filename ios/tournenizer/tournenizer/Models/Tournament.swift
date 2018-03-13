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
    var status: TournamentStatus?;
    var creatorId: CUnsignedLong;
    var cancelled: Bool?;
    var teamSize: Int;

    init(id: CUnsignedLong, name: String, description: String?, lat: Double, lng: Double, startTime: Date, maxTeams: Int, currentTeams: Int?, timeCreated: Date, tournamentType: TournamentType?, creatorId: CUnsignedLong, cancelled: Bool?, teamSize: Int, status: TournamentStatus?) {
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
        self.status = status;
    }
};
