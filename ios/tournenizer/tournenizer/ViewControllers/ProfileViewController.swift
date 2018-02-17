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

    var titleLabel: UILabel!;
    var emailLabel: UILabel!;
    var teamsTitleLabel: UILabel!;
    var statusBarCover: UIView!;
    var contentView: UIView!;

    let topTitlePadding: CGFloat = 30;
    let sideTitlePadding: CGFloat = 15;
    let emailPadding: CGFloat = 10;
    let teamsPadding: CGFloat = 7.5;
    let teamsTitle = "Past Teams:";

    var profileList: ProfileTeamsViewController!;

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.lightGray;

        user = User(email: "ryanl.wiener@gmail.com", name: "Ryan Wiener", timeCreated: Date());

        titleLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.header);
            view.textAlignment = .center;
            view.textColor = Constants.color.red;
            view.text = user.name;
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            return view;
        }();

        emailLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            view.textAlignment = .center;
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
        view.addSubview(teamsTitleLabel);
        view.addSubview(contentView);
        view.setNeedsUpdateConstraints();
    }

    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            titleLabel.autoPinEdge(toSuperviewEdge: .top, withInset: topTitlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);

            emailLabel.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: emailPadding);
            emailLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: emailPadding);
            emailLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: emailPadding);

            teamsTitleLabel.autoPinEdge(.top, to: .bottom, of: emailLabel, withOffset: topTitlePadding);
            teamsTitleLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: teamsPadding);
            teamsTitleLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: teamsPadding);

            contentView.autoPinEdge(.top, to: .bottom, of: teamsTitleLabel, withOffset: teamsPadding);
            contentView.autoPinEdge(toSuperviewEdge: .bottom, withInset: teamsPadding);
            contentView.autoPinEdge(toSuperviewEdge: .trailing, withInset: teamsPadding);
            contentView.autoPinEdge(toSuperviewEdge: .leading, withInset: teamsPadding);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    func setUser(_ user: User) {
        self.user = user;
    }
}
