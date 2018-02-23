//
//  MyTournamentsViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/18/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class MyTournamentsViewController : UIViewController {
    let tournamentList = TournamentListViewController();

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;

        tournamentList.setTournaments([
            Tournament(id: 0, name: "Tournament of The Champions of the Void of Los Angeles", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2)
        ]);
        tournamentList.setSelectCallback(selectTournament(_:));

        addChildViewController(tournamentList);
        tournamentList.view.frame = view.bounds;
        view.addSubview(tournamentList.view);
        tournamentList.didMove(toParentViewController: self);

        view.setNeedsUpdateConstraints();
    }

    func selectTournament(_ tournament: Tournament) {
        let vc = TournamentViewController();
        vc.setJoinable(false);
        vc.setTournament(tournament);
        self.navigationController?.pushViewController(vc, animated: true);
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        super.updateViewConstraints();
    }
}


