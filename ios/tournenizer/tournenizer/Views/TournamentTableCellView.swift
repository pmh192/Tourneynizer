//
//  TournamentTableCellView.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import Foundation;

class TournamentTableCellView : UITableViewCell {
    var nameLabel: UILabel!;
    var addressLabel: UILabel!;
    var dateLabel: UILabel!;

    var tournament: Tournament!;

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

    func setTournament(_ tournament: Tournament) {
        self.tournament = tournament;
        nameLabel.text = tournament.name;
        addressLabel.text = tournament.address;

        let formatter = DateFormatter();
        formatter.dateFormat = "MM/dd/yyyy";
        dateLabel.text = "Starts On: " + formatter.string(from: tournament.startTime);

        setNeedsLayout();
        layoutIfNeeded();
        setNeedsUpdateConstraints();
        updateConstraintsIfNeeded();
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

        addressLabel = {
            let view = UILabel.newAutoLayout();
            view.textColor = Constants.color.darkGray;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.small);
            return view;
        }();

        dateLabel = {
            let view = UILabel.newAutoLayout();
            view.textColor = Constants.color.darkGray;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.small);
            return view;
        }();

        self.selectionStyle = .none;

        contentView.addSubview(nameLabel!);
        contentView.addSubview(addressLabel!);
        contentView.addSubview(dateLabel);
    }

    override func updateConstraints() {
        NSLayoutConstraint.autoSetPriority(UILayoutPriority.required) {
            nameLabel.autoSetContentCompressionResistancePriority(for: .vertical);
            addressLabel.autoSetContentCompressionResistancePriority(for: .vertical);
        };

        nameLabel.autoPinEdge(toSuperviewEdge: .top, withInset: topPadding);
        nameLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sidePadding);
        nameLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sidePadding);

        addressLabel.autoPinEdge(.top, to: .bottom, of: nameLabel);
        addressLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sidePadding);
        addressLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sidePadding);

        dateLabel.autoPinEdge(.top, to: .bottom, of: addressLabel);
        dateLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sidePadding);
        dateLabel.autoPinEdge(toSuperviewEdge: .bottom, withInset: bottomPadding);

        super.updateConstraints();
    }
};
