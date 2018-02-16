//
//  TournamentViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/9/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;
import GoogleMaps;

class TournamentViewController : UIViewController {
    var backView: UIButton!;
    var logoLabel: UILabel!;
    var statusBarCover: UIView!;

    var titleLabel: UILabel!;
    var locationLabel: UILabel!;
    var descriptionLabel: UILabel!;
    var startTimeLabel: UILabel!;
    var teamsLabel: UILabel!;
    var maxTeamsLabel: UILabel!;
    var mapView: GMSMapView!;

    let iconSize: CGFloat = 25;
    let logoLabelHeight: CGFloat = 50;
    let iconPadding: CGFloat = 15;
    
    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.lightGray;

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

        backView = {
            let view = UIButton();
            let image = UIImage(named: "arrowright")?.withRenderingMode(.alwaysTemplate);
            view.setImage(image, for: UIControlState.normal);
            view.imageView?.transform = CGAffineTransform(scaleX: -1, y: 1);
            view.imageView?.tintColor = Constants.color.white;
            view.contentMode = .scaleAspectFit;
            return view;
        }();

        backView.addTarget(self, action: #selector(exit), for: .touchUpInside);

        view.addSubview(statusBarCover);
        view.addSubview(logoLabel);
        view.addSubview(backView);
        view.setNeedsUpdateConstraints();
    }

    func setTournament(tourament: Tournament) {

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

            logoLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            logoLabel.autoSetDimension(.height, toSize: logoLabelHeight);
            logoLabel.autoPinEdge(toSuperviewEdge: .leading);
            logoLabel.autoPinEdge(toSuperviewEdge: .trailing);

            backView.autoSetDimension(.width, toSize: iconSize);
            backView.autoMatch(.height, to: .width, of: backView);
            backView.autoAlignAxis(.horizontal, toSameAxisOf: logoLabel);
            backView.autoPinEdge(toSuperviewEdge: .left, withInset: iconPadding);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }
}

