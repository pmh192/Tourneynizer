//
//  RosterAddViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/9/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class RosterAddViewController : UIViewController {
    var selectLabel: UILabel!;
    var contentView: UIView!;
    var statusBarCover: UIView!;
    var backView: UIButton!;
    var rosterPrompt: UILabel!;
    var rosterButton: UIButton!;

    let selectText = "Create a team.";
    let dialogTitle = "User Request Sent";
    let dialogBody = "You have successfully requested the user to join your team.";
    let dialogButtonText = "Ok";
    let rosterPromptText = "Planned Roster:";
    let rosterButtonText = "Add Player";

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
    var team: Team!;

    private func promptGenerator() -> UILabel {
        let view = UILabel.newAutoLayout();
        view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
        view.textColor = Constants.color.darkGray;
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

        rosterPrompt = promptGenerator();
        rosterPrompt.text = rosterPromptText;

        contentView = UIView.newAutoLayout();
        contentView.backgroundColor = Constants.color.lightGray;

        rosterButton = buttonGenerator();
        rosterButton.setTitle(rosterButtonText, for: .normal);
        rosterButton.addTarget(self, action: #selector(addPlayer), for: .touchUpInside);

        userListController.tableView.backgroundColor = Constants.color.lightGray;
        userListController.tableView.allowsSelection = false;
        userListController.editable = true;
        userListController.separatorColor = Constants.color.white;
        userListController.tableView.allowsMultipleSelectionDuringEditing = false;
        userListController.addUser(UserService.shared.getCurrentUser()!);

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
        view.addSubview(rosterPrompt);
        view.addSubview(rosterButton);
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

            rosterButton.autoPinEdge(.top, to: .bottom, of: selectLabel, withOffset: promptTopPadding);
            rosterButton.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            rosterButton.autoSetDimension(.width, toSize: buttonWidth);

            rosterPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            rosterPrompt.autoPinEdge(.trailing, to: .leading, of: rosterButton, withOffset: promptPadding);
            rosterPrompt.autoAlignAxis(.baseline, toSameAxisOf: rosterButton);

            contentView.autoPinEdge(.top, to: .bottom, of: rosterButton, withOffset: promptPadding);
            contentView.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            contentView.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            contentView.autoPinEdge(toSuperviewEdge: .bottom, withInset: promptPadding);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    @objc func exit() {
        let vcIndex = self.navigationController!.viewControllers.count-3;
        let vc = self.navigationController?.viewControllers[vcIndex];
        self.navigationController?.popToViewController(vc!, animated: true);
    }

    func addUser(_ user: User) {
        self.navigationController?.popViewController(animated: true);

        TeamRequestService.shared.requestUserToJoinTeam(team: team, user: user) { (error: String?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }

            return DispatchQueue.main.async {
                self.userListController.addUser(user);
                let alert = UIAlertController(title: self.dialogTitle, message: self.dialogBody, preferredStyle: .alert);
                alert.addAction(UIAlertAction(title: self.dialogButtonText, style: .default, handler: nil));
                self.present(alert, animated: true, completion: nil);
            }
        }
    }

    @objc func addPlayer() {
        let vc = UsersViewController();
        vc.cb = addUser(_:);
        self.navigationController?.pushViewController(vc, animated: true);
    }

    func setTeam(_ team: Team) {
        self.team = team;
    }

}


