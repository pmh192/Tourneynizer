//
//  UIColorExtension.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/3/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;

extension UIColor {
    convenience init(r: UInt8, g: UInt8, b: UInt8, a: UInt8) {
        self.init(red: CGFloat(r)/255, green: CGFloat(g)/255, blue: CGFloat(b)/255, alpha: CGFloat(a)/255);
    }
};
