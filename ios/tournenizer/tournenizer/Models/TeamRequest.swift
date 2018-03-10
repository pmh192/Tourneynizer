//
//  TeamRequest.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/9/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation

class TeamRequest : Codable {
    var id: CUnsignedLong;
    var teamId: CUnsignedLong;
    var userId: CUnsignedLong;
    var requesterId: CUnsignedLong;
    var timeRequested: Date;

    init(id: CUnsignedLong, teamId: CUnsignedLong, userId: CUnsignedLong, requesterId: CUnsignedLong, timeRequested: Date) {
        self.id = id;
        self.teamId = teamId;
        self.userId = userId;
        self.requesterId = requesterId;
        self.timeRequested = timeRequested;
    }
}
