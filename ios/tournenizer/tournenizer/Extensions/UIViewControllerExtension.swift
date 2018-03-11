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

    func displayError(_ error: String) {
        let alert = UIAlertController(title: "Error", message: error, preferredStyle: .alert);
        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: nil));
        self.present(alert, animated: true, completion: nil);
    }
};
