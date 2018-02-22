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

    let bottomBarHeight: CGFloat = 50;

    var currentTab = 0;
    var tabBarControllers: [UIViewController]!;
    var tabBarButtons: [UIButton]!;

    var imagePercentage: CGFloat = 0.5;

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;

        bottomBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
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
            view.imageView?.tintColor = Constants.color.red;
            view.tag = 0;
            return view;
        }();

        searchButton = {
            let view = UIButton.newAutoLayout();
            view.setImage(searchImage, for: .normal);
            view.setImage(searchImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = Constants.color.white;
            view.tag = 1;
            return view;
        }();

        addButton = {
            let view = UIButton.newAutoLayout();
            view.setImage(addImage, for: .normal);
            view.setImage(addImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = Constants.color.white;
            view.tag = 2;
            return view;
        }();

        eventsButton = {
            let view = UIButton.newAutoLayout();
            view.setImage(eventsImage, for: .normal);
            view.setImage(eventsImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = Constants.color.white;
            view.tag = 3;
            return view;
        }();

        profileButton = {
            let view = UIButton.newAutoLayout();
            view.setImage(profileImage, for: .normal);
            view.setImage(profileImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = Constants.color.white;
            view.tag = 4;
            return view;
        }();

        buttonContainer = { [tournamentsButton, searchButton, addButton, eventsButton, profileButton] in
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            view.addSubview(tournamentsButton!);
            view.addSubview(searchButton!);
            view.addSubview(addButton!);
            view.addSubview(eventsButton!);
            view.addSubview(profileButton!);
            return view;
        }();

        tabBarControllers = [
            TournamentListViewContainerController(),
            PlayersViewController(),
            CreateTournamentViewController(),
            UserActionsViewController(),
            ProfileViewController()
        ].map {vc -> UIViewController in
            let viewController = UINavigationController();
            viewController.pushViewController(vc, animated: false);
            viewController.isNavigationBarHidden = true;
            return viewController;
        };

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
            
            bottomBarCover.autoPin(toBottomLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            bottomBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight);
            bottomBarCover.autoPinEdge(toSuperviewEdge: .left);
            bottomBarCover.autoPinEdge(toSuperviewEdge: .right);

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
            contentView.autoPinEdge(toSuperviewEdge: .top);
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
        tabBarButtons[currentTab].imageView?.tintColor = Constants.color.white;
        tabBarControllers[currentTab].willMove(toParentViewController: nil);
        tabBarControllers[currentTab].view.removeFromSuperview();
        tabBarControllers[currentTab].removeFromParentViewController();

        //set currentTab
        currentTab = sender.tag;
        tabBarButtons[currentTab].imageView?.tintColor = Constants.color.red;
        addChildViewController(tabBarControllers[currentTab]);
        tabBarControllers[currentTab].view.frame = contentView.bounds;
        contentView.addSubview(tabBarControllers[currentTab].view);
        tabBarControllers[currentTab].didMove(toParentViewController: self);
    }
};
