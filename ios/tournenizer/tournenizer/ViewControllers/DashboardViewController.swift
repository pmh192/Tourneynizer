//
//  DashboardViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/4/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;

class DashboardViewController : UIViewController {
    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.white;
        view.setNeedsUpdateConstraints();
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated);

        // Set status bar style
        self.navigationController?.navigationBar.barStyle = .default;
    }
};
