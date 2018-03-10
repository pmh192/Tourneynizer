//
//  UserActionsViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class UserActionsViewController : UIViewController {
    var statusBarCover: UIView!;
    var myTournamentsLabel: UIButton!;
    var myTeamsLabel: UIButton!;
    var teamRequestsLabel: UIButton!;
    var contentView: UIView!;

    var topBarControllers: [UIViewController] = [];


    var topBarButtons: [UIButton] = [];
    var currentTab = 0;

    let myTournamentsText = "My Tournaments";
    let myTeamsText = "My Teams";
    let teamRequestsText = "Team Requests";

    let topBarHeight: CGFloat = 50;

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.lightGray;

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            return view;
        }();

        myTournamentsLabel = {
            let view = UIButton.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.small);
            view.setTitle(myTournamentsText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.tag = 0;
            view.addTarget(self, action: #selector(buttonClicked(sender:)), for: .touchUpInside);
            return view;
        }();

        myTeamsLabel = {
            let view = UIButton.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.small);
            view.setTitle(myTeamsText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.tag = 1;
            view.addTarget(self, action: #selector(buttonClicked(sender:)), for: .touchUpInside);
            return view;
        }();

        teamRequestsLabel = {
            let view = UIButton.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.small);
            view.setTitle(teamRequestsText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.tag = 2;
            view.addTarget(self, action: #selector(buttonClicked(sender:)), for: .touchUpInside);
            return view;
        }();

        contentView = UIView.newAutoLayout();

        topBarButtons = [
            myTournamentsLabel,
            myTeamsLabel,
            teamRequestsLabel
        ]

        topBarControllers = [
            MyTournamentsViewController(),
            MyTeamsViewController(),
            MyTeamRequestsViewController()
        ].map {vc -> UIViewController in
            let viewController = UINavigationController();
            viewController.pushViewController(vc, animated: false);
            viewController.isNavigationBarHidden = true;
            return viewController;
        };

        view.addSubview(statusBarCover);
        view.addSubview(myTournamentsLabel);
        view.addSubview(myTeamsLabel);
        view.addSubview(teamRequestsLabel);
        view.addSubview(contentView);
        view.setNeedsUpdateConstraints();
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        buttonClicked(sender: topBarButtons[currentTab]);
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

            let views: NSArray = [
                myTournamentsLabel,
                myTeamsLabel,
                teamRequestsLabel
            ] as NSArray;
            views.autoSetViewsDimension(.height, toSize: topBarHeight);
            views.autoDistributeViews(along: .horizontal, alignedTo: .horizontal, withFixedSpacing: 0, insetSpacing: false, matchedSizes: true);
            myTournamentsLabel.autoPinEdge(.top, to: .bottom, of: statusBarCover);

            contentView.autoPinEdge(toSuperviewEdge: .leading);
            contentView.autoPinEdge(toSuperviewEdge: .trailing);
            contentView.autoPinEdge(toSuperviewEdge: .bottom);
            contentView.autoPinEdge(.top, to: .bottom, of: myTournamentsLabel);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    @objc func buttonClicked(sender: UIButton) {
        //undo currentTab
        topBarButtons[currentTab].setTitleColor(Constants.color.white, for: .normal);
        topBarControllers[currentTab].willMove(toParentViewController: nil);
        topBarControllers[currentTab].view.removeFromSuperview();
        topBarControllers[currentTab].removeFromParentViewController();

        //set currentTab
        currentTab = sender.tag;
        topBarButtons[currentTab].setTitleColor(Constants.color.red, for: .normal);
        addChildViewController(topBarControllers[currentTab]);
        topBarControllers[currentTab].view.frame = contentView.bounds;
        contentView.addSubview(topBarControllers[currentTab].view);
        topBarControllers[currentTab].didMove(toParentViewController: self);
    }
}
