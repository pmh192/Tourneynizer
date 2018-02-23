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
import Foundation;

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
    var mapViewContainer: UIView!;
    var mapView: GMSMapView!;
    var joinButton: UIButton!;
    var createTeamButton: UIButton!;
    var editButton: UIButton!;

    var actionsBar: UIView!;

    let iconSize: CGFloat = 25;
    let logoLabelHeight: CGFloat = 50;
    let iconPadding: CGFloat = 15;
    let titlePadding: CGFloat = 10;
    let padding: CGFloat = 5;
    let signupButtonBorderRadius: CGFloat = 5;
    let signupButtonBorderWidth: CGFloat = 5;
    let signupSidePadding: CGFloat = 30;
    let mapPadding: CGFloat = 10;
    let buttonWidth: CGFloat = 200;
    let buttonPadding: CGFloat = 10;
    var tournament: Tournament = Tournament();

    var joinable = true;

    func setJoinable(_ join: Bool) {
        self.joinable = join;
    }
    
    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;

        actionsBar = UIView.newAutoLayout();
        actionsBar.backgroundColor = Constants.color.navy;

        logoLabel = {
            let view = UILabel.newAutoLayout();
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
            let view = UIButton.newAutoLayout();
            let image = UIImage(named: "arrowright")?.withRenderingMode(.alwaysTemplate);
            view.setImage(image, for: UIControlState.normal);
            view.imageView?.transform = CGAffineTransform(scaleX: -1, y: 1);
            view.imageView?.tintColor = Constants.color.white;
            view.contentMode = .scaleAspectFit;
            return view;
        }();
        backView.addTarget(self, action: #selector(exit), for: .touchUpInside);

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

        joinButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle("Join Existing Team", for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = signupButtonBorderRadius;
            view.layer.borderWidth = signupButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        joinButton.addTarget(self, action: #selector(join), for: .touchUpInside);

        createTeamButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle("Create Team", for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = signupButtonBorderRadius;
            view.layer.borderWidth = signupButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        createTeamButton.addTarget(self, action: #selector(create), for: .touchUpInside);

        editButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle("Edit Tournament", for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = signupButtonBorderRadius;
            view.layer.borderWidth = signupButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        editButton.addTarget(self, action: #selector(edit), for: .touchUpInside);

        locationLabel = UILabel.newAutoLayout();
        descriptionLabel = UILabel.newAutoLayout();
        startTimeLabel = UILabel.newAutoLayout();
        teamsLabel = UILabel.newAutoLayout();
        maxTeamsLabel = UILabel.newAutoLayout();

        locationLabel.text = tournament.address;
        descriptionLabel.text = tournament.description;

        let formatter = DateFormatter();
        formatter.dateFormat = "MM/dd/yyyy HH:mm";

        startTimeLabel.text = "Start Time: " + formatter.string(from: tournament.startTime);

        if(tournament.currentTeams != nil) {
            teamsLabel.text = "Teams: " + tournament.currentTeams!.description;
        }

        maxTeamsLabel.text = "Max Teams: " + tournament.maxTeams.description;

        let common: [UILabel] = [
            locationLabel,
            descriptionLabel,
            startTimeLabel,
            teamsLabel,
            maxTeamsLabel
        ];

        for label in common {
            label.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            label.textColor = Constants.color.navy;
            label.textAlignment = .left;
        }

        mapViewContainer = UIView.newAutoLayout();

        view.addSubview(statusBarCover);
        view.addSubview(actionsBar);
        view.addSubview(backView);
        view.addSubview(titleLabel);
        view.addSubview(locationLabel);
        view.addSubview(descriptionLabel);
        view.addSubview(startTimeLabel);
        view.addSubview(teamsLabel);
        view.addSubview(maxTeamsLabel);
        view.addSubview(mapViewContainer);

        if(joinable) {
            view.addSubview(joinButton);
            view.addSubview(createTeamButton);
            view.addSubview(logoLabel);
        } else {
            view.addSubview(editButton);
        }

        view.setNeedsUpdateConstraints();
    }


    func setTournament(_ tournament: Tournament) {
        self.tournament = tournament;
    }

    override func viewDidLayoutSubviews() {
        if(mapView == nil) {
            let camera = GMSCameraPosition.camera(withLatitude: 34.414593, longitude: -119.854979, zoom: 18.0);
            mapView = GMSMapView.map(withFrame: mapViewContainer.bounds, camera: camera);
            let marker = GMSMarker();
            marker.position = CLLocationCoordinate2D(latitude: 34.414593, longitude: -119.854979);
            marker.map = mapView;
            marker.title = "Sydney";
            marker.snippet = "Australia";
            mapViewContainer.addSubview(mapView);
        }
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

            titleLabel.autoPinEdge(.top, to: .bottom, of: actionsBar, withOffset: titlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);

            locationLabel.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: padding);
            locationLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: padding);
            locationLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: padding);

            descriptionLabel.autoPinEdge(.top, to: .bottom, of: locationLabel, withOffset: padding);
            descriptionLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: padding);
            descriptionLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: padding);

            startTimeLabel.autoPinEdge(.top, to: .bottom, of: descriptionLabel, withOffset: padding);
            startTimeLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: padding);
            startTimeLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: padding);

            teamsLabel.autoPinEdge(.top, to: .bottom, of: startTimeLabel, withOffset: padding);
            teamsLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: padding);
            teamsLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: padding);

            maxTeamsLabel.autoPinEdge(.top, to: .bottom, of: teamsLabel, withOffset: padding);
            maxTeamsLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: padding);
            maxTeamsLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: padding);

            mapViewContainer.autoPinEdge(.top, to: .bottom, of: maxTeamsLabel, withOffset: mapPadding);
            mapViewContainer.autoPinEdge(toSuperviewEdge: .leading, withInset: mapPadding);
            mapViewContainer.autoPinEdge(toSuperviewEdge: .trailing, withInset: mapPadding);

            if(joinable) {
                let views: NSArray = [joinButton, createTeamButton] as NSArray;
                views.autoDistributeViews(along: .horizontal, alignedTo: .horizontal, withFixedSpacing: 5.0, insetSpacing: true, matchedSizes: true);
                joinButton.autoPinEdge(toSuperviewEdge: .bottom, withInset: padding);
                mapViewContainer.autoPinEdge(.bottom, to: .top, of: joinButton, withOffset: -mapPadding);

                logoLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);
                logoLabel.autoSetDimension(.height, toSize: logoLabelHeight);
                logoLabel.autoPinEdge(toSuperviewEdge: .leading);
                logoLabel.autoPinEdge(toSuperviewEdge: .trailing);
            } else {
                mapViewContainer.autoPinEdge(toSuperviewEdge: .bottom, withInset: mapPadding);
                editButton.autoPinEdge(toSuperviewEdge: .trailing, withInset: buttonPadding);
                editButton.autoPinEdge(.bottom, to: .bottom, of: actionsBar, withOffset: -buttonPadding);
                editButton.autoSetDimension(.width, toSize: buttonWidth);
            }

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    @objc func join() {
        self.navigationController?.pushViewController(SelectTeamViewController(), animated: true);
    }

    @objc func create() {
        self.navigationController?.pushViewController(CreateTeamViewController(), animated: true);
    }

    @objc func edit() {
        let vc = EditTournamentViewController();
        vc.setTournament(self.tournament);
        vc.setCallback(cb: { (t: Tournament) -> Void in
            self.tournament = t;
        });
        self.navigationController?.pushViewController(vc, animated: true);
    }

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }
}

