//
//  Constant.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/3/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;

class Constants {
    class color {
        static let red = UIColor.init(r: 231, g: 76, b: 60, a: 255);
        static let lightBlue = UIColor.init(r: 52, g: 152, b: 219, a: 255);
        static let white = UIColor.init(r: 236, g: 240, b: 241, a: 255);
        static let navy = UIColor.init(r: 44, g: 62, b: 80, a: 255);
        static let blue = UIColor.init(r: 41, g: 128, b: 185, a: 255);
        static let lightGray = UIColor(r: 220, g: 220, b: 220, a: 255);
        static let gray = UIColor.lightGray;
        static let darkGray = UIColor.darkGray;
    };

    class font {
        static let normal = "Futura";
        static let medium = "Futura-Medium";
    };

    class fontSize {
        static let header: CGFloat = 45;
        static let mediumHeader: CGFloat = 30;
        static let smallHeader: CGFloat = 22;
        static let normal: CGFloat = 17;
        static let small: CGFloat = 12;
    };

    static let statusBarCoverHeight: CGFloat = 50;
};
