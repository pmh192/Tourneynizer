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
    var errorPrompt: UILabel!;

    // Definitions for all style related constants
    let logoLabelHeight: CGFloat = 150;
    let loginClusterSidePadding: CGFloat = 30;
    let loginClusterElementVerticalPadding: CGFloat = 15;
    let loginButtonBorderRadius: CGFloat = 5;
    let loginButtonBorderWidth: CGFloat = 5;
    let registerPromptPadding: CGFloat = 10;
    let passwordKeyboardLowResPadding: CGFloat = 20;
    let logoText = "Tourneynizer";
    let usernameFieldPrompt = "Email";
    let passwordFieldPrompt = "Password";
    let loginButtonText = "Login";
    let registerPromptText = "Don't have an account?";
    let registerButtonText = "Sign up.";
    let emptyFieldError = "Please fill in all fields.";

    override func loadView() {
        // Initialize view
        view = UIView();
        view.backgroundColor = Constants.color.white;

        // Initialize all subviews
        fakeKeyboardView = nil;

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            return view;
        }();

        logoLabel = {
            let view = UILabel.newAutoLayout();
            view.text = logoText;
            view.backgroundColor = Constants.color.navy;
            view.textColor = Constants.color.red;
            view.textAlignment = .center;
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.header);
            return view;
        }();

        errorPrompt = {
            let view = UILabel.newAutoLayout();
            view.textColor = Constants.color.red;
            view.textAlignment = .center;
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            return view;
        }();

        usernameField = {
            let view = UITextField.newAutoLayout();
            view.placeholder = usernameFieldPrompt;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.textAlignment = .center;
            view.textColor = Constants.color.navy;
            view.keyboardType = .emailAddress;
            view.returnKeyType = .next;
            return view;
        }();

        passwordField = {
            let view = UITextField.newAutoLayout();
            view.placeholder = passwordFieldPrompt;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.textAlignment = .center;
            view.isSecureTextEntry = true;
            view.textColor = Constants.color.navy;
            view.returnKeyType = .go;
            return view;
        }();

        loginButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(loginButtonText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = loginButtonBorderRadius;
            view.layer.borderWidth = loginButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();

        registerPrompt = {
            let view = UILabel.newAutoLayout();
            view.text = registerPromptText;
            view.textColor = Constants.color.gray;
            view.textAlignment = .center;
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            return view;
        }();

        registerButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(registerButtonText, for: .normal);
            view.setTitleColor(Constants.color.lightBlue, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            return view;
        }();

        loginStack = { [usernameField, passwordField, loginButton, errorPrompt] in
            let view = UIStackView();
            view.axis = .vertical;
            view.addSubview(errorPrompt!);
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

        // Setup callback to hide keyboard on outside tap
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard));
        tap.cancelsTouchesInView = false;
        view.addGestureRecognizer(tap);

        // Delegate View Controller to respond to events
        usernameField.delegate = self;
        passwordField.delegate = self;
        registerButton.addTarget(self, action: #selector(registerClicked(_:)), for: .touchUpInside);
        loginButton.addTarget(self, action: #selector(loginClicked(_:)), for: .touchUpInside);

        // Add all subviews and trigger contraint setup
        view.addSubview(statusBarCover);
        view.addSubview(logoLabel);
        view.addSubview(loginStack);
        view.addSubview(registerStack);
        view.setNeedsUpdateConstraints();
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight);
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

            errorPrompt.autoPinEdge(.bottom, to: .top, of: usernameField, withOffset: -loginClusterElementVerticalPadding);
            errorPrompt.autoPinEdge(toSuperviewEdge: .leading);
            errorPrompt.autoPinEdge(toSuperviewEdge: .trailing);

            registerButton.autoPinEdge(toSuperviewEdge: .bottom, withInset: registerPromptPadding);
            registerButton.autoCenterInSuperview();

            registerPrompt.autoPinEdge(.bottom, to: .top, of: registerButton);
            registerPrompt.autoPinEdge(toSuperviewEdge: .leading);
            registerPrompt.autoPinEdge(toSuperviewEdge: .trailing);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }


    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated);

        // Subscribes to the keyboard show and hide event for resizing
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: .UIKeyboardDidShow, object: nil);
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: .UIKeyboardWillHide, object: nil);

        // Set status bar style
        self.navigationController?.navigationBar.barStyle = .black;
    }


    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated);

        // Removes all subscriptions
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

        fakeKeyboardView?.removeFromSuperview();
        fakeKeyboardView = nil;

        // only move up for email field
        if(!passwordField.isFirstResponder) {
            let fakeView = UIView();
            view.addSubview(fakeView);

            fakeView.autoSetDimension(.height, toSize: keyboardHeight)
            fakeView.autoPinEdge(toSuperviewEdge: .bottom);
            fakeView.autoPinEdge(toSuperviewEdge: .left);
            fakeView.autoPinEdge(toSuperviewEdge: .right);
            fakeKeyboardView = fakeView;

            passwordField.autoPinEdge(.bottom, to: .top, of: fakeKeyboardView!, withOffset: -passwordKeyboardLowResPadding);
        }

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
            loginClicked(loginButton!);
        }

        return false;
    }

    // Register onclick function
    @objc func registerClicked(_ sender: UIButton!) {
        self.setError("");
        self.usernameField.text = "";
        self.passwordField.text = "";
        self.navigationController?.pushViewController(CreateUserViewController(), animated: true);
    }

    // Register onclick function
    @objc func loginClicked(_ sender: UIButton!) {
        if(usernameField.text == nil || usernameField.text == "" ||
           passwordField.text == nil || passwordField.text == "") {
            setError(emptyFieldError);
            return;
        }

        setError("");
        let user = User(email: usernameField.text!, name: "", password: passwordField.text!);
        
        UserService.shared.login(user: user) { (error: String?, user: User?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.setError(error!);
                }
            }

            return DispatchQueue.main.async {
                self.transitionToDashboard();
            }
        }
    }

    func transitionToDashboard() {
        let animation = CATransition();
        animation.type = kCATransitionReveal;
        animation.subtype = kCATransitionFromBottom;
        animation.timingFunction = CAMediaTimingFunction(name: kCAMediaTimingFunctionEaseInEaseOut);
        animation.duration = CFTimeInterval(0.35);

        let window = UIApplication.shared.keyWindow;
        window?.layer.add(animation, forKey: nil);
        window?.rootViewController = DashboardViewController();
    }

    func setError(_ error: String) {
        self.errorPrompt.text = error;
        self.view.setNeedsLayout();
        self.view.layoutIfNeeded();
    }
}
