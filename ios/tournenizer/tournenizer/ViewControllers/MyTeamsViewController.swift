//
//  MyTeamsViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/18/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//
import UIKit;
import PureLayout;

class MyTeamsViewController : UIViewController {
    let teamList = TeamListViewController();

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;

        teamList.setTeams([
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void"),
            Team(id: 0, name: "Team Coach", timeCreated: Date(), tournament: "Tournament of the Champions of the Void")
        ]);
        teamList.setSelectCallback(selectTeam(_:));
        teamList.tableView.allowsSelection = true;

        addChildViewController(teamList);
        teamList.view.frame = view.bounds;
        view.addSubview(teamList.view);
        teamList.didMove(toParentViewController: self);

        view.setNeedsUpdateConstraints();
    }

    func selectTeam(_ team: Team) {

    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }
}


