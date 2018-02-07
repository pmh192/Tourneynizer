//
//  EventsViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class EventsViewController : UIViewController {
    override func loadView() {
        view = UIView();
        view.backgroundColor = UIColor.lightGray;

        view.setNeedsUpdateConstraints();
    }
}
