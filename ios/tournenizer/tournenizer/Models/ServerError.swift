//
//  ServerError.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/8/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation

class ServerError : Codable {
    var message: String;

    init(message: String = "") {
        self.message = message;
    }
}
