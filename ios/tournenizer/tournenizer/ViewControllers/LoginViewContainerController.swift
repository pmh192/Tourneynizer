//
//  LoginViewContainerController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/17/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;

class LoginViewContainerController : UINavigationController {

    override func loadView() {
        super.loadView();

        navigationBar.isHidden = true;
        pushViewController(LoginViewController(), animated: false);
    }
};
