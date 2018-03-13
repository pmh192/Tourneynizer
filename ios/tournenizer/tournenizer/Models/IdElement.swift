//
//  IdElement.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/12/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;

class IdElement : Codable {
    var id: CUnsignedLong;

    init(_ id: CUnsignedLong) {
        self.id = id;
    }
}
