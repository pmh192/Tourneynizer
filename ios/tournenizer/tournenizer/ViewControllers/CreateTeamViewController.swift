//
//  CreateTeamViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/18/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;

class CreateTeamViewController : UIViewController, UITextFieldDelegate {
    var selectLabel: UILabel!;
    var contentView: UIView!;
    var statusBarCover: UIView!;
    var backView: UIButton!;
    var namePrompt: UILabel!;
    var nameField: UITextField!;
    var rosterPrompt: UILabel!;
    var rosterButton: UIButton!;
    var createButton: UIButton!;
    var errorPrompt: UILabel!;

    let selectText = "Create a team.";
    let dialogTitle = "Team Created";
    let dialogBody = "You have successfully created your team %@.";
    let dialogButtonText = "Ok";
    let namePromptText = "Name:";
    let createButtonText = "Create";
    let rosterPromptText = "Roster:";
    let rosterButtonText = "Add Player";
    let errorExistsText = "Team already exists for tournament.";
    let errorEmptyText = "Team name cannot be empty.";

    let actionsBarHeight: CGFloat = 50;
    let topTitlePadding: CGFloat = 20;
    let sideTitlePadding: CGFloat = 15;
    let buttonPadding: CGFloat = 10;
    let iconSize: CGFloat = 25;
    let contentPadding: CGFloat = 5;
    let errorBorderWidth: CGFloat = 1;
    let promptTopPadding: CGFloat = 25;
    let promptPadding: CGFloat = 10;
    let leftPercentWidth: CGFloat = 0.2;
    let buttonBorderWidth: CGFloat = 1;
    let buttonBorderRadius: CGFloat = 5;
    let buttonWidth: CGFloat = 100;

    let userListController = UserListViewController();

    private func promptGenerator() -> UILabel {
        let view = UILabel.newAutoLayout();
        view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
        view.textColor = Constants.color.darkGray;
        return view;
    }

    private func fieldGenerator() -> UITextField {
        let view = UITextField.newAutoLayout();
        view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
        view.textColor = Constants.color.navy;
        view.returnKeyType = .done;
        view.backgroundColor = Constants.color.lightGray;
        view.textAlignment = .center;
        view.delegate = self;
        view.layer.borderWidth = errorBorderWidth;
        view.layer.borderColor = UIColor.clear.cgColor;
        return view;
    }

