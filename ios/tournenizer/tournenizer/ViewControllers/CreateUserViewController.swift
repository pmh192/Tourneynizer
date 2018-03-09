//
//  ViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 1/29/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class CreateUserViewController: UIViewController, UITextFieldDelegate {
    // Declarations for all subviews
    var loginPrompt: UILabel!;
    var loginButton: UIButton!;
    var loginStack: UIStackView!;
    var registerButton: UIButton!;
    var passwordField: UITextField!;
    var reenterPasswordField: UITextField!;
    var nameField: UITextField!;
    var emailField: UITextField!;
    var registerStack: UIStackView!;
    var errorPrompt: UILabel!;

    // Definitions for all style related constants
    let loginPromptText = "Already have an account?";
    let loginButtonText = "Sign in.";
    let registerButtonText = "Register";
    let passwordFieldPrompt = "Password";
    let reenterPasswordFieldPrompt = "Re-enter password";
    let emailFieldPrompt = "Email";
    var nameFieldPrompt = "Name";
    let passwordNoMatchError = "Passwords don't match.";
    let emptyFieldError = "Please fill in all fields.";
    let loginPromptPadding: CGFloat = 10;
    let registerButtonBorderRadius: CGFloat = 5;
    let registerButtonBorderWidth: CGFloat = 5;
    let registerButtonBottomPadding: CGFloat = 30;
    let registerStackVerticalPadding: CGFloat = 15;
    let registerStackSidePadding: CGFloat = 30;

    override func loadView() {
        // Initialize view
        view = UIView();
        view.backgroundColor = Constants.color.white;

        // Initialize all subviews
        loginPrompt = {
            let view = UILabel.newAutoLayout();
            view.text = loginPromptText;
            view.textColor = Constants.color.gray;
            view.textAlignment = .center;
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            return view;
        }();

        errorPrompt = {
            let view = UILabel.newAutoLayout();
            view.textColor = Constants.color.red;
            view.textAlignment = .center;
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            return view;
        }();

        loginButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(loginButtonText, for: .normal);
            view.setTitleColor(Constants.color.lightBlue, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            return view;
        }();

        nameField = {
            let view = UITextField.newAutoLayout();
            view.placeholder = nameFieldPrompt;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.textAlignment = .center;
            view.textColor = Constants.color.navy;
            view.returnKeyType = .next;
            return view;
        }();

        emailField = {
            let view = UITextField.newAutoLayout();
            view.placeholder = emailFieldPrompt;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.textAlignment = .center;
            view.textColor = Constants.color.navy;
            view.returnKeyType = .next;
            view.keyboardType = .emailAddress;
            return view;
        }();

        passwordField = {
            let view = UITextField.newAutoLayout();
            view.placeholder = passwordFieldPrompt;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.textAlignment = .center;
            view.isSecureTextEntry = true;
            view.textColor = Constants.color.navy;
            view.returnKeyType = .next;
            return view;
        }();

        reenterPasswordField = {
            let view = UITextField.newAutoLayout();
            view.placeholder = reenterPasswordFieldPrompt;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.textAlignment = .center;
            view.isSecureTextEntry = true;
            view.textColor = Constants.color.navy;
            view.returnKeyType = .go;
            return view;
        }();

        registerButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(registerButtonText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = registerButtonBorderRadius;
            view.layer.borderWidth = registerButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();

        loginStack = { [loginPrompt, loginButton] in
            let view = UIStackView();
            view.axis = .vertical;
            view.addSubview(loginPrompt!);
            view.addSubview(loginButton!);
            return view;
        }();

        registerStack = { [nameField, emailField, passwordField, reenterPasswordField, registerButton, errorPrompt] in
            let view = UIStackView();
            view.axis = .vertical;
            view.addSubview(errorPrompt!);
            view.addSubview(nameField!);
            view.addSubview(emailField!);
            view.addSubview(passwordField!);
            view.addSubview(reenterPasswordField!);
            view.addSubview(registerButton!);
            return view;
        }();

        // Setup callback to hide keyboard on outside tap
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard));
        tap.cancelsTouchesInView = false;
        view.addGestureRecognizer(tap);

        // Delegate View Controller to respond to events
        loginButton.addTarget(self, action: #selector(exit(_:)), for: .touchUpInside);
        registerButton.addTarget(self, action: #selector(register), for: .touchUpInside);
        nameField.delegate = self;
        emailField.delegate = self;
        passwordField.delegate = self;
        reenterPasswordField.delegate = self;

        // Add all subviews and trigger contraint setup
        view.addSubview(registerStack);
        view.addSubview(loginStack);
        view.setNeedsUpdateConstraints();
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated);

        // Set status bar style
        self.navigationController?.navigationBar.barStyle = .default;
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            loginStack.autoPin(toBottomLayoutGuideOf: self, withInset: 0);
            loginStack.autoPinEdge(toSuperviewEdge: .left);
            loginStack.autoPinEdge(toSuperviewEdge: .right);

            loginButton.autoPinEdge(toSuperviewEdge: .bottom, withInset: loginPromptPadding);
            loginButton.autoCenterInSuperview();

            loginPrompt.autoPinEdge(.bottom, to: .top, of: loginButton);
            loginPrompt.autoPinEdge(toSuperviewEdge: .leading);
            loginPrompt.autoPinEdge(toSuperviewEdge: .trailing);

            registerStack.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            registerStack.autoPinEdge(.bottom, to: .top, of: loginStack);
            registerStack.autoPinEdge(toSuperviewEdge: .left, withInset: registerStackSidePadding, relation: .lessThanOrEqual);
            registerStack.autoPinEdge(toSuperviewEdge: .right, withInset: registerStackSidePadding, relation: .lessThanOrEqual);

            passwordField.autoCenterInSuperview();
            passwordField.autoPinEdge(toSuperviewEdge: .leading);
            passwordField.autoPinEdge(toSuperviewEdge: .trailing);

            emailField.autoPinEdge(.bottom, to: .top, of: passwordField, withOffset: -registerStackVerticalPadding);
            emailField.autoPinEdge(toSuperviewEdge: .leading);
            emailField.autoPinEdge(toSuperviewEdge: .trailing);

            nameField.autoPinEdge(.bottom, to: .top, of: emailField, withOffset: -registerStackVerticalPadding);
            nameField.autoPinEdge(toSuperviewEdge: .leading);
            nameField.autoPinEdge(toSuperviewEdge: .trailing);

            errorPrompt.autoPinEdge(.bottom, to: .top, of: nameField, withOffset: -registerStackVerticalPadding);
            errorPrompt.autoPinEdge(toSuperviewEdge: .leading);
            errorPrompt.autoPinEdge(toSuperviewEdge: .trailing);

            reenterPasswordField.autoPinEdge(.top, to: .bottom, of: passwordField, withOffset: registerStackVerticalPadding);
            reenterPasswordField.autoPinEdge(toSuperviewEdge: .leading);
            reenterPasswordField.autoPinEdge(toSuperviewEdge: .trailing);

            registerButton.autoPinEdge(.top, to: .bottom, of: reenterPasswordField, withOffset: registerStackVerticalPadding);
            registerButton.autoPinEdge(toSuperviewEdge: .leading);
            registerButton.autoPinEdge(toSuperviewEdge: .trailing);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    // Login onclick function
    @objc func exit(_ sender: UIButton!) {
        self.navigationController?.popViewController(animated: true);
    }

    @objc func register() {
        if(emailField.text == nil || emailField.text == "" ||
           nameField.text == nil || nameField.text == "" ||
           passwordField.text == nil || passwordField.text == "" ||
           reenterPasswordField.text == nil || reenterPasswordField.text == "") {
            setError(emptyFieldError);
            return;
        }

        if(passwordField.text != reenterPasswordField.text) {
            setError(passwordNoMatchError);
            return;
        }

        setError("");
        let email = emailField.text!;
        let name = nameField.text!;
        let password = passwordField.text!;

        let user = User(email: email, name: name, password: password);
        UserService.shared.createUser(user, cb: { (error: String?, newUser: User?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.setError(error!);
                };
            }

            return DispatchQueue.main.async {
                self.clearFields();
                self.exit(self.loginButton);
            };
        });
    }

    // Keyboard return callback
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        switch(textField) {
            case nameField:
                emailField.becomeFirstResponder();
            case emailField:
                passwordField.becomeFirstResponder();
            case passwordField:
                reenterPasswordField.becomeFirstResponder();
            case reenterPasswordField:
                fallthrough;
            default:
                register();
                reenterPasswordField.resignFirstResponder();
        };

        return false;
    }

    func clearFields() {
        emailField.text = "";
        nameField.text = "";
        passwordField.text = "";
        reenterPasswordField.text = "";
    }

    func setError(_ error: String) {
        errorPrompt.text = error;
        self.view.setNeedsLayout();
        self.view.layoutIfNeeded();
    }
}
