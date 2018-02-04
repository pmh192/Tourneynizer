//
//  LoginViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/3/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class LoginViewController: UIViewController {

    var loginButton: UIButton = {
        let view = UIButton.newAutoLayout();
        view.setTitle("Hello World!", for: .normal);
        view.setTitleColor(UIColor.blue, for: .normal);
        return view;
    }();
    
    var didUpdateConstraints = false;
    
    override func loadView() {
        view = UIView();
        view.backgroundColor = UIColor.white;
        view.addSubview(loginButton);
        
        view.setNeedsUpdateConstraints();
    }
    
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            loginButton.autoCenterInSuperview();
            
            
            didUpdateConstraints = true;
        }
        
        super.updateViewConstraints();
    }
    
    
    
}


