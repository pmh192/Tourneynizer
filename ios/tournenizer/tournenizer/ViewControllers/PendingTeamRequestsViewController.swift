//
//  PendingTeamRequestsViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/10/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class PendingTeamRequestsViewController : UIViewController {
    let teamRequestView = TeamRequestListViewController();
    var team: Team!;

    var contentView: UIView!;
    var backView: UIButton!;
    var selectLabel: UILabel!;
    var topBackBackground: UIView!;

    let actionsBarHeight: CGFloat = 35;
    let topTitlePadding: CGFloat = 20;
    let sideTitlePadding: CGFloat = 15;
    let buttonPadding: CGFloat = 10;
    let iconSize: CGFloat = 25;
    let selectText = "User Team Requests";

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;

        topBackBackground = {
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

        teamRequestView.setReloadCallback {
            self.loadTeamRequests();
        }
        teamRequestView.setAcceptCallback(acceptRequest(_:index:));
        teamRequestView.setRejectCallback(rejectRequest(_:index:));

        addChildViewController(teamRequestView);
        teamRequestView.view.frame = contentView.bounds;
        contentView.addSubview(teamRequestView.view);
        teamRequestView.didMove(toParentViewController: self);

        loadTeamRequests();

        view.addSubview(topBackBackground);
        view.addSubview(selectLabel);
        view.addSubview(backView);
        view.addSubview(contentView);
        view.setNeedsUpdateConstraints();
    }

    func acceptRequest(_ teamRequest: TeamRequest, index: Int) {
        TeamRequestService.shared.acceptRequest(teamRequest) { (error: String?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }

            return DispatchQueue.main.async {
                self.teamRequestView.removeElementAtIndex(index);
            }
        }
    }

    func rejectRequest(_ teamRequest: TeamRequest, index: Int) {
        TeamRequestService.shared.rejectRequest(teamRequest) { (error: String?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }

            return DispatchQueue.main.async {
                self.teamRequestView.removeElementAtIndex(index);
            }
        }
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            topBackBackground.autoPinEdge(toSuperviewEdge: .leading);
            topBackBackground.autoPinEdge(toSuperviewEdge: .trailing);
            topBackBackground.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            topBackBackground.autoSetDimension(.height, toSize: actionsBarHeight);

            backView.autoSetDimension(.width, toSize: iconSize);
            backView.autoMatch(.height, to: .width, of: backView);
            backView.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            backView.autoPinEdge(toSuperviewEdge: .leading, withInset: buttonPadding);

            selectLabel.autoPinEdge(.leading, to: .trailing, of: backView, withOffset: sideTitlePadding);
            selectLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            selectLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);

            contentView.autoPinEdge(.top, to: .bottom, of: topBackBackground);
            contentView.autoPinEdge(toSuperviewEdge: .trailing);
            contentView.autoPinEdge(toSuperviewEdge: .leading);
            contentView.autoPinEdge(toSuperviewEdge: .bottom);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    func loadTeamRequests() {
        TeamRequestService.shared.getRequestsForTeam(team.id) { (error: String?, teamRequests: [TeamRequest]?, tournaments: [Tournament]?, users: [User]?, teams: [Team]?) in
            if(error != nil) {
                return;
            }

            DispatchQueue.main.async {
                self.teamRequestView.setData(teamRequests: teamRequests!, tournaments: tournaments!, requesters: users!, teams: teams!);
            }
        }
    }

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }

    func setTeam(_ team: Team) {
        self.team = team;
    }
}

