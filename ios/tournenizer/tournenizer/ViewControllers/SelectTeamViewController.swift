//
//  SelectTeamViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/18/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;

class SelectTeamViewController : UIViewController {
    var selectLabel: UILabel!;
    var contentView: UIView!;
    var statusBarCover: UIView!;
    var backView: UIButton!;

    let selectText = "Select a team to join.";
    let dialogTitle = "Team Request";
    let dialogBody = "You have requested to join %@.";
    let dialogButtonText = "Ok";

    let actionsBarHeight: CGFloat = 50;
    let topTitlePadding: CGFloat = 20;
    let sideTitlePadding: CGFloat = 15;
    let buttonPadding: CGFloat = 10;
    let iconSize: CGFloat = 25;
    let contentPadding: CGFloat = 5;

    var tournament: Tournament!;

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.lightGray;

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            return view;
        }();

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

        selectLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.smallHeader);
            view.textColor = Constants.color.white;
            view.text = selectText;
            view.textAlignment = .center;
            return view;
        }();

        contentView = UIView.newAutoLayout();

        let teamList = TeamListViewController();
        TeamService.shared.getAllTeamsForTournament(id: tournament.id) { (error: String?, teams: [Team]?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }

            return DispatchQueue.main.async {
                teamList.setData(teams: teams!, tournaments: [Tournament](repeating: self.tournament, count: teams!.count));
            }
        }

        teamList.tableView.allowsSelection = true;
        teamList.setSelectCallback(select(_:));

        addChildViewController(teamList);
        contentView.addSubview(teamList.view);
        teamList.view.frame = contentView.bounds;
        teamList.didMove(toParentViewController: self);

        view.addSubview(statusBarCover);
        view.addSubview(backView);
        view.addSubview(selectLabel);
        view.addSubview(contentView);
        view.setNeedsUpdateConstraints();
    }

    func setTournament(_ tournament: Tournament) {
        self.tournament = tournament;
    }

    func select(_ team: Team) {
        TeamRequestService.shared.requestToJoinTeam(team) { (error: String?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }

            return DispatchQueue.main.async {
                let alert = UIAlertController(title: self.dialogTitle, message: String(format: self.dialogBody, team.name), preferredStyle: .alert);
                alert.addAction(UIAlertAction(title: self.dialogButtonText, style: .default, handler: { _ in
                    self.navigationController?.popViewController(animated: true);
                }));
                self.present(alert, animated: true, completion: nil);
            }
        }
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight + actionsBarHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            backView.autoSetDimension(.width, toSize: iconSize);
            backView.autoMatch(.height, to: .width, of: backView);
            backView.autoPinEdge(.bottom, to: .bottom, of: statusBarCover, withOffset: -buttonPadding);
            backView.autoPinEdge(toSuperviewEdge: .leading, withInset: buttonPadding);

            selectLabel.autoPinEdge(.leading, to: .trailing, of: backView, withOffset: sideTitlePadding);
            selectLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            selectLabel.autoPinEdge(.bottom, to: .bottom, of: statusBarCover, withOffset: -buttonPadding);

            contentView.autoPinEdge(.top, to: .bottom, of: statusBarCover, withOffset: contentPadding);
            contentView.autoPinEdge(toSuperviewEdge: .leading, withInset: contentPadding);
            contentView.autoPinEdge(toSuperviewEdge: .trailing, withInset: contentPadding);
            contentView.autoPinEdge(toSuperviewEdge: .bottom, withInset: contentPadding);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }
}
