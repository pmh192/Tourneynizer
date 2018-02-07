//
//  TournamentListViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class TournamentListViewController : UIViewController {
    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.red;

        view.setNeedsUpdateConstraints();
    }
}
