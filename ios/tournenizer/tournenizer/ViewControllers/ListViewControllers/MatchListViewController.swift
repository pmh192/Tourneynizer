//
//  MatchListViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/13/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class MatchListViewController : UITableViewController {
    var teams: [[Team]] = [];
    var referees: [User] = [];
    var matches: [Match] = [];
    let cellIdentifier = "MatchCell";
    let cellSpacingHeight: CGFloat = 5;

    override func loadView() {
        super.loadView();

        view.backgroundColor = Constants.color.lightGray;
        tableView.allowsSelection = true;
        tableView.separatorStyle = .none;
        tableView.register(MatchCellView.self, forCellReuseIdentifier: cellIdentifier);
        tableView.rowHeight = UITableViewAutomaticDimension;
        tableView.estimatedRowHeight = 50;
    }

    func setData(matches: [Match], referees: [User], teams: [[Team]]) {
        self.teams = teams;
        self.referees = referees;
        self.matches = matches;
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

    override func numberOfSections(in tableView: UITableView) -> Int {
        return teams.count;
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

    var cb: ((Match, [Team], User) -> Void)?;

    func setSelectCallback(_ cb: @escaping ((Match, [Team], User) -> Void)) {
        self.cb = cb;
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        cb?(matches[indexPath.section], teams[indexPath.section], referees[indexPath.section]);
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: MatchCellView? = tableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? MatchCellView;
        cell?.setData(team1: teams[indexPath.section][0], team2: teams[indexPath.section][1], referee: referees[indexPath.section])

        cell?.setNeedsUpdateConstraints();
        cell?.updateConstraintsIfNeeded();

        if(cell != nil) {
            return cell!;
        } else {
            return UITableViewCell();
        }
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
