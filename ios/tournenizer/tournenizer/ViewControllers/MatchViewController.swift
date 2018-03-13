//
//  MatchViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/13/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class MatchViewController : UIViewController {
    var match: Match!;
    var team1: Team!;
    var team2: Team!;
    var referee: User!;
    var tournament: Tournament!;

    var pollingTimer: Timer?;

    var statusBarCover: UIView!;
    var actionsBar: UIView!;
    var backView: UIButton!;
    var logoLabel: UILabel!;
    var titleLabel: UILabel!;
    var team1Label: UILabel!;
    var team2Label: UILabel!;
    var team1Score: UILabel!;
    var team2Score: UILabel!;
    var refereeLabel: UILabel!;

    let logoLabelHeight: CGFloat = 50;
    let iconSize: CGFloat = 25;
    let buttonWidth: CGFloat = 200;
    let buttonPadding: CGFloat = 10;
    let titlePadding: CGFloat = 10;
    let statisticsClusterOffset: CGFloat = 50;
    let sideTitlePadding: CGFloat = 15;

    let logoText = "Tournament Information";
    let matchesText = "Matches: ";

    func promptGenerator() -> UILabel {
        let view = UILabel.newAutoLayout();
        view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
        view.textColor = Constants.color.navy;
        view.lineBreakMode = .byWordWrapping;
        view.numberOfLines = 0;
        view.textAlignment = .right;
        return view;
    }

    func centerPromptGenerator() -> UILabel {
        let temp = promptGenerator();
        temp.textAlignment = .center;
        return temp;
    }

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
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.header);
            view.textAlignment = .center;
            view.textColor = Constants.color.navy;
            view.text = "Match #\(match.matchOrder + 1)";
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            return view;
        }();

        refereeLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.smallHeader);
            view.textAlignment = .center;
            view.textColor = Constants.color.navy;
            view.text = "Referee: \(referee.name)";
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            return view;
        }();

        team1Label = promptGenerator();
        team1Label.text = team1.name;

        team2Label = promptGenerator();
        team2Label.text = team2.name;
        
        team1Score = centerPromptGenerator();
        team1Score.text = 0.description;

        team2Score = centerPromptGenerator();
        team2Score.text = 0.description;

        view.addSubview(statusBarCover);
        view.addSubview(actionsBar);
        view.addSubview(backView);
        view.addSubview(logoLabel);
        view.addSubview(titleLabel);
        view.addSubview(team1Label);
        view.addSubview(team2Label);
        view.addSubview(team1Score);
        view.addSubview(team2Score);
        view.addSubview(refereeLabel);
        view.setNeedsUpdateConstraints();

        poll();
        startPolling();
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

            refereeLabel.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: titlePadding);
            refereeLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            refereeLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);

            team1Label.autoPinEdge(.top, to: .bottom, of: refereeLabel, withOffset: statisticsClusterOffset);
            team1Label.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            team1Label.autoMatch(.width, to: .width, of: view, withMultiplier: 0.35);

            team1Score.autoPinEdge(.leading, to: .trailing, of: team1Label);
            team1Score.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            team1Score.autoAlignAxis(.baseline, toSameAxisOf: team1Label);

            team2Label.autoPinEdge(.top, to: .bottom, of: team1Label, withOffset: statisticsClusterOffset);
            team2Label.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            team2Label.autoMatch(.width, to: .width, of: view, withMultiplier: 0.35);

            team2Score.autoPinEdge(.leading, to: .trailing, of: team2Label);
            team2Score.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            team2Score.autoAlignAxis(.baseline, toSameAxisOf: team2Label);
            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    func setData(match: Match, team1: Team, team2: Team, ref: User, tournament: Tournament) {
        self.match = match;
        self.team1 = team1;
        self.team2 = team2;
        self.referee = ref;
        self.tournament = tournament;
    }

    func startPolling() {
        pollingTimer = Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(poll), userInfo: nil, repeats: true);
    }

    @objc func poll() {
        MatchService.shared.getScore(tournamentId: tournament.id, matchId: match.id) { (error: String?, score1: Int?, score2: Int?) in
            if(error != nil) {
                return;
            }

            return DispatchQueue.main.async {
                self.team1Score.text = score1?.description;
                self.team2Score.text = score2?.description;
            }
        }
    }

    @objc func exit() {
        pollingTimer?.invalidate();
        self.navigationController?.popViewController(animated: true);
    }
};
