//
//  DataExtension.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/8/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation

extension Data {
    func toUtf8String() -> String {
        if let responseString = String(data: self, encoding: .utf8) {
            return responseString;
        } else {
            return "";
        }
    }
};
