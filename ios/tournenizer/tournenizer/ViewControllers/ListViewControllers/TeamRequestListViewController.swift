//
//  TeamRequestsListViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/18/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class TeamRequestListViewController : UITableViewController {
    var teamRequests: [TeamRequest] = [];
    var tournaments: [Tournament] = [];
    var users: [User] = [];
    var teams: [Team] = [];

    let cellIdentifier = "TeamRequestCell";
    let cellSpacingHeight: CGFloat = 5;
    
    override func viewDidLoad() {
        view.backgroundColor = Constants.color.lightGray;
        tableView.allowsSelection = true;
        tableView.separatorStyle = .none;
        tableView.register(TeamRequestCellView.self, forCellReuseIdentifier: cellIdentifier);
        tableView.rowHeight = UITableViewAutomaticDimension;
        tableView.estimatedRowHeight = 50;
    }

    func setData(teamRequests: [TeamRequest], tournaments: [Tournament], requesters: [User], teams: [Team]) {
        self.teamRequests = teamRequests;
        self.tournaments = tournaments;
        self.users = requesters;
        self.teams = teams;
        self.tableView.reloadData();
    }

    func removeElementAtIndex(_ index: Int) {
        teamRequests.remove(at: index);
        tournaments.remove(at: index);
        users.remove(at: index);
        self.tableView.reloadData();
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

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: TeamRequestCellView? = tableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? TeamRequestCellView;
        cell?.setTournament(tournaments[indexPath.section]);
        cell?.setRequester(users[indexPath.section]);
        cell?.setTeamRequest(teamRequests[indexPath.section]);
        cell?.setTeam(teams[indexPath.section]);
        cell?.setNeedsUpdateConstraints()
        cell?.updateConstraintsIfNeeded()

        if(cell != nil) {
            return cell!;
        } else {
            return UITableViewCell();
        }
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return teamRequests.count;
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

    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true;
    }

    var acceptCallback: ((TeamRequest, Int) -> Void)?;
    var rejectCallback: ((TeamRequest, Int) -> Void)?;

    func setAcceptCallback(_ cb: @escaping ((TeamRequest, Int) -> Void)) {
        self.acceptCallback = cb;
    }

    func setRejectCallback(_ cb: @escaping ((TeamRequest, Int) -> Void)) {
        self.rejectCallback = cb;
    }

    override func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        let rejectRowAction = UITableViewRowAction(style: UITableViewRowActionStyle.default, title: "Reject", handler:{action, indexpath in
            self.rejectCallback?(self.teamRequests[indexPath.section], indexPath.section);
        });
        rejectRowAction.backgroundColor = Constants.color.red;

        let approveRowAction = UITableViewRowAction(style: UITableViewRowActionStyle.default, title: "Approve", handler:{action, indexpath in
            self.acceptCallback?(self.teamRequests[indexPath.section], indexPath.section);
        });
        approveRowAction.backgroundColor = Constants.color.green;

        return [rejectRowAction, approveRowAction];
    }


    var reloadCallback: (() -> Void)?;

    func setReloadCallback(_ cb: @escaping (() -> Void)) {
        self.reloadCallback = cb;
    }

    override func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if(scrollView.contentOffset.y <= -50) {
            reloadCallback?();
        }
    }
}
