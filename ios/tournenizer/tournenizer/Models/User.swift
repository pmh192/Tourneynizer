//
//  User.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/16/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class User : Codable {
    var email: String;
    var name: String;
    var password: String?;
    var timeCreated: Date;

    init(email: String, name: String, password: String? = nil, timeCreated: Date = Date()) {
        self.email = email;
        self.name = name;
        self.password = password;
        self.timeCreated = timeCreated;
    }
};
