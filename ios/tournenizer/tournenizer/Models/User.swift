//
//  User.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/16/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class User {
    var email: String;
    var name: String;
    var timeCreated: Date;
    var pastTeams: [Team];

    init(email: String, name: String, timeCreated: Date, pastTeams: [Team] = []) {
        self.email = email;
        self.name = name;
        self.timeCreated = timeCreated;
        self.pastTeams = pastTeams;
    }
};
