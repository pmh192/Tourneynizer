//
//  TournamentCurrentViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/12/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class TournamentCurrentViewController : UIViewController {
    var tournament: Tournament!;
    let matchList = MatchListViewController();

    var statusBarCover: UIView!;
    var actionsBar: UIView!;
    var backView: UIButton!;
    var logoLabel: UILabel!;
    var titleLabel: UILabel!;
    var matchListView: UIView!;
    var matchesPrompt: UILabel!;

    let logoLabelHeight: CGFloat = 50;
    let iconSize: CGFloat = 25;
    let buttonWidth: CGFloat = 200;
    let buttonPadding: CGFloat = 10;
    let titlePadding: CGFloat = 10;

    let logoText = "Tournament Information";
    let matchesText = "Matches: ";

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            return view;
        }();

        actionsBar = UIView.newAutoLayout();
        actionsBar.backgroundColor = Constants.color.navy;

        backView = {
            let view = UIButton.newAutoLayout();
            let image = UIImage(named: "arrowright")?.withRenderingMode(.alwaysTemplate);
            view.setImage(image, for: UIControlState.normal);
            view.imageView?.transform = CGAffineTransform(scaleX: -1, y: 1);
            view.imageView?.tintColor = Constants.color.white;
            view.contentMode = .scaleAspectFit;
            return view;
        }();
        backView.addTarget(self, action: #selector(exit), for: .touchUpInside);

        logoLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.smallHeader);
            view.textColor = Constants.color.white;
            view.text = logoText;
            view.textAlignment = .center;
            return view;
        }();

        matchesPrompt = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            view.textColor = Constants.color.navy;
            view.text = matchesText;
            return view;
        }();

        titleLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.smallHeader);
            view.textAlignment = .center;
            view.textColor = Constants.color.navy;
            view.text = tournament.name;
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            return view;
        }();

        matchListView = UIView.newAutoLayout();
        matchList.setReloadCallback {
            self.loadMatches();
        }
        self.loadMatches();
        matchList.setSelectCallback(selectMatch(match:teams:ref:));

        addChildViewController(matchList);
        matchListView.addSubview(matchList.view);
        matchList.view.frame = matchListView.bounds;
        matchList.didMove(toParentViewController: self);

        view.addSubview(statusBarCover);
        view.addSubview(actionsBar);
        view.addSubview(backView);
        view.addSubview(logoLabel);
        view.addSubview(titleLabel);
        view.addSubview(matchListView);
        view.addSubview(matchesPrompt);
        view.setNeedsUpdateConstraints();
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            actionsBar.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            actionsBar.autoSetDimension(.height, toSize: logoLabelHeight);
            actionsBar.autoPinEdge(toSuperviewEdge: .leading);
            actionsBar.autoPinEdge(toSuperviewEdge: .trailing);

            backView.autoSetDimension(.width, toSize: iconSize);
            backView.autoMatch(.height, to: .width, of: backView);
            backView.autoPinEdge(.bottom, to: .bottom, of: actionsBar, withOffset: -buttonPadding);
            backView.autoPinEdge(toSuperviewEdge: .leading, withInset: buttonPadding);

            logoLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            logoLabel.autoSetDimension(.height, toSize: logoLabelHeight);
            logoLabel.autoPinEdge(toSuperviewEdge: .leading);
            logoLabel.autoPinEdge(toSuperviewEdge: .trailing);

            titleLabel.autoPinEdge(.top, to: .bottom, of: actionsBar, withOffset: titlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);

            matchesPrompt.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: titlePadding);
            matchesPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            matchesPrompt.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);

            matchListView.autoPinEdge(.top, to: .bottom, of: matchesPrompt, withOffset: titlePadding);
            matchListView.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            matchListView.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);
            matchListView.autoPinEdge(toSuperviewEdge: .bottom, withInset: titlePadding);
            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    func setTournament(tournament: Tournament!) {
        self.tournament = tournament;
    }

    func loadMatches() {
        MatchService.shared.getValidMatches(tournament.id) { (error: String?, matches: [Match]?, teams: [[Team]]?, refs: [User]?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }

            return DispatchQueue.main.async {
                self.matchList.setData(matches: matches!, referees: refs!, teams: teams!);
            }
        }
    }

    func selectMatch(match: Match, teams: [Team], ref: User) {
        
    }

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }
};
