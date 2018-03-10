//
//  TeamRequestCellView.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/9/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import Foundation;

class TeamRequestCellView : UITableViewCell {
    var nameLabel: UILabel!;
    var tournamentLabel: UILabel!;
    var creatorLabel: UILabel!;
    var teamRequest: TeamRequest!;
    var tournament: Tournament!;
    var requester: User!;
    var team: Team!;

    let topPadding: CGFloat = 10;
    let bottomPadding: CGFloat = 10;
    let sidePadding: CGFloat = 10;
    let elementSpacing: CGFloat = 15;
    let iconSize: CGFloat = 20;

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder);
    }

    override init(style: UITableViewCellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier);

        setupViews();
    }

    func setTeamRequest(_ teamRequest: TeamRequest) {
        self.teamRequest = teamRequest;
    }

    func setTeam(_ team: Team) {
        self.team = team;
        self.nameLabel.text = team.name;
    }

    func setTournament(_ tournament: Tournament) {
        self.tournament = tournament;
        self.tournamentLabel.text = tournament.name;
    }

    func setRequester(_ requester: User) {
        self.requester = requester;
        self.creatorLabel.text = requester.name;
    }

    func setupViews() {
        contentView.backgroundColor = Constants.color.white;

        nameLabel = {
            let view = UILabel.newAutoLayout();
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            view.textColor = Constants.color.darkGray;
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            return view;
        }();

        tournamentLabel = {
            let view = UILabel.newAutoLayout();
            view.textColor = Constants.color.darkGray;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.small);
            return view;
        }();

        creatorLabel = {
            let view = UILabel.newAutoLayout();
            view.textColor = Constants.color.darkGray;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.small);
            return view;
        }();

        self.selectionStyle = .none;

        contentView.addSubview(nameLabel!);
        contentView.addSubview(tournamentLabel!);
        contentView.addSubview(creatorLabel!);
    }

    override func updateConstraints() {
        NSLayoutConstraint.autoSetPriority(UILayoutPriority.required) {
            nameLabel.autoSetContentCompressionResistancePriority(for: .vertical);
            tournamentLabel.autoSetContentCompressionResistancePriority(for: .vertical);
        };

        nameLabel.autoPinEdge(toSuperviewEdge: .top, withInset: topPadding);
        nameLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sidePadding);
        nameLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sidePadding);

        tournamentLabel.autoPinEdge(.top, to: .bottom, of: nameLabel);
        tournamentLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sidePadding);
        tournamentLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sidePadding);

        creatorLabel.autoPinEdge(.top, to: .bottom, of: tournamentLabel);
        creatorLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sidePadding);
        creatorLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sidePadding);
        creatorLabel.autoPinEdge(toSuperviewEdge: .bottom, withInset: sidePadding);

        super.updateConstraints();
    }
};
