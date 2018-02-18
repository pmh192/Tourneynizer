//
//  TeamRequestsViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/18/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//
import UIKit;
import PureLayout;

class TeamRequestsViewController : UIViewController {
    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.darkGray;

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
}

