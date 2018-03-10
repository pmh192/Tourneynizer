//
//  PlayersViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class UsersViewController : UIViewController, UITextFieldDelegate {
    var statusBarCover: UIView!;
    var contentView: UIView!;
    var searchField: UITextField!;

    let searchHeight: CGFloat = 30;
    let searchPadding: CGFloat = 10;
    let playerPadding: CGFloat = 5;

    var userListController: UserListViewController!;
    let searchFieldPrompt = "Search...";
    var cb: ((User) -> Void)?;
    var users: [User] = [];


    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            return view;
        }();

        contentView = UIView.newAutoLayout();

        searchField = {
            let view = UITextField.newAutoLayout();
            view.placeholder = searchFieldPrompt;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.textColor = Constants.color.navy;
            view.textAlignment = .center;
            view.backgroundColor = Constants.color.lightGray;
            view.returnKeyType = .search;
            return view;
        }();
        searchField.delegate = self;
        searchField.addTarget(self, action: #selector(filterUsers), for: .editingChanged);

        userListController = UserListViewController();
        userListController.setReloadCallback {
            self.loadUsers();
        }
        loadUsers();

        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard));
        tap.cancelsTouchesInView = false;
        view.addGestureRecognizer(tap);

        addChildViewController(userListController);
        contentView.addSubview(userListController.view);
        userListController.view.frame = contentView.bounds;
        userListController.didMove(toParentViewController: self);

        if(cb != nil) {
            userListController.addSelectionCallback(cb!);
        } else {
            userListController.addSelectionCallback(selectUser(_:));
        }

        view.addSubview(statusBarCover);
        view.addSubview(searchField);
        view.addSubview(contentView);
        view.setNeedsUpdateConstraints();
    }

    func selectUser(_ user: User) {
        let vc = ProfileViewController();
        vc.setUser(user);
        vc.setNavigatable(true);
        vc.setCurrentProfile(false);
        self.navigationController?.pushViewController(vc, animated: true);
        dismissKeyboard();
    }

    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            searchField.autoPinEdge(.top, to: .bottom, of: statusBarCover, withOffset: searchPadding);
            searchField.autoSetDimension(.height, toSize: searchHeight);
            searchField.autoPinEdge(toSuperviewEdge: .leading, withInset: searchPadding);
            searchField.autoPinEdge(toSuperviewEdge: .trailing, withInset: searchPadding);

            contentView.autoPinEdge(.top, to: .bottom, of: searchField, withOffset: searchPadding);
            contentView.autoPinEdge(toSuperviewEdge: .leading, withInset: playerPadding);
            contentView.autoPinEdge(toSuperviewEdge: .trailing, withInset: playerPadding)
            contentView.autoPinEdge(toSuperviewEdge: .bottom, withInset: playerPadding)

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        searchField.resignFirstResponder();
        return false;
    }

    @objc func filterUsers() {
        if(searchField.text == nil || searchField.text == "") {
            self.userListController.setUsers(users);
            return;
        }

        let searchText = searchField.text!.lowercased();

        let filteredUsers = users.filter { (user: User) -> Bool in
            return (user.name.lowercased().contains(searchText)) ||
                   (user.email.lowercased().contains(searchText));
        }

        self.userListController.setUsers(filteredUsers);
    }

    func loadUsers() {
        UserService.shared.getAllUsers { (error: String?, users: [User]?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }

            return DispatchQueue.main.async {
                self.users = users!;
                self.filterUsers();
            }
        }
    }
}
