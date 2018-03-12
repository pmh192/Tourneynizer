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
    var teamSizeLabel: UILabel!;
    var descriptionLabel: UILabel!;
    var startTimeLabel: UILabel!;
    var teamsLabel: UILabel!;
    var maxTeamsLabel: UILabel!;
    var mapViewContainer: UIView!;
    var mapView: GMSMapView!;
    var joinButton: UIButton!;
    var createTeamButton: UIButton!;
    var teamViewContainer: UIView!;
    var actionsBar: UIView!;
    var startButton: UIButton!;
    var approveButton: UIButton!;
    var teamViewController: TeamListViewController!;

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
    let teamContainerHeight: CGFloat = 68;
    let logoText = "Tournament Information";
    let joinButtonText = "Join Existing Team";
    let createButtonText = "Create Team";
    let startButtonText = "Start Tournament";
    let approveButtonText = "Approve Teams";

    var tournament: Tournament!;
    var team: Team!;
    var registered = false;
    var dashboard = false;

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;

        actionsBar = UIView.newAutoLayout();
        actionsBar.backgroundColor = Constants.color.navy;

        logoLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.smallHeader);
            view.textColor = Constants.color.white;
            view.text = logoText;
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
            view.setTitle(joinButtonText, for: .normal);
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
            view.setTitle(createButtonText, for: .normal);
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

        startButton = {
                let view = UIButton.newAutoLayout();
                view.setTitle(startButtonText, for: .normal);
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

        approveButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(approveButtonText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = signupButtonBorderRadius;
            view.layer.borderWidth = signupButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        approveButton.addTarget(self, action: #selector(approve), for: .touchUpInside);

        teamSizeLabel = UILabel.newAutoLayout();
        descriptionLabel = UILabel.newAutoLayout();
        startTimeLabel = UILabel.newAutoLayout();
        teamsLabel = UILabel.newAutoLayout();
        maxTeamsLabel = UILabel.newAutoLayout();

        teamSizeLabel.text = "Team Size: \(tournament.teamSize)";
        descriptionLabel.text = tournament.description;

        let formatter = DateFormatter();
        formatter.dateFormat = "MM/dd/yyyy HH:mm";

        startTimeLabel.text = "Start Time: \(formatter.string(from: tournament.startTime))";

        if(tournament.currentTeams != nil) {
            teamsLabel.text = "Teams: \(tournament.currentTeams!.description)";
        }

        maxTeamsLabel.text = "Max Teams: \(tournament.maxTeams.description)";

        let common: [UILabel] = [
            teamSizeLabel,
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

        if(registered) {
            teamViewController = TeamListViewController();
            teamViewController.setData(teams: [team], tournaments: [tournament]);
            teamViewContainer = UIView.newAutoLayout();
            teamViewController.tableView.isScrollEnabled = false;
        }

        view.addSubview(statusBarCover);
        view.addSubview(actionsBar);
        view.addSubview(backView);
        view.addSubview(titleLabel);
        view.addSubview(teamSizeLabel);
        view.addSubview(descriptionLabel);
        view.addSubview(startTimeLabel);
        view.addSubview(teamsLabel);
        view.addSubview(maxTeamsLabel);
        view.addSubview(mapViewContainer);
        view.addSubview(logoLabel);

        if(dashboard) {
            view.addSubview(startButton);
            //view.addSubview(approveButton);
        }

        if(!registered) {
            view.addSubview(joinButton);
            view.addSubview(createTeamButton);
        } else {
            view.addSubview(teamViewContainer);

            addChildViewController(teamViewController);
            teamViewController.view.frame = teamViewContainer.bounds;
            teamViewContainer.addSubview(teamViewController.view);
            teamViewController.didMove(toParentViewController: self);
        }

        view.setNeedsUpdateConstraints();
    }

    override func viewDidLayoutSubviews() {
        if(mapView == nil) {
            let camera = GMSCameraPosition.camera(withLatitude: tournament.lat, longitude: tournament.lng, zoom: 18.0);
            mapView = GMSMapView.map(withFrame: mapViewContainer.bounds, camera: camera);
            let marker = GMSMarker();
            marker.position = CLLocationCoordinate2D(latitude: tournament.lat, longitude: tournament.lng);
            marker.map = mapView;
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

            teamSizeLabel.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: padding);
            teamSizeLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            teamSizeLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);

            descriptionLabel.autoPinEdge(.top, to: .bottom, of: teamSizeLabel, withOffset: padding);
            descriptionLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            descriptionLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);

            startTimeLabel.autoPinEdge(.top, to: .bottom, of: descriptionLabel, withOffset: padding);
            startTimeLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            startTimeLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);

            teamsLabel.autoPinEdge(.top, to: .bottom, of: startTimeLabel, withOffset: padding);
            teamsLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            teamsLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);

            maxTeamsLabel.autoPinEdge(.top, to: .bottom, of: teamsLabel, withOffset: padding);
            maxTeamsLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
            maxTeamsLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);

            mapViewContainer.autoPinEdge(.top, to: .bottom, of: maxTeamsLabel, withOffset: mapPadding);
            mapViewContainer.autoPinEdge(toSuperviewEdge: .leading, withInset: mapPadding);
            mapViewContainer.autoPinEdge(toSuperviewEdge: .trailing, withInset: mapPadding);

            logoLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            logoLabel.autoSetDimension(.height, toSize: logoLabelHeight);
            logoLabel.autoPinEdge(toSuperviewEdge: .leading);
            logoLabel.autoPinEdge(toSuperviewEdge: .trailing);

            if(dashboard) {
                startButton.autoPinEdge(toSuperviewEdge: .bottom, withInset: padding);
                startButton.autoPinEdge(toSuperviewEdge: .leading, withInset: titlePadding);
                startButton.autoPinEdge(toSuperviewEdge: .trailing, withInset: titlePadding);
            }

            if(!registered) {
                let views: NSArray = [joinButton, createTeamButton] as NSArray;
                views.autoDistributeViews(along: .horizontal, alignedTo: .horizontal, withFixedSpacing: titlePadding, insetSpacing: true, matchedSizes: true);

                if(dashboard) {
                    joinButton.autoPinEdge(.bottom, to: .top, of: startButton, withOffset: -padding);
                } else {
                    joinButton.autoPinEdge(toSuperviewEdge: .bottom, withInset: padding);
                }

                mapViewContainer.autoPinEdge(.bottom, to: .top, of: joinButton, withOffset: -mapPadding);
            } else {
                teamViewContainer.autoPinEdge(toSuperviewEdge: .leading, withInset: padding);
                teamViewContainer.autoPinEdge(toSuperviewEdge: .trailing, withInset: padding);

                if(dashboard) {
                    teamViewContainer.autoPinEdge(.bottom, to: .top, of: startButton, withOffset: -padding);
                } else {
                    teamViewContainer.autoPinEdge(toSuperviewEdge: .bottom, withInset: padding);
                }

                teamViewContainer.autoSetDimension(.height, toSize: teamContainerHeight);
                mapViewContainer.autoPinEdge(.bottom, to: .top, of: teamViewContainer, withOffset: -mapPadding);
            }

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    @objc func join() {
        let vc = SelectTeamViewController();
        vc.setTournament(tournament);
        self.navigationController?.pushViewController(vc, animated: true);
    }

    @objc func create() {
        let vc = CreateTeamViewController();
        vc.setTournament(tournament);
        self.navigationController?.pushViewController(vc, animated: true);
    }

    @objc func exit() {
        self.navigationController?.popViewController(animated: true);
    }

    @objc func approve() {

    }

    @objc func start() {

    }

    func setRegistered(_ registered: Bool) {
        self.registered = registered;
    }

    func setDashboard(_ dashboard: Bool) {
        self.dashboard = dashboard;
    }

    func setTeam(_ team: Team) {
        self.team = team;
    }

    func setTournament(_ tournament: Tournament) {
        self.tournament = tournament;
    }
}

