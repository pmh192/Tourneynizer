//
//  RefereeMatchViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/13/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class RefereeMatchViewController : UIViewController, UITextFieldDelegate {
    var match: Match!;
    var team1: Team!;
    var team2: Team!;
    var referee: User!;
    var tournament: Tournament!;

    var statusBarCover: UIView!;
    var actionsBar: UIView!;
    var backView: UIButton!;
    var logoLabel: UILabel!;
    var titleLabel: UILabel!;
    var team1Label: UILabel!;
    var team2Label: UILabel!;
    var refereeLabel: UILabel!;
    var updateTeam1Score: UITextField!;
    var updateTeam2Score: UITextField!;
    var startButton: UIButton!;
    var endButton: UIButton!;
    var updateButton: UIButton!;

    let logoLabelHeight: CGFloat = 50;
    let iconSize: CGFloat = 25;
    let buttonWidth: CGFloat = 200;
    let buttonPadding: CGFloat = 10;
    let titlePadding: CGFloat = 10;
    let statisticsClusterOffset: CGFloat = 50;
    let sideTitlePadding: CGFloat = 15;
    let signupButtonBorderRadius: CGFloat = 5;
    let signupButtonBorderWidth: CGFloat = 5;

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

    private func fieldGenerator() -> UITextField {
        let view = UITextField.newAutoLayout();
        view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
        view.textColor = Constants.color.navy;
        view.returnKeyType = .done;
        view.backgroundColor = Constants.color.lightGray;
        view.textAlignment = .center;
        return view;
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

        startButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle("Start", for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = signupButtonBorderRadius;
            view.layer.borderWidth = signupButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        startButton.addTarget(self, action: #selector(start), for: .touchUpInside);

        endButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle("End", for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = signupButtonBorderRadius;
            view.layer.borderWidth = signupButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        endButton.addTarget(self, action: #selector(end), for: .touchUpInside);

        updateButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle("Update", for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = signupButtonBorderRadius;
            view.layer.borderWidth = signupButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        updateButton.addTarget(self, action: #selector(update), for: .touchUpInside);

        team1Label = promptGenerator();
        team1Label.text = team1.name;

        team2Label = promptGenerator();
        team2Label.text = team2.name;

        updateTeam1Score = fieldGenerator();
        updateTeam1Score.keyboardType = .numberPad;
        updateTeam1Score.text = match.score1?.description;

        updateTeam2Score = fieldGenerator();
        updateTeam2Score.keyboardType = .numberPad;
        updateTeam2Score.text = match.score2?.description;

        if(match.matchStatus == .STARTED) {
            startButton.isEnabled = false;
            startButton.backgroundColor = Constants.color.gray;
            startButton.layer.borderColor = Constants.color.gray.cgColor;
        } else {
            updateButton.isEnabled = false;
            updateButton.backgroundColor = Constants.color.gray;
            updateButton.layer.borderColor = Constants.color.gray.cgColor;

            endButton.isEnabled = false;
            endButton.backgroundColor = Constants.color.gray;
            endButton.layer.borderColor = Constants.color.gray.cgColor;

            updateTeam1Score.isEnabled = false;
            updateTeam2Score.isEnabled = false;
        }

        view.addSubview(statusBarCover);
        view.addSubview(actionsBar);
        view.addSubview(backView);
        view.addSubview(logoLabel);
        view.addSubview(titleLabel);
        view.addSubview(team1Label);
        view.addSubview(team2Label);
        view.addSubview(updateTeam1Score);
        view.addSubview(updateTeam2Score);
        view.addSubview(refereeLabel);
        view.addSubview(startButton);
        view.addSubview(endButton);
        view.addSubview(updateButton);
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

            refereeLabel.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: titlePadding);
            refereeLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            refereeLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);

            team1Label.autoPinEdge(.top, to: .bottom, of: refereeLabel, withOffset: statisticsClusterOffset);
            team1Label.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            team1Label.autoMatch(.width, to: .width, of: view, withMultiplier: 0.35);

            updateTeam1Score.autoPinEdge(.leading, to: .trailing, of: team1Label, withOffset: sideTitlePadding);
            updateTeam1Score.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            updateTeam1Score.autoAlignAxis(.baseline, toSameAxisOf: team1Label);

            team2Label.autoPinEdge(.top, to: .bottom, of: team1Label, withOffset: statisticsClusterOffset);
            team2Label.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            team2Label.autoMatch(.width, to: .width, of: view, withMultiplier: 0.35);

            updateTeam2Score.autoPinEdge(.leading, to: .trailing, of: team2Label, withOffset: sideTitlePadding);
            updateTeam2Score.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            updateTeam2Score.autoAlignAxis(.baseline, toSameAxisOf: team2Label);

            let views: NSArray = [startButton, endButton] as NSArray;
            views.autoDistributeViews(along: .horizontal, alignedTo: .horizontal, withFixedSpacing: titlePadding, insetSpacing: true, matchedSizes: true);
            startButton.autoPinEdge(toSuperviewEdge: .bottom, withInset: statisticsClusterOffset);

            updateButton.autoPinEdge(.top, to: .bottom, of: team2Label, withOffset: statisticsClusterOffset);
            updateButton.autoMatch(.width, to: .width, of: startButton);
            updateButton.autoAlignAxis(.vertical, toSameAxisOf: view);

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

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }

    @objc func start() {
        MatchService.shared.start(tournamentId: tournament.id, matchId: match.id) { (error: String?) in
            if(error != nil) {
                return;
            }

            DispatchQueue.main.async {
                self.match.matchStatus = .STARTED;
                self.startButton.isEnabled = false;
                self.startButton.backgroundColor = Constants.color.gray;
                self.startButton.layer.borderColor = Constants.color.gray.cgColor;

                self.updateButton.isEnabled = true;
                self.updateButton.backgroundColor = Constants.color.lightBlue;
                self.updateButton.layer.borderColor = Constants.color.lightBlue.cgColor;

                self.endButton.isEnabled = true;
                self.endButton.backgroundColor = Constants.color.lightBlue;
                self.endButton.layer.borderColor = Constants.color.lightBlue.cgColor;

                self.updateTeam1Score.isEnabled = true;
                self.updateTeam2Score.isEnabled = true;
            }
        }
    }

    @objc func end() {
        let one: Int = {
            guard let value = Int(updateTeam1Score.text!) else {
                return 0;
            }
            return value;
        }();

        let two: Int = {
            guard let value = Int(updateTeam2Score.text!) else {
                return 0;
            }
            return value;
        }();

        let score = UpdateScore(score1: one, score2: two);

        MatchService.shared.end(tournamentId: tournament.id, matchId: match.id, score: score) { (error: String?) in
            if(error != nil) {
                return;
            }

            DispatchQueue.main.async {
                let vcSize = self.navigationController!.viewControllers.count;
                var viewControllers = Array(self.navigationController!.viewControllers[0...vcSize-2]);
                let vc = MatchViewController();
                vc.setData(match: self.match, team1: self.team1, team2: self.team2, ref: self.referee, tournament: self.tournament);
                viewControllers.append(vc);
                self.navigationController?.setViewControllers(viewControllers, animated: false);
            }
        }
    }

    @objc func update() {
        let one: Int = {
            guard let value = Int(updateTeam1Score.text!) else {
                return 0;
            }
            return value;
        }();

        let two: Int = {
            guard let value = Int(updateTeam2Score.text!) else {
                return 0;
            }
            return value;
        }();

        let score = UpdateScore(score1: one, score2: two);

        MatchService.shared.updateScore(tournamentId: tournament.id, matchId: match.id, score: score) { (error: String?) in
            if(error != nil) {
                return;
            }
        }
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder();
        return false;
    }
};
