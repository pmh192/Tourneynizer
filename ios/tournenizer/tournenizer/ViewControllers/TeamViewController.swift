//
//  TeamViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/18/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class TeamViewController : UIViewController {
    var selectLabel: UILabel!;
    var contentView: UIView!;
    var statusBarCover: UIView!;
    var backView: UIButton!;
    var namePrompt: UILabel!;
    var nameField: UILabel!;
    var rosterPrompt: UILabel!;

    let selectText = "Team Information";
    let namePromptText = "Name:";
    let rosterPromptText = "Roster:";

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

    var team: Team!;

    let userListController = UserListViewController();

    private func promptGenerator() -> UILabel {
        let view = UILabel.newAutoLayout();
        view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
        view.textColor = Constants.color.darkGray;
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

        nameField = promptGenerator();
        nameField.text = team.name;

        contentView = UIView.newAutoLayout();
        contentView.backgroundColor = Constants.color.lightGray;

        userListController.tableView.backgroundColor = Constants.color.lightGray;
        userListController.tableView.allowsSelection = false;
        userListController.editable = false;
        userListController.separatorColor = Constants.color.white;
        userListController.addUser(User(email: "ryanl.wiener@gmail.com", name: "Ryan Wiener", timeCreated: Date()));

        addChildViewController(userListController);
        contentView.addSubview(userListController.view);
        userListController.view.frame = contentView.bounds;
        userListController.didMove(toParentViewController: self);

        view.addSubview(statusBarCover);
        view.addSubview(backView);
        view.addSubview(selectLabel);
        view.addSubview(contentView);
        view.addSubview(namePrompt);
        view.addSubview(nameField);
        view.addSubview(rosterPrompt);
        view.addSubview(contentView);
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

            rosterPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            rosterPrompt.autoPinEdge(.top, to: .bottom, of: namePrompt, withOffset: promptTopPadding);
            rosterPrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            contentView.autoPinEdge(.top, to: .bottom, of: rosterPrompt, withOffset: promptPadding);
            contentView.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            contentView.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            contentView.autoPinEdge(toSuperviewEdge: .bottom, withInset: promptPadding);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }

    func setTeam(_ team: Team) {
        self.team = team;
    }

}


