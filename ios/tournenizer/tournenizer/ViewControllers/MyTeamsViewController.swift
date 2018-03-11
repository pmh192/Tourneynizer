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

        teamList.tableView.allowsSelection = true;
        teamList.setReloadCallback {
            self.loadTeams();
        }

        teamList.setSelectCallback(selectTeam(_:))

        addChildViewController(teamList);
        teamList.view.frame = view.bounds;
        view.addSubview(teamList.view);
        teamList.didMove(toParentViewController: self);

        view.setNeedsUpdateConstraints();

        loadTeams();
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

    func loadTeams() {
        TeamService.shared.getAllTeamsForCurrentUser { (error: String?, teams: [Team]?, tournaments: [Tournament]?) in
            if(error != nil) {
                return;
            }

            DispatchQueue.main.async {
                self.teamList.setData(teams: teams!, tournaments: tournaments!);
            }
        }
    }

    func selectTeam(_ team: Team) {
        let vc = TeamViewController();
        vc.setTeam(team);
        self.navigationController?.pushViewController(vc, animated: true);
    }
}


