//
//  DashboardViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/4/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class DashboardViewController : UIViewController {
    var logoLabel: UILabel!;
    var statusBarCover: UIView!;
    var bottomBarCover: UIView!;
    var buttonContainer: UIView!;
    var contentView: UIView!;

    var tournamentsButton: UIButton!;
    var searchButton: UIButton!;
    var addButton: UIButton!;
    var eventsButton: UIButton!;
    var profileButton: UIButton!;

    var tournamentImage: UIImage!;
    var searchImage: UIImage!;
    var addImage: UIImage!;
    var eventsImage: UIImage!;
    var profileImage: UIImage!;

    let logoLabelHeight: CGFloat = 45;
    let bottomBarHeight: CGFloat = 44;

    var currentTab = 0;
    var tabBarControllers: [UIViewController]!;
    var tabBarButtons: [UIButton]!;

    var imagePercentage: CGFloat = 0.6;

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.white;

        logoLabel = {
            let view = UILabel.newAutoLayout();
            view.backgroundColor = Constants.navy;
            view.textColor = Constants.red;
            view.font = UIFont(name: Constants.fontMedium, size: Constants.topHeaderFontSize);
            view.text = "Tourneynizer";
            view.textAlignment = .center;
            return view;
        }();

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.navy;
            return view;
        }();

        bottomBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.navy;
            return view;
        }();

        contentView = UIView.newAutoLayout();

        tournamentImage = UIImage(named: "tournament")?.withRenderingMode(.alwaysTemplate);
        searchImage = UIImage(named: "search")?.withRenderingMode(.alwaysTemplate);
        profileImage = UIImage(named: "profile")?.withRenderingMode(.alwaysTemplate);
        eventsImage = UIImage(named: "events")?.withRenderingMode(.alwaysTemplate);
        addImage = UIImage(named: "add")?.withRenderingMode(.alwaysTemplate);

        tournamentsButton = {
            let view = UIButton.newAutoLayout();
            view.setImage(tournamentImage, for: .normal);
            view.setImage(tournamentImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = Constants.red;
            view.tag = 0;
            return view;
        }();

        searchButton = {
            let view = UIButton.newAutoLayout();
            view.setImage(searchImage, for: .normal);
            view.setImage(searchImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = Constants.white;
            view.tag = 1;
            return view;
        }();

        addButton = {
            let view = UIButton.newAutoLayout();
            view.setImage(addImage, for: .normal);
            view.setImage(addImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = Constants.white;
            view.tag = 2;
            return view;
        }();

        eventsButton = {
            let view = UIButton.newAutoLayout();
            view.setImage(eventsImage, for: .normal);
            view.setImage(eventsImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = Constants.white;
            view.tag = 3;
            return view;
        }();

        profileButton = {
            let view = UIButton.newAutoLayout();
            view.setImage(profileImage, for: .normal);
            view.setImage(profileImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = Constants.white;
            view.tag = 4;
            return view;
        }();

        buttonContainer = { [tournamentsButton, searchButton, addButton, eventsButton, profileButton] in
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.navy;
            view.addSubview(tournamentsButton!);
            view.addSubview(searchButton!);
            view.addSubview(addButton!);
            view.addSubview(eventsButton!);
            view.addSubview(profileButton!);
            return view;
        }();

        tabBarControllers = [
            TournamentListViewController(),
            PlayerListViewController(),
            CreateTournamentViewController(),
            EventsViewController(),
            ProfileViewController()
        ];

        tabBarButtons = [
            tournamentsButton,
            searchButton,
            addButton,
            eventsButton,
            profileButton
        ];

        for el in tabBarButtons {
            el.addTarget(self, action: #selector(tabButtonClicked(sender:)), for: .touchUpInside);
        }

        view.addSubview(statusBarCover);
        view.addSubview(logoLabel);
        view.addSubview(contentView);
        view.addSubview(buttonContainer);
        view.addSubview(bottomBarCover);
        view.setNeedsUpdateConstraints();

        if #available(iOS 11.0, *) {
             self.setNeedsUpdateOfHomeIndicatorAutoHidden();
        }

        self.view = view;
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        tabButtonClicked(sender: tournamentsButton);
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            didUpdateConstraints = true;

            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            bottomBarCover.autoPin(toBottomLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            bottomBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight);
            bottomBarCover.autoPinEdge(toSuperviewEdge: .left);
            bottomBarCover.autoPinEdge(toSuperviewEdge: .right);

            logoLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            logoLabel.autoSetDimension(.height, toSize: logoLabelHeight);
            logoLabel.autoPinEdge(toSuperviewEdge: .leading);
            logoLabel.autoPinEdge(toSuperviewEdge: .trailing);

            buttonContainer.autoPin(toBottomLayoutGuideOf: self, withInset: 0);
            buttonContainer.autoSetDimension(.height, toSize: bottomBarHeight);
            buttonContainer.autoPinEdge(toSuperviewEdge: .leading);
            buttonContainer.autoPinEdge(toSuperviewEdge: .trailing);

            let views: NSArray = tabBarButtons as NSArray;
            views.autoSetViewsDimension(.height, toSize: bottomBarHeight);
            views.autoDistributeViews(along: .horizontal, alignedTo: .horizontal, withFixedSpacing: 10.0, insetSpacing: true, matchedSizes: true);

            tournamentsButton.autoAlignAxis(toSuperviewAxis: .horizontal);
            tournamentsButton.imageView?.autoSetDimension(.width, toSize: imagePercentage * bottomBarHeight);
            searchButton.imageView?.autoSetDimension(.width, toSize: imagePercentage * bottomBarHeight);
            addButton.imageView?.autoSetDimension(.width, toSize: imagePercentage * bottomBarHeight);
            eventsButton.imageView?.autoSetDimension(.width, toSize: imagePercentage * bottomBarHeight);
            profileButton.imageView?.autoSetDimension(.width, toSize: imagePercentage * bottomBarHeight);

            contentView.autoPinEdge(toSuperviewEdge: .leading);
            contentView.autoPinEdge(.top, to: .bottom, of: logoLabel);
            contentView.autoPinEdge(toSuperviewEdge: .trailing);
            contentView.autoPinEdge(.bottom, to: .top, of: buttonContainer);
        }

        super.updateViewConstraints();
    }

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent;
    }

    override func prefersHomeIndicatorAutoHidden() -> Bool {
        return true;
    }

    @objc func tabButtonClicked(sender: UIButton) {
        //undo currentTab
        tabBarButtons[currentTab].imageView?.tintColor = Constants.white;
        tabBarControllers[currentTab].willMove(toParentViewController: nil);
        tabBarControllers[currentTab].view.removeFromSuperview();
        tabBarControllers[currentTab].removeFromParentViewController();

        //set currentTab
        currentTab = sender.tag;
        tabBarButtons[currentTab].imageView?.tintColor = Constants.red;
        addChildViewController(tabBarControllers[currentTab]);
        tabBarControllers[currentTab].view.frame = contentView.bounds;
        contentView.addSubview(tabBarControllers[currentTab].view);
        tabBarControllers[currentTab].didMove(toParentViewController: self);
    }
};
