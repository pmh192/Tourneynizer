//
//  LoginViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/3/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class LoginViewController: UIViewController, UITextFieldDelegate {
    // Declarations for all subviews
    var statusBarCover: UIView!;
    var logoLabel: UILabel!;
    var usernameField: UITextField!;
    var passwordField: UITextField!;
    var loginButton: UIButton!;
    var loginStack: UIStackView!;
    var registerPrompt: UILabel!;
    var registerButton: UIButton!;
    var registerStack: UIStackView!;
    var fakeKeyboardView: UIView?;
    
    // Definitions for all style related constants
    let statusBarHeight: CGFloat = 50;
    let logoLabelHeight: CGFloat = 150;
    let loginClusterSidePadding: CGFloat = 30;
    let loginClusterElementVerticalPadding: CGFloat = 15;
    let loginButtonBorderRadius: CGFloat = 5;
    let loginButtonBorderWidth: CGFloat = 1;
    let registerPromptPadding: CGFloat = 10;
    let passwordKeyboardLowResPadding: CGFloat = 20;
    let headerFontSize: CGFloat = 45;
    let normalFontSize: CGFloat = 17;
    let smallFontSize: CGFloat = 12;
    let logoText = "Tourneynizer";
    let usernameFieldPrompt = "Email";
    let passwordFieldPrompt = "Password";
    let loginButtonText = "Login";
    let registerPromptText = "Don't have an account?";
    let registerButtonText = "Sign up.";
    
    override func loadView() {
        // Initialize view
        view = UIView();
        view.backgroundColor = Constant.white;
        
        // Setup callback to hide keyboard on outside tap
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard));
        view.addGestureRecognizer(tap);
        
        // Initialize all subviews
        fakeKeyboardView = nil;
        
        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constant.navy;
            return view;
        }();
        
        logoLabel = {
            let view = UILabel.newAutoLayout();
            view.text = logoText;
            view.backgroundColor = Constant.navy;
            view.textColor = Constant.red;
            view.textAlignment = .center;
            view.font = UIFont(name: Constant.fontMedium, size: headerFontSize);
            return view;
        }();
        
        usernameField = {
            let view = UITextField.newAutoLayout();
            view.placeholder = usernameFieldPrompt;
            view.font = UIFont(name: Constant.font, size: normalFontSize);
            view.textAlignment = .center;
            view.textColor = Constant.navy;
            view.keyboardType = .emailAddress;
            view.returnKeyType = .next;
            return view;
        }();
        
        passwordField = {
            let view = UITextField.newAutoLayout();
            view.placeholder = passwordFieldPrompt;
            view.font = UIFont(name: Constant.font, size: normalFontSize);
            view.textAlignment = .center;
            view.isSecureTextEntry = true;
            view.textColor = Constant.navy;
            view.returnKeyType = .go;
            return view;
        }();
        
        loginButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(loginButtonText, for: .normal);
            view.setTitleColor(Constant.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constant.font, size: normalFontSize);
            view.layer.cornerRadius = loginButtonBorderRadius;
            view.layer.borderWidth = loginButtonBorderRadius;
            view.layer.borderColor = Constant.lightBlue.cgColor;
            view.backgroundColor = Constant.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        
        registerPrompt = {
            let view = UILabel.newAutoLayout();
            view.text = registerPromptText;
            view.textColor = UIColor.lightGray;
            view.textAlignment = .center;
            view.font = UIFont(name: Constant.fontMedium, size: normalFontSize);
            return view;
        }();
        
        registerButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(registerButtonText, for: .normal);
            view.setTitleColor(Constant.lightBlue, for: .normal);
            view.titleLabel?.font = UIFont(name: Constant.font, size: normalFontSize);
            return view;
        }();
        
        loginStack = { [usernameField, passwordField, loginButton] in
            let view = UIStackView();
            view.axis = .vertical;
            view.addSubview(usernameField!);
            view.addSubview(passwordField!);
            view.addSubview(loginButton!);
            return view;
        }();
        
        registerStack = { [registerPrompt, registerButton] in
            let view = UIStackView();
            view.axis = .vertical;
            view.addSubview(registerPrompt!);
            view.addSubview(registerButton!);
            return view;
        }();
        
        // Add all subviews and trigger contraint setup
        view.addSubview(statusBarCover);
        view.addSubview(logoLabel);
        view.addSubview(loginStack);
        view.addSubview(registerStack);
        view.setNeedsUpdateConstraints();
        
        // Delegate view Controller to respond to events
        usernameField.delegate = self;
        passwordField.delegate = self;
    }
    
    // Creates a bottom border for a UITextField
    private func setBottomBorderTextField(_ field: UITextField) {
        let border = CALayer();
        let width = CGFloat(2.0);
        border.borderColor = UIColor.lightGray.cgColor;
        border.frame = CGRect(x: 0, y: field.frame.size.height - width, width:  field.frame.size.width, height: field.frame.size.height);
        
        border.borderWidth = width;
        field.layer.addSublayer(border);
        field.layer.masksToBounds = true;
    }
    
    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;
    var didLoadSubviews = false;
    
    // Sets the bottom borders on all UITextFields
    override func viewDidLayoutSubviews() {
        if(!didLoadSubviews) {
            setBottomBorderTextField(usernameField);
            setBottomBorderTextField(passwordField);
            didLoadSubviews = true;
        }
    }
    
    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -statusBarHeight);
            statusBarCover.autoSetDimension(.height, toSize: statusBarHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);
            
            logoLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            logoLabel.autoPinEdge(toSuperviewEdge: .leading);
            logoLabel.autoPinEdge(toSuperviewEdge: .trailing);
            logoLabel.autoSetDimension(.height, toSize: logoLabelHeight);
            
            registerStack.autoPin(toBottomLayoutGuideOf: self, withInset: 0);
            registerStack.autoPinEdge(toSuperviewEdge: .left);
            registerStack.autoPinEdge(toSuperviewEdge: .right);
            
            loginStack.autoPinEdge(.top, to: .bottom, of: logoLabel);
            loginStack.autoPinEdge(.bottom, to: .top, of: registerStack);
            loginStack.autoPinEdge(toSuperviewEdge: .left, withInset: loginClusterSidePadding, relation: .lessThanOrEqual);
            loginStack.autoPinEdge(toSuperviewEdge: .right, withInset: loginClusterSidePadding, relation: .lessThanOrEqual);
            
            loginButton.autoCenterInSuperview();
            loginButton.autoPinEdge(toSuperviewEdge: .leading);
            loginButton.autoPinEdge(toSuperviewEdge: .trailing);
            
            passwordField.autoPinEdge(.bottom, to: .top, of: loginButton, withOffset: -loginClusterElementVerticalPadding);
            passwordField.autoPinEdge(toSuperviewEdge: .leading);
            passwordField.autoPinEdge(toSuperviewEdge: .trailing);
            
            usernameField.autoPinEdge(.bottom, to: .top, of: passwordField, withOffset: -loginClusterElementVerticalPadding);
            usernameField.autoPinEdge(toSuperviewEdge: .leading);
            usernameField.autoPinEdge(toSuperviewEdge: .trailing);
            
            registerButton.autoPinEdge(toSuperviewEdge: .bottom, withInset: registerPromptPadding);
            registerButton.autoPinEdge(toSuperviewEdge: .leading);
            registerButton.autoPinEdge(toSuperviewEdge: .trailing);
            
            registerPrompt.autoPinEdge(.bottom, to: .top, of: registerButton);
            registerPrompt.autoPinEdge(toSuperviewEdge: .leading);
            registerPrompt.autoPinEdge(toSuperviewEdge: .trailing);
            
            didUpdateConstraints = true;
        }
        
        super.updateViewConstraints();
    }
    
    // Subscribes to the keyboard show and hide event for resizing
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated);
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: .UIKeyboardDidShow, object: nil);
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: .UIKeyboardWillHide, object: nil);
    }
    
    // Removes all subscriptions
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated);
        NotificationCenter.default.removeObserver(self, name: .UIKeyboardDidShow, object: nil);
        NotificationCenter.default.removeObserver(self, name: .UIKeyboardDidHide, object: nil);
    }
    
    // Logic for resetting a view on smaller devices to prevent UITextFields from being hidden by keyboard
    @objc func keyboardWillShow(notification: NSNotification) {
        // Find a better way to detect smaller screens
        if(UIScreen.main.bounds.height > 568.0) {
            return;
        }
        
        let info = notification.userInfo!;
        let keyboardSize = (info[UIKeyboardFrameEndUserInfoKey] as! NSValue).cgRectValue.height;
        let duration: TimeInterval = (info[UIKeyboardAnimationDurationUserInfoKey] as! NSNumber).doubleValue;
        
        let keyboardHeight: CGFloat;
        if #available(iOS 11.0, *) {
            keyboardHeight = keyboardSize - self.view.safeAreaInsets.bottom;
        } else {
            keyboardHeight = keyboardSize;
        }
        
        if(fakeKeyboardView != nil) {
            fakeKeyboardView?.removeFromSuperview();
            fakeKeyboardView = nil;
        }
        
        let fakeView = UIView();
        view.addSubview(fakeView);
        
        fakeView.autoSetDimension(.height, toSize: keyboardHeight)
        fakeView.autoPinEdge(toSuperviewEdge: .bottom);
        fakeView.autoPinEdge(toSuperviewEdge: .left);
        fakeView.autoPinEdge(toSuperviewEdge: .right);
        fakeKeyboardView = fakeView;
        
        passwordField.autoPinEdge(.bottom, to: .top, of: fakeKeyboardView!, withOffset: -passwordKeyboardLowResPadding);
        
        UIView.animate(withDuration: duration, animations: {
            self.view.layoutIfNeeded();
        });
    }
    
    // Logic for resetting a view on smaller devices to undo previous function
    @objc func keyboardWillHide(notification: NSNotification) {
        // Find a better way to detect smaller screens
        if(UIScreen.main.bounds.height > 568.0) {
            return;
        }
        
        let info = notification.userInfo!;
        let duration: TimeInterval = (info[UIKeyboardAnimationDurationUserInfoKey] as! NSNumber).doubleValue;
        
        fakeKeyboardView?.removeFromSuperview();
        fakeKeyboardView = nil;
        
        
        UIView.animate(withDuration: duration, animations: {
            self.view.layoutIfNeeded();
        });
    }
    
    // Hides the keyboard
    @objc func dismissKeyboard() {
        view.endEditing(true);
    }
    
    // Set status bar to have white text
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent;
    }
    
    // Prevent Auto Rotation
    override var shouldAutorotate: Bool {
        return false;
    }
    
    // Keyboard return callback
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if(textField == usernameField) {
            passwordField.becomeFirstResponder();
        } else {
            passwordField.resignFirstResponder();
        }

        return false;
    }
}


