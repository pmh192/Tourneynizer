//
//  Player.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/16/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation

class Player: User {
    var isLeader: Bool = false;

    init(email: String, name: String, timeCreated: Date, isLeader: Bool) {
        self.isLeader = isLeader;
        super.init(email: email, name: name, timeCreated: timeCreated);
    }
};
