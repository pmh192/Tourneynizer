//
//  ProfileViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class ProfileViewController : UIViewController {
    var user: User!;
    var currentProfile = true;
    var navigatable = false;

    var titleLabel: UILabel!;
    var emailLabel: UILabel!;
    var statusBarCover: UIView!;
    var logoutButton: UIButton!;
    var backView: UIButton!;
    var winsPrompt: UILabel!;
    var lossesPrompt: UILabel!;
    var tournamentsPrompt: UILabel!;
    var matchesPrompt: UILabel!;
    var winsContent: UILabel!;
    var lossesContent: UILabel!;
    var tournamentsContent: UILabel!;
    var matchesContent: UILabel!;

    let topTitlePadding: CGFloat = 20;
    let sideTitlePadding: CGFloat = 15;
    let emailPadding: CGFloat = 10;
    let topTeamsPadding: CGFloat = 20;
    let teamsPadding: CGFloat = 7.5;
    let buttonHeight: CGFloat = 25;
    let logoutButtonBorderWidth: CGFloat = 1;
    let logoutButtonBorderRadius: CGFloat = 5;
    let actionsBarHeight: CGFloat = 50;
    let buttonPadding: CGFloat = 10;
    let iconSize: CGFloat = 25;
    let iconPadding: CGFloat = 15;
    let statisticsClusterOffset: CGFloat = 50;

    let teamsTitle = "Past Teams:";
    let logoutText = "Sign Out";
    let winsPromptText = "Wins:";
    let lossesPromptText = "Losses:";
    let tournamentsPromptText = "Tournaments:";
    let matchesPromptText = "Matches:";
    let dialogBody = Constants.error.serverError;

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
        view.backgroundColor = Constants.color.lightGray;

        titleLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.mediumHeader);
            view.textColor = Constants.color.red;
            view.text = user.name;
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            return view;
        }();

        emailLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            view.textColor = Constants.color.navy;
            view.text = user.email;
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            return view;
        }();

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            return view;
        }();

        logoutButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(logoutText, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = logoutButtonBorderRadius;
            view.layer.borderWidth = logoutButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        logoutButton.addTarget(self, action: #selector(logout), for: .touchUpInside);

        backView = {
            let view = UIButton.newAutoLayout();
            let image = UIImage(named: "arrowright")?.withRenderingMode(.alwaysTemplate);
            view.setImage(image, for: UIControlState.normal);
            view.imageView?.transform = CGAffineTransform(scaleX: -1, y: 1);
            view.imageView?.tintColor = Constants.color.white;
            view.contentMode = .scaleAspectFit;
            return view;
        }();
        backView.addTarget(self, action: #selector(back), for: .touchUpInside);

        winsPrompt = promptGenerator();
        winsPrompt.text = winsPromptText;

        lossesPrompt = promptGenerator();
        lossesPrompt.text = lossesPromptText;

        tournamentsPrompt = promptGenerator();
        tournamentsPrompt.text = tournamentsPromptText;

        matchesPrompt = promptGenerator();
        matchesPrompt.text = matchesPromptText;

        winsContent = centerPromptGenerator();
        winsContent.text = 0.description;

        lossesContent = centerPromptGenerator();
        lossesContent.text = 0.description;

        tournamentsContent = centerPromptGenerator();
        tournamentsContent.text = 0.description;

        matchesContent = centerPromptGenerator();
        matchesContent.text = 0.description;

        view.addSubview(titleLabel);
        view.addSubview(statusBarCover);
        view.addSubview(emailLabel);
        if(currentProfile) {
            view.addSubview(logoutButton);
        }

        if(navigatable) {
            view.addSubview(backView);
        }

        view.addSubview(winsPrompt);
        view.addSubview(lossesPrompt);
        view.addSubview(tournamentsPrompt);
        view.addSubview(matchesPrompt);
        view.addSubview(winsContent);
        view.addSubview(lossesContent);
        view.addSubview(tournamentsContent);
        view.addSubview(matchesContent);

        view.setNeedsUpdateConstraints();
    }

    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!didUpdateConstraints) {
            statusBarCover.autoPin(toTopLayoutGuideOf: self, withInset: -Constants.statusBarCoverHeight);
            statusBarCover.autoSetDimension(.height, toSize: Constants.statusBarCoverHeight + actionsBarHeight);
            statusBarCover.autoPinEdge(toSuperviewEdge: .left);
            statusBarCover.autoPinEdge(toSuperviewEdge: .right);

            titleLabel.autoPinEdge(.top, to: .bottom, of: statusBarCover, withOffset: topTitlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);

            emailLabel.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: emailPadding);
            emailLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            emailLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);

            winsPrompt.autoPinEdge(.top, to: .bottom, of: emailLabel, withOffset: statisticsClusterOffset);
            winsPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            winsPrompt.autoMatch(.width, to: .width, of: view, withMultiplier: 0.35);

            lossesPrompt.autoPinEdge(.top, to: .bottom, of: winsPrompt, withOffset: topTitlePadding);
            lossesPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            lossesPrompt.autoMatch(.width, to: .width, of: view, withMultiplier: 0.35);

            tournamentsPrompt.autoPinEdge(.top, to: .bottom, of: lossesPrompt, withOffset: topTitlePadding);
            tournamentsPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            tournamentsPrompt.autoMatch(.width, to: .width, of: view, withMultiplier: 0.35);

            matchesPrompt.autoPinEdge(.top, to: .bottom, of: tournamentsPrompt, withOffset: topTitlePadding);
            matchesPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            matchesPrompt.autoMatch(.width, to: .width, of: view, withMultiplier: 0.35);

            winsContent.autoPinEdge(.leading, to: .trailing, of: winsPrompt);
            winsContent.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            winsContent.autoAlignAxis(.baseline, toSameAxisOf: winsPrompt);

            lossesContent.autoPinEdge(.leading, to: .trailing, of: lossesPrompt);
            lossesContent.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            lossesContent.autoAlignAxis(.baseline, toSameAxisOf: lossesPrompt);

            tournamentsContent.autoPinEdge(.leading, to: .trailing, of: tournamentsPrompt);
            tournamentsContent.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            tournamentsContent.autoAlignAxis(.baseline, toSameAxisOf: tournamentsPrompt);

            matchesContent.autoPinEdge(.leading, to: .trailing, of: matchesPrompt);
            matchesContent.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);
            matchesContent.autoAlignAxis(.baseline, toSameAxisOf: matchesPrompt);

            if(currentProfile) {
                logoutButton.autoPinEdge(.bottom, to: .bottom, of: statusBarCover, withOffset: -buttonPadding);
                logoutButton.autoPinEdge(toSuperviewEdge: .trailing, withInset: buttonPadding);
                logoutButton.autoMatch(.width, to: .width, of: view, withMultiplier: 0.3);
            }

            if(navigatable) {
                backView.autoSetDimension(.width, toSize: iconSize);
                backView.autoMatch(.height, to: .width, of: backView);
                backView.autoPinEdge(.bottom, to: .bottom, of: statusBarCover, withOffset: -buttonPadding);
                backView.autoPinEdge(toSuperviewEdge: .leading, withInset: buttonPadding);
            }

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    func setUser(_ user: User) {
        self.user = user;
    }

    func setCurrentProfile(_ currentProfile: Bool) {
        self.currentProfile = currentProfile;
    }

    func setNavigatable(_ navigatable: Bool) {
        self.navigatable = navigatable;
    }

    @objc func logout() {
        UserService.shared.logout { (error: String?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(Constants.error.serverError);
                }
            }

            return DispatchQueue.main.async {
                self.exitToLogin();
            }
        }
    }

    func exitToLogin() {
        let animation = CATransition();
        animation.type = kCATransitionReveal;
        animation.subtype = kCATransitionFromBottom;
        animation.timingFunction = CAMediaTimingFunction(name: kCAMediaTimingFunctionEaseInEaseOut);
        animation.duration = CFTimeInterval(0.35);

        let window = UIApplication.shared.keyWindow;
        window?.layer.add(animation, forKey: nil);
        window?.rootViewController = LoginViewContainerController();
    }

    @objc func back() {
        self.navigationController?.popViewController(animated: true);
    }

    func setUser(user: User) {
        self.user = user;
    }
}
