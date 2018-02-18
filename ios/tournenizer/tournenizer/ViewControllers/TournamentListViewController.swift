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
    var tournaments: [Tournament] = [];
    let cellIdentifier = "TournamentCell";
    let cellSpacingHeight: CGFloat = 5;

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated);

        tournaments = [
            Tournament(id: 0, name: "Tournament of The Champions of the Void of Los Angeles", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 1, name: "Tournament 2", description: "The tournament of champions of Isla Vista", address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
            Tournament(id: 2, name: "Tournament 3", description: nil, address: "Greek Park, Isla Vista", startTime: Date(), endTime: nil, maxTeams: 20, currentTeams: nil, timeCreated: Date(), tournamentType: nil, logo: nil, courts: nil, creatorId: 0, cancelled: nil, teamSize: 2),
        ];
    }

    override func loadView() {
        super.loadView();

        view.backgroundColor = Constants.color.lightGray;

        tableView.separatorStyle = .none;
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

        super.updateViewConstraints();
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return tournaments.count;
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1;
    }

    override func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return cellSpacingHeight;
    }

    override func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView();
        headerView.backgroundColor = UIColor.clear;
        return headerView;
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: TournamentTableCellView? = tableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? TournamentTableCellView;
        cell?.setTournament(tournaments[indexPath.section]);
        cell?.setNeedsUpdateConstraints()
        cell?.updateConstraintsIfNeeded()

        if(cell != nil) {
            return cell!;
        } else {
            return UITableViewCell();
        }
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let vc = TournamentViewController();
        vc.setTournament(tournaments[indexPath.section]);
        self.navigationController?.pushViewController(vc, animated: true);

    }
}
