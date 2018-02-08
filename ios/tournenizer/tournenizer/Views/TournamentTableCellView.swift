//
//  TournamentTableCellView.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;

class TournamentTableCellView : UITableViewCell {
    var nameLabel: UILabel!;
    var creatorLabel: UILabel!;
    var currentTeamsLabel: UILabel!;
    var tournament: Tournament!;

    let nameLabelVerticalPadding: CGFloat = 15.0;
    let nameLabelHorizontalPadding: CGFloat = 10.0;

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
    }

    func setupViews() {
        self.contentView.backgroundColor = Constants.white;

        nameLabel = {
            let view = UILabel.newAutoLayout();
            view.textColor = Constants.navy;
            view.font = UIFont(name: Constants.fontMedium, size: Constants.normalFontSize);
            return view;
        }();

        self.contentView.addSubview(nameLabel!);
    }

    var didUpdateConstraints = false;

    override func updateConstraints() {
        if(!didUpdateConstraints) {
            NSLayoutConstraint.autoSetPriority(UILayoutPriority.required) {
                nameLabel.autoSetContentCompressionResistancePriority(for: .vertical);
            };

            nameLabel.autoPinEdge(toSuperviewEdge: .top, withInset: nameLabelVerticalPadding);
            nameLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: nameLabelHorizontalPadding);
            nameLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: nameLabelHorizontalPadding);

            didUpdateConstraints = true;
        }

        super.updateConstraints();
    }
};
