//
//  UserListViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/17/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class UserListViewController : UITableViewController {
    var users: [User] = [];
    let cellIdentifier = "UserCell";
    let cellSpacingHeight: CGFloat = 5;
    var cb: ((User) -> Void)?;
    var separatorColor = UIColor.clear;
    var editable = false;

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated);
    }

    override func loadView() {
        super.loadView();

        view.backgroundColor = Constants.color.white;
        tableView.allowsSelection = true;
        tableView.separatorStyle = .none;
        tableView.register(UserCellView.self, forCellReuseIdentifier: cellIdentifier);
        tableView.rowHeight = UITableViewAutomaticDimension;
        tableView.estimatedRowHeight = 50;
    }

    func setUsers(_ users: [User]) {
        self.users = users;
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
        return users.count;
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1;
    }

    override func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return cellSpacingHeight;
    }

    override func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView();
        headerView.backgroundColor = separatorColor;
        return headerView;
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: UserCellView? = tableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? UserCellView;
        cell?.setUser(users[indexPath.section]);
        cell?.setNeedsUpdateConstraints();
        cell?.updateConstraintsIfNeeded();

        if(cell != nil) {
            return cell!;
        } else {
            return UITableViewCell();
        }
    }

    func addSelectionCallback(_ cb: @escaping (User) -> Void) {
        self.cb = cb;
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        cb?(users[indexPath.section]);
    }

    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return editable;
    }

    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if (editingStyle == .delete) {
            users.remove(at: indexPath.section)
            tableView.reloadData();
        }
    }

    func addUser(_ user: User) {
        users.append(user);
        self.tableView.reloadData();
    }
}

