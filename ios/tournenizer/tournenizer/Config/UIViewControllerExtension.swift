//
//  UIViewControllerExtension.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/4/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;

extension UIViewController {
    // Hides the keyboard
    @objc func dismissKeyboard() {
        view.endEditing(true);
    }
};
