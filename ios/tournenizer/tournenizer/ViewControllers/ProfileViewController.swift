//
//  ProfileViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class ProfileViewController : UIViewController {
    var user: User!;
    var currentProfile = true;
    var navigatable = false;

    var titleLabel: UILabel!;
    var emailLabel: UILabel!;
    var teamsTitleLabel: UILabel!;
    var statusBarCover: UIView!;
    var contentView: UIView!;
    var logoutButton: UIButton!;
    var backView: UIButton!;

    let topTitlePadding: CGFloat = 20;
    let sideTitlePadding: CGFloat = 15;
    let emailPadding: CGFloat = 10;
    let topTeamsPadding: CGFloat = 20;
    let teamsPadding: CGFloat = 7.5;
    let buttonHeight: CGFloat = 25;
    let logoutButtonBorderWidth: CGFloat = 1;
    let logoutButtonBorderRadius: CGFloat = 5;
    let actionsBarHeight: CGFloat = 50;
    let buttonPadding: CGFloat = 10;
    let iconSize: CGFloat = 25;
    let iconPadding: CGFloat = 15;

    let teamsTitle = "Past Teams:";
    let logoutText = "Sign Out";

    var profileList: ProfileTeamsViewController!;

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.lightGray;

        user = User(email: "ryanl.wiener@gmail.com", name: "Ryan Wiener", timeCreated: Date());

        titleLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.header);
            view.textColor = Constants.color.red;
            view.text = user.name;
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            return view;
        }();

        emailLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            view.textColor = Constants.color.navy;
            view.text = user.email;
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            return view;
        }();

        teamsTitleLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.smallHeader);
            view.textColor = Constants.color.navy;
            view.text = teamsTitle;
            return view;
        }();

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            return view;
        }();

        contentView = UIView.newAutoLayout();

        logoutButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(logoutText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = logoutButtonBorderRadius;
            view.layer.borderWidth = logoutButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        logoutButton.addTarget(self, action: #selector(logout), for: .touchUpInside);

        backView = {
            let view = UIButton.newAutoLayout();
            let image = UIImage(named: "arrowright")?.withRenderingMode(.alwaysTemplate);
            view.setImage(image, for: UIControlState.normal);
            view.imageView?.transform = CGAffineTransform(scaleX: -1, y: 1);
            view.imageView?.tintColor = Constants.color.white;
            view.contentMode = .scaleAspectFit;
            return view;
        }();
        backView.addTarget(self, action: #selector(back), for: .touchUpInside);


        profileList = ProfileTeamsViewController();
        profileList.setTeams([
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void")
        ]);

        addChildViewController(profileList);
        contentView.addSubview(profileList.view);
        profileList.view.frame = contentView.bounds;
        profileList.didMove(toParentViewController: self);

        view.addSubview(titleLabel);
        view.addSubview(statusBarCover);
        view.addSubview(emailLabel);
        if(currentProfile) {
            view.addSubview(logoutButton);
        }

        if(navigatable) {
            view.addSubview(backView);
        }
        view.addSubview(teamsTitleLabel);
        view.addSubview(contentView);
        view.setNeedsUpdateConstraints();
    }

    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight + actionsBarHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            titleLabel.autoPinEdge(.top, to: .bottom, of: statusBarCover, withOffset: topTitlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);

            emailLabel.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: emailPadding);
            emailLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            emailLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);

            teamsTitleLabel.autoPinEdge(.top, to: .bottom, of: emailLabel, withOffset: topTeamsPadding);
            teamsTitleLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: teamsPadding);
            teamsTitleLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: teamsPadding);

            contentView.autoPinEdge(.top, to: .bottom, of: teamsTitleLabel, withOffset: teamsPadding);
            contentView.autoPinEdge(toSuperviewEdge: .bottom, withInset: teamsPadding);
            contentView.autoPinEdge(toSuperviewEdge: .trailing, withInset: teamsPadding);
            contentView.autoPinEdge(toSuperviewEdge: .leading, withInset: teamsPadding);

            if(currentProfile) {
                logoutButton.autoPinEdge(.bottom, to: .bottom, of: statusBarCover, withOffset: -buttonPadding);
                logoutButton.autoPinEdge(toSuperviewEdge: .trailing, withInset: buttonPadding);
                logoutButton.autoMatch(.width, to: .width, of: view, withMultiplier: 0.3);
            }

            if(navigatable) {
                backView.autoSetDimension(.width, toSize: iconSize);
                backView.autoMatch(.height, to: .width, of: backView);
                backView.autoPinEdge(.bottom, to: .bottom, of: statusBarCover, withOffset: -buttonPadding);
                backView.autoPinEdge(toSuperviewEdge: .leading, withInset: buttonPadding);
            }

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    func setUser(_ user: User) {
        self.user = user;
    }

    func setCurrentProfile(_ currentProfile: Bool) {
        self.currentProfile = currentProfile;
    }

    func setNavigatable(_ navigatable: Bool) {
        self.navigatable = navigatable;
    }

    @objc func logout() {
        let animation = CATransition();
        animation.type = kCATransitionReveal;
        animation.subtype = kCATransitionFromBottom;
        animation.timingFunction = CAMediaTimingFunction(name: kCAMediaTimingFunctionEaseInEaseOut);
        animation.duration = CFTimeInterval(0.35);

        let window = UIApplication.shared.keyWindow;
        window?.layer.add(animation, forKey: nil);
        window?.rootViewController = LoginViewContainerController();
    }

    @objc func back() {
        self.navigationController?.popViewController(animated: true);
    }


}
