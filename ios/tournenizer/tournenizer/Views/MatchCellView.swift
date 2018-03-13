//
//  MatchCellView.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/13/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import Foundation;

class MatchCellView : UITableViewCell {
    var teamLabel: UILabel!;
    var refereeLabel: UILabel!;

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

    func setData(team1: Team, team2: Team, referee: User) {
        teamLabel.text = "\(team1.name) vs \(team2.name)";
        refereeLabel.text = "Referee: \(referee.name) \(referee.email)";

        setNeedsLayout();
        layoutIfNeeded();
        setNeedsUpdateConstraints();
        updateConstraintsIfNeeded();
    }

    func setupViews() {
        contentView.backgroundColor = Constants.color.white;

        teamLabel = {
            let view = UILabel.newAutoLayout();
            view.lineBreakMode = .byWordWrapping;
            view.numberOfLines = 0;
            view.textColor = Constants.color.darkGray;
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.normal);
            return view;
        }();

        refereeLabel = {
            let view = UILabel.newAutoLayout();
            view.textColor = Constants.color.darkGray;
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.small);
            return view;
        }();

        self.selectionStyle = .none;

        contentView.addSubview(teamLabel);
        contentView.addSubview(refereeLabel);
    }

    override func updateConstraints() {
        NSLayoutConstraint.autoSetPriority(UILayoutPriority.required) {
            teamLabel.autoSetContentCompressionResistancePriority(for: .vertical);
            refereeLabel.autoSetContentCompressionResistancePriority(for: .vertical);
        };

        teamLabel.autoPinEdge(toSuperviewEdge: .top, withInset: topPadding);
        teamLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sidePadding);
        teamLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sidePadding);

        refereeLabel.autoPinEdge(.top, to: .bottom, of: teamLabel);
        refereeLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sidePadding);
        refereeLabel.autoPinEdge(toSuperviewEdge: .bottom, withInset: bottomPadding);

        super.updateConstraints();
    }
};
