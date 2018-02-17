//
//  UserCellView.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/17/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import Foundation;

class UserCellView : UITableViewCell {
    var nameLabel: UILabel!;
    var emailLabel: UILabel!;
    var user: User!;

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

    func setTeam(_ user: User) {
        self.user = user;
        nameLabel.text = user.name;
        emailLabel.text = user.email;

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

        emailLabel = {
            let view = UILabel.newAutoLayout();
            view.textColor = Constants.color.darkGray;
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.small);
            return view;
        }();

        self.selectionStyle = .none;

        contentView.addSubview(nameLabel!);
        contentView.addSubview(emailLabel!);
    }

    override func updateConstraints() {
        NSLayoutConstraint.autoSetPriority(UILayoutPriority.required) {
            nameLabel.autoSetContentCompressionResistancePriority(for: .vertical);
            emailLabel.autoSetContentCompressionResistancePriority(for: .vertical);
        };

        nameLabel.autoPinEdge(toSuperviewEdge: .top, withInset: topPadding);
        nameLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sidePadding);
        nameLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sidePadding);

        emailLabel.autoPinEdge(.top, to: .bottom, of: nameLabel);
        emailLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sidePadding);
        emailLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sidePadding);
        emailLabel.autoPinEdge(toSuperviewEdge: .bottom, withInset: sidePadding);

        super.updateConstraints();
    }
};


