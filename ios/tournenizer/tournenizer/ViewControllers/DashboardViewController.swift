//
//  DashboardViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/4/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class DashboardViewController : UIViewController {
    var logoLabel: UILabel!;
    var statusBarCover: UIView!;
    var buttonContainer: UIView!;
    var tournamentsButton: UIButton!;
    var tournamentsButton1: UIButton!;
    var tournamentsButton2: UIButton!;
    var tournamentsButton3: UIButton!;
    var tournamentsButton4: UIButton!;
    var playersButton: UIButton!;
    var profileButton: UIButton!;
    var tournamentImage: UIImage!;

    let logoLabelHeight: CGFloat = 45;

    
    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.white;

        logoLabel = {
            let view = UILabel.newAutoLayout();
            view.backgroundColor = Constants.navy;
            view.textColor = Constants.red;
            view.font = UIFont(name: Constants.fontMedium, size: Constants.topHeaderFontSize);
            view.text = "Tourneynizer";
            view.textAlignment = .center;
            return view;
        }();

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.navy;
            return view;
        }();

        tournamentImage = UIImage(named: "tournament")?.withRenderingMode(.alwaysTemplate);

        tournamentsButton = {
            let view = UIButton.newAutoLayout();
            view.setImage(tournamentImage, for: .normal);
            view.setImage(tournamentImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = UIColor.gray;
            return view;
        }();

        tournamentsButton2 = {
            let view = UIButton.newAutoLayout();
            view.setImage(tournamentImage, for: .normal);
            view.setImage(tournamentImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = UIColor.gray;
            return view;
        }();

        tournamentsButton1 = {
            let view = UIButton.newAutoLayout();
            view.setImage(tournamentImage, for: .normal);
            view.setImage(tournamentImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = UIColor.gray;
            return view;
        }();

        tournamentsButton3 = {
            let view = UIButton.newAutoLayout();
            view.setImage(tournamentImage, for: .normal);
            view.setImage(tournamentImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = UIColor.gray;
            return view;
        }();

        tournamentsButton4 = {
            let view = UIButton.newAutoLayout();
            view.setImage(tournamentImage, for: .normal);
            view.setImage(tournamentImage, for: .focused);
            view.imageView?.contentMode = .scaleAspectFit;
            view.imageView?.tintColor = UIColor.gray;
            return view;
        }();

        buttonContainer = { [tournamentsButton, tournamentsButton1, tournamentsButton2, tournamentsButton3, tournamentsButton4] in
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.navy;
            view.addSubview(tournamentsButton!);
            view.addSubview(tournamentsButton1!);
            view.addSubview(tournamentsButton2!);
            view.addSubview(tournamentsButton3!);
            view.addSubview(tournamentsButton4!);
            return view;
        }();

        view.addSubview(statusBarCover);
        view.addSubview(logoLabel);
        view.addSubview(buttonContainer);
        view.setNeedsUpdateConstraints();
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            didUpdateConstraints = true;

            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            logoLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            logoLabel.autoSetDimension(.height, toSize: logoLabelHeight);
            logoLabel.autoPinEdge(toSuperviewEdge: .leading);
            logoLabel.autoPinEdge(toSuperviewEdge: .trailing);

            buttonContainer.autoPin(toBottomLayoutGuideOf: self, withInset: 0);
            buttonContainer.autoSetDimension(.height, toSize: 40);
            buttonContainer.autoPinEdge(toSuperviewEdge: .leading);
            buttonContainer.autoPinEdge(toSuperviewEdge: .trailing);

            let views: NSArray = [tournamentsButton, tournamentsButton1, tournamentsButton2, tournamentsButton3, tournamentsButton4];
            views.autoSetViewsDimension(.height, toSize: 40);
            views.autoDistributeViews(along: .horizontal, alignedTo: .horizontal, withFixedSpacing: 10.0, insetSpacing: true, matchedSizes: true);

            tournamentsButton.autoAlignAxis(toSuperviewAxis: .horizontal);
            tournamentsButton.imageView?.autoSetDimension(.width, toSize: 30);
            tournamentsButton2.imageView?.autoSetDimension(.width, toSize: 30);
            tournamentsButton1.imageView?.autoSetDimension(.width, toSize: 30);
            tournamentsButton3.imageView?.autoSetDimension(.width, toSize: 30);
            tournamentsButton4.imageView?.autoSetDimension(.width, toSize: 30);
        }

        super.updateViewConstraints();
    }

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
};
