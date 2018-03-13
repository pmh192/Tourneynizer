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

    var statusBarCover: UIView!;
    var actionsBar: UIView!;
    var backView: UIButton!;

    let logoLabelHeight: CGFloat = 50;
    let iconSize: CGFloat = 25;
    let buttonWidth: CGFloat = 200;
    let buttonPadding: CGFloat = 10;

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.lightGray;

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

        view.addSubview(statusBarCover);
        view.addSubview(actionsBar);
        view.addSubview(backView);
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
            
            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    func setTournament(tournament: Tournament!) {
        self.tournament = tournament;
    }

    func loadMatches() {
        MatchService.shared.getValidMatches(tournament.id) { (error: String?, matches: [Match]?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }


        };
    }

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }
};
