//
//  ProfileViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class ProfileViewController : UIViewController {
    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.lightBlue;

        view.setNeedsUpdateConstraints();
    }
}