    private func buttonGenerator() -> UIButton {
        let view = UIButton.newAutoLayout();
        view.setTitleColor(Constants.color.white, for: .normal);
        view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
        view.layer.cornerRadius = buttonBorderRadius;
        view.layer.borderWidth = buttonBorderWidth;
        view.layer.borderColor = Constants.color.lightBlue.cgColor;
        view.backgroundColor = Constants.color.lightBlue;
        view.titleLabel?.lineBreakMode = .byCharWrapping;
        return view;
    }

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            return view;
        }();

        backView = {
            let view = UIButton.newAutoLayout();
            let image = UIImage(named: "arrowright")?.withRenderingMode(.alwaysTemplate);
            view.setImage(image, for: UIControlState.normal);
            view.imageView?.transform = CGAffineTransform(scaleX: -1, y: 1);
            view.imageView?.tintColor = Constants.color.white;
            view.contentMode = .scaleAspectFit;
            return view;
        }();
        backView.addTarget(self, action: #selector(exit), for: .touchUpInside);

        selectLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.smallHeader);
            view.textColor = Constants.color.white;
            view.text = selectText;
            view.textAlignment = .center;
            return view;
        }();

        namePrompt = promptGenerator();
        namePrompt.text = namePromptText;

        rosterPrompt = promptGenerator();
        rosterPrompt.text = rosterPromptText;

        nameField = fieldGenerator();

        errorPrompt = promptGenerator();
        errorPrompt.textColor = Constants.color.red;
        errorPrompt.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.small);

        contentView = UIView.newAutoLayout();
        contentView.backgroundColor = Constants.color.lightGray;

        createButton = buttonGenerator();
        createButton.setTitle(createButtonText, for: .normal);
        createButton.addTarget(self, action: #selector(create), for: .touchUpInside);

        rosterButton = buttonGenerator();
        rosterButton.setTitle(rosterButtonText, for: .normal);
        rosterButton.addTarget(self, action: #selector(addPlayer), for: .touchUpInside);

        userListController.tableView.backgroundColor = Constants.color.lightGray;
        userListController.tableView.allowsSelection = false;
        userListController.editable = true;
        userListController.separatorColor = Constants.color.white;
        userListController.tableView.allowsMultipleSelectionDuringEditing = false;
        userListController.addUser(User(email: "ryanl.wiener@gmail.com", name: "Ryan Wiener", timeCreated: Date()));

        addChildViewController(userListController);
        contentView.addSubview(userListController.view);
        userListController.view.frame = contentView.bounds;
        userListController.didMove(toParentViewController: self);

        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard));
        tap.cancelsTouchesInView = false;
        view.addGestureRecognizer(tap);

        view.addSubview(statusBarCover);
        view.addSubview(backView);
        view.addSubview(selectLabel);
        view.addSubview(contentView);
        view.addSubview(namePrompt);
        view.addSubview(nameField);
        view.addSubview(rosterPrompt);
        view.addSubview(rosterButton);
        view.addSubview(contentView);
        view.addSubview(createButton);
        view.addSubview(errorPrompt);
        view.setNeedsUpdateConstraints();
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight + actionsBarHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            backView.autoSetDimension(.width, toSize: iconSize);
            backView.autoMatch(.height, to: .width, of: backView);
            backView.autoPinEdge(.bottom, to: .bottom, of: statusBarCover, withOffset: -buttonPadding);
            backView.autoPinEdge(toSuperviewEdge: .leading, withInset: buttonPadding);

            selectLabel.autoPinEdge(.leading, to: .trailing, of: backView, withOffset: sideTitlePadding);
            selectLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            selectLabel.autoPinEdge(.bottom, to: .bottom, of: statusBarCover, withOffset: -buttonPadding);

            namePrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            namePrompt.autoPinEdge(.top, to: .bottom, of: statusBarCover, withOffset: promptTopPadding);
            namePrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            nameField.autoAlignAxis(.baseline, toSameAxisOf: namePrompt);
            nameField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            nameField.autoPinEdge(.leading, to: .trailing, of: namePrompt);

            errorPrompt.autoPinEdge(.leading, to: .leading, of: nameField);
            errorPrompt.autoPinEdge(.top, to: .bottom, of: nameField);

            rosterPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            rosterPrompt.autoPinEdge(.top, to: .bottom, of: namePrompt, withOffset: promptTopPadding);
            rosterPrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            rosterButton.autoAlignAxis(.baseline, toSameAxisOf: rosterPrompt);
            rosterButton.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            rosterButton.autoSetDimension(.width, toSize: buttonWidth);

            createButton.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            createButton.autoPinEdge(toSuperviewEdge: .bottom, withInset: promptPadding);
            createButton.autoSetDimension(.width, toSize: buttonWidth);

            contentView.autoPinEdge(.top, to: .bottom, of: rosterButton, withOffset: promptPadding);
            contentView.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            contentView.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            contentView.autoPinEdge(.bottom, to: .top, of: createButton, withOffset: -promptPadding);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }

    @objc func create() {
        var error = false;

        if(nameField.text == nil || nameField.text == "") {
            nameField.layer.borderColor = Constants.color.red.cgColor;
            errorPrompt.text = errorEmptyText;
            error = true;
        } else {
            nameField.layer.borderColor = UIColor.clear.cgColor;
            errorPrompt.text = "";
        }

        let name = nameField.text!;
        if(false) {
            //name already exists
            nameField.layer.borderColor = Constants.color.red.cgColor;
            errorPrompt.text = errorExistsText;
            error = true;
        }

        if(error) {
            return;
        }

        let alert = UIAlertController(title: dialogTitle, message: String(format: dialogBody, nameField.text ?? ""), preferredStyle: .alert);
        alert.addAction(UIAlertAction(title: dialogButtonText, style: .default, handler: { _ in
            self.navigationController?.popToRootViewController(animated: true);
        }));
        self.present(alert, animated: true, completion: nil);
    }

    func addUser(_ user: User) {
        self.navigationController?.popViewController(animated: true);
        userListController.addUser(user);
    }

    @objc func addPlayer() {
        let vc = PlayersViewController();
        vc.cb = addUser(_:);
        self.navigationController?.pushViewController(vc, animated: true);
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        nameField.resignFirstResponder();
        return false;
    }

}

