//
//  TournamentListViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class TournamentListViewController : UITableViewController {
    var tournaments: [Tournament]!;
    let cellIdentifier = "TournamentCell";

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated);

        tournaments = [
            Tournament(id: 0, name: "Tournament 1", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 3, name: "Tournament 4", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil)
        ];
    }

    override func viewDidLoad() {
        view.backgroundColor = UIColor.lightGray;

        tableView.separatorStyle = .none;
        tableView.allowsSelection = false;
        tableView.register(TournamentTableCellView.self, forCellReuseIdentifier: cellIdentifier);
        tableView.rowHeight = UITableViewAutomaticDimension;
        tableView.estimatedRowHeight = 50;
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {

            tableView.autoPinEdgesToSuperviewEdges();

            didUpdateConstraints = true;
        }
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }


    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tournaments.count;
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: TournamentTableCellView? = tableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? TournamentTableCellView;
        cell?.setTournament(tournaments[indexPath.row]);
        cell?.setNeedsUpdateConstraints()
        cell?.updateConstraintsIfNeeded()

        if(cell != nil) {
            return cell!;
        } else {
            return UITableViewCell();
        }
    }
}
