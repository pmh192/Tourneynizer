//
//  TournamentListViewContainerController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/15/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class TournamentListViewContainerController : UIViewController {
    var logoLabel: UILabel!;
    var statusBarCover: UIView!;
    var contentView: UIView!;
    let logoLabelHeight: CGFloat = 50;

    var tournamentList: TournamentListViewController!;

    override func loadView() {
        view = UIView();
        view.backgroundColor = UIColor.lightGray;

        logoLabel = {
            let view = UILabel.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            view.textColor = Constants.color.red;
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.mediumHeader);
            view.text = "Tourneynizer";
            view.textAlignment = .center;
            return view;
        }();

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            return view;
        }();

        contentView = UIView.newAutoLayout();

        tournamentList = TournamentListViewController();
        tournamentList.setSelectCallback(onSelect(_:));
        tournamentList.setReloadCallback {
            self.loadTournaments()
        }

        self.loadTournaments();

        addChildViewController(tournamentList);
        contentView.addSubview(tournamentList.view);
        tournamentList.view.frame = contentView.bounds;
        tournamentList.didMove(toParentViewController: self);

        view.addSubview(statusBarCover);
        view.addSubview(logoLabel);
        view.addSubview(contentView);
        
        view.setNeedsUpdateConstraints();
    }

    func onSelect(_ tournament: Tournament) {
        let vc = TournamentViewController();
        vc.setTournament(tournament);
        vc.setDashboard(tournament.creatorId == UserService.shared.getCurrentUser()!.id);
        TeamService.shared.getTeamForTournament(tournament.id) { (error: String?, team: Team?) in
            if(team != nil) {
                vc.setTeam(team!);
                vc.setRegistered(true);
            }

            DispatchQueue.main.async {
                self.navigationController?.pushViewController(vc, animated: true);
            }
        }
    }

    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            logoLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            logoLabel.autoSetDimension(.height, toSize: logoLabelHeight);
            logoLabel.autoPinEdge(toSuperviewEdge: .leading);
            logoLabel.autoPinEdge(toSuperviewEdge: .trailing);

            contentView.autoPinEdge(toSuperviewEdge: .leading);
            contentView.autoPinEdge(toSuperviewEdge: .trailing);
            contentView.autoPinEdge(.top, to: .bottom, of: logoLabel);
            contentView.autoPinEdge(toSuperviewEdge: .bottom);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    func loadTournaments() {
        TournamentService.shared.getAllTournaments { (error: String?, tournamentList: [Tournament]?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }

            return DispatchQueue.main.async {
                self.tournamentList.setTournaments(tournamentList!);
            }
        }
    }
}
