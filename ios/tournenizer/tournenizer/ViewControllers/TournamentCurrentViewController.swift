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
    var matchSwitch: UISegmentedControl!;
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

        titleLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.mediumHeader);
            view.textAlignment = .center;
            view.textColor = Constants.color.navy;
            view.text = tournament.name;
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            return view;
        }();

        matchSwitch = {
            let view = UISegmentedControl.newAutoLayout();
            view.insertSegment(withTitle: "In Progress/Future Matches", at: 0, animated: false);
            view.insertSegment(withTitle: "Completed Matches", at: 1, animated: false);
            view.tintColor = Constants.color.navy;
            view.selectedSegmentIndex = 0;
            return view;
        }();
        matchSwitch.addTarget(self, action: #selector(selectorChanged), for: .valueChanged);

        matchesPrompt = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            view.textAlignment = .center;
            view.textColor = Constants.color.navy;
            view.text = "Matches";
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

        if(tournament.status! == .FINISHED) {
            view.addSubview(matchesPrompt);
        } else {
            view.addSubview(matchSwitch);
        }

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

            if(tournament.status! == .FINISHED) {
                matchesPrompt.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: titlePadding);
                matchesPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
                matchesPrompt.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);
                matchListView.autoPinEdge(.top, to: .bottom, of: matchesPrompt, withOffset: titlePadding);
            } else {
                matchSwitch.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: titlePadding);
                matchSwitch.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
                matchSwitch.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);
                matchListView.autoPinEdge(.top, to: .bottom, of: matchSwitch, withOffset: titlePadding);
            }

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
        if(tournament.status! == .FINISHED || matchSwitch.selectedSegmentIndex == 1) {
            MatchService.shared.getCompletedMatches(tournament.id) { (error: String?, matches: [Match]?, teams: [[Team]]?, refs: [User]?) in
                if(error != nil) {
                    return DispatchQueue.main.async {
                        self.displayError(error!);
                    }
                }

                return DispatchQueue.main.async {
                    self.matchList.setData(matches: matches!, referees: refs!, teams: teams!);
                }
            }
        } else {
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
    }

    func selectMatch(match: Match, teams: [Team], ref: User) {
        if(ref.id == UserService.shared.getCurrentUser()!.id && match.matchStatus != .COMPLETED) {
            let vc = RefereeMatchViewController();
            vc.setData(match: match, team1: teams[0], team2: teams[1], ref: ref, tournament: tournament);
            self.navigationController?.pushViewController(vc, animated: true);
        } else {
            let vc = MatchViewController();
            vc.setData(match: match, team1: teams[0], team2: teams[1], ref: ref, tournament: tournament);
            self.navigationController?.pushViewController(vc, animated: true);
        }
    }

    @objc func selectorChanged() {
        loadMatches();
    }

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }
};
