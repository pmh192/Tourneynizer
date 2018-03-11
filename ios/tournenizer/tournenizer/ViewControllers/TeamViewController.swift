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
    var addUserButton: UIButton!;
    var pendingRequestsButton: UIButton!;
    var memberSwitch: UISegmentedControl!;

    let selectText = "Team Information";
    let namePromptText = "Name:";
    let addButtonText = "Add User";
    let pendingButtonText = "Pending Requests";
    let registeredMembersText = "Registered Members";
    let pendingMembersText = "Pending Members";
    let dialogTitle = "User Request Sent";
    let dialogBody = "You have successfully requested the user to join your team.";
    let dialogButtonText = "Ok";

    let actionsBarHeight: CGFloat = 35;
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
    let mainButtonBorderRadius: CGFloat = 5;
    let mainButtonBorderWidth: CGFloat = 5;

    var team: Team!;
    var creator = false;

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

        addUserButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(addButtonText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = mainButtonBorderRadius;
            view.layer.borderWidth = mainButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();

        addUserButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(addButtonText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = mainButtonBorderRadius;
            view.layer.borderWidth = mainButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        addUserButton.addTarget(self, action: #selector(openPlayerSelector), for: .touchUpInside);

        pendingRequestsButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(pendingButtonText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = mainButtonBorderRadius;
            view.layer.borderWidth = mainButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        pendingRequestsButton.addTarget(self, action: #selector(openPendingRequests), for: .touchUpInside);

        memberSwitch = {
            let view = UISegmentedControl.newAutoLayout();
            view.insertSegment(withTitle: registeredMembersText, at: 0, animated: false);
            view.insertSegment(withTitle: pendingMembersText, at: 1, animated: false);
            view.tintColor = Constants.color.navy;
            view.selectedSegmentIndex = 0;
            return view;
        }();
        memberSwitch.addTarget(self, action: #selector(selectorChanged), for: .valueChanged);

        namePrompt = promptGenerator();
        namePrompt.text = namePromptText;

        nameField = promptGenerator();
        nameField.text = team.name;

        contentView = UIView.newAutoLayout();
        contentView.backgroundColor = Constants.color.lightGray;

        userListController.tableView.backgroundColor = Constants.color.lightGray;
        userListController.tableView.allowsSelection = false;
        userListController.editable = false;
        userListController.separatorColor = Constants.color.white;
        userListController.setReloadCallback {
            self.loadUsers();
        }
        loadUsers();

        addChildViewController(userListController);
        contentView.addSubview(userListController.view);
        userListController.view.frame = contentView.bounds;
        userListController.didMove(toParentViewController: self);

        creator = UserService.shared.getCurrentUser()!.id == team.creatorId;

        if(creator) {
            view.addSubview(memberSwitch);
            view.addSubview(addUserButton);
            view.addSubview(pendingRequestsButton);
        }

        view.addSubview(statusBarCover);
        view.addSubview(backView);
        view.addSubview(selectLabel);
        view.addSubview(contentView);
        view.addSubview(namePrompt);
        view.addSubview(nameField);
        view.addSubview(contentView);
        view.setNeedsUpdateConstraints();
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            statusBarCover.autoSetDimension(.height, toSize: actionsBarHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            backView.autoSetDimension(.width, toSize: iconSize);
            backView.autoMatch(.height, to: .width, of: backView);
            backView.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            backView.autoPinEdge(toSuperviewEdge: .leading, withInset: buttonPadding);

            selectLabel.autoPinEdge(.leading, to: .trailing, of: backView, withOffset: sideTitlePadding);
            selectLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            selectLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);

            namePrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            namePrompt.autoPinEdge(.top, to: .bottom, of: statusBarCover, withOffset: promptTopPadding);
            namePrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            nameField.autoAlignAxis(.baseline, toSameAxisOf: namePrompt);
            nameField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            nameField.autoPinEdge(.leading, to: .trailing, of: namePrompt);

            if(creator) {
                memberSwitch.autoPinEdge(.top, to: .bottom, of: namePrompt, withOffset: promptTopPadding);
                memberSwitch.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
                memberSwitch.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
                contentView.autoPinEdge(.top, to: .bottom, of: memberSwitch, withOffset: contentPadding);
            } else {
                contentView.autoPinEdge(.top, to: .bottom, of: namePrompt, withOffset: contentPadding);
            }

            contentView.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            contentView.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);

            if(creator) {
                let views: NSArray = [pendingRequestsButton, addUserButton] as NSArray;
                views.autoDistributeViews(along: .horizontal, alignedTo: .horizontal, withFixedSpacing: promptPadding, insetSpacing: true, matchedSizes: true);
                pendingRequestsButton.autoPinEdge(toSuperviewEdge: .bottom, withInset: contentPadding);
                contentView.autoPinEdge(.bottom, to: .top, of: pendingRequestsButton, withOffset: -promptPadding);
            } else {
                contentView.autoPinEdge(toSuperviewEdge: .bottom, withInset: promptPadding);
            }

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

    @objc func selectorChanged() {
        loadUsers();
    }

    func loadUsers() {
        if(memberSwitch.selectedSegmentIndex == 0) {
            TeamService.shared.getTeamMembers(team.id) { (error: String?, users: [User]?) in
                if(error != nil) {
                    return DispatchQueue.main.async {
                        self.displayError(error!);
                    }
                }

                return DispatchQueue.main.async {
                    self.userListController.setUsers(users!);
                }
            }
        } else {
            TeamRequestService.shared.getRequestsByTeam(team.id, cb: { (error: String?, teamRequests: [TeamRequest]?, tournaments: [Tournament]?, users: [User]?, teams: [Team]?) in
                if(error != nil) {
                    return DispatchQueue.main.async {
                        self.displayError(error!);
                    }
                }

                if(teamRequests!.count > 0) {
                    print("CurrentUser: " + self.team.creatorId.description);
                    print("Requester: " + teamRequests![0].requesterId.description);
                    print("Target: " + teamRequests![0].userId.description)
                }

                return DispatchQueue.main.async {
                    self.userListController.setUsers(users!);
                }
            });
        }

    }

    @objc func openPlayerSelector() {
        let vc = UsersViewController();
        vc.setCallback(cb: addUser(_:));
        vc.setNavigatable(true);
        self.navigationController?.pushViewController(vc, animated: true);
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
                if(self.memberSwitch.selectedSegmentIndex == 1) {
                    self.userListController.addUser(user);
                }

                let alert = UIAlertController(title: self.dialogTitle, message: self.dialogBody, preferredStyle: .alert);
                alert.addAction(UIAlertAction(title: self.dialogButtonText, style: .default, handler: nil));
                self.present(alert, animated: true, completion: nil);
            }
        }
    }

    @objc func openPendingRequests() {
        let vc = PendingTeamRequestsViewController();
        vc.setTeam(team);
        self.navigationController?.pushViewController(vc, animated: true);
    }
}


