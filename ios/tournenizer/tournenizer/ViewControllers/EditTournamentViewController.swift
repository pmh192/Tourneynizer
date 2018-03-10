//
//  EditTournamentViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/21/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;
import Foundation;

class EditTournamentViewController : CreateTournamentViewController {
    var tournamentCopy: Tournament!;
    var cb: ((Tournament) -> Void)?;

    var backView: UIButton!;

    let buttonPadding: CGFloat = 10;
    let iconSize: CGFloat = 25;

    override func viewDidLoad() {
        self.titleLabel.text = "Edit Tournament";

        self.nameField.text = tournamentCopy.name;
        let formatter = DateFormatter();
        formatter.dateFormat = "MM/dd/yyyy HH:mm";
        self.startTimeField.text = formatter.string(from: tournamentCopy.startTime);
        self.teamSizeField.text = tournamentCopy.teamSize.description;
        self.maxTeamsField.text = tournamentCopy.maxTeams.description;

        if(tournamentCopy.type == TournamentType.VOLLEYBALL_BRACKET) {
            self.typeField.text = pickerOptions[0];
        } else {
            self.typeField.text = pickerOptions[1];
        }

        self.locationField.text = "\(tournamentCopy.lat) \(tournamentCopy.lng)";
        self.nextButton.setTitle("Edit", for: .normal);

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
        view.addSubview(backView);
    }

    // Ensures that the corresponding methods are only called once
    var updateContraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        if(!updateContraints) {
            backView.autoSetDimension(.width, toSize: iconSize);
            backView.autoMatch(.height, to: .width, of: backView);
            backView.autoPinEdge(.bottom, to: .bottom, of: titleLabel, withOffset: -buttonPadding);
            backView.autoPinEdge(toSuperviewEdge: .leading, withInset: buttonPadding);

            updateContraints = true;
        }

        super.updateViewConstraints();
    }

    @objc override func create() {
        let tournament = getTournamentFromFields();
        if(tournament == nil) {
            return;
        }

        cb?(tournament!);

        let alert = UIAlertController(title: "Edit Tournament", message: "The tournament was successfully edited.", preferredStyle: .alert);
        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: nil));
        self.present(alert, animated: true, completion: nil);
    }

    @objc func back() {
        self.navigationController?.popViewController(animated: true);
    }

    func setTournament(_ tournament : Tournament) {
        tournamentCopy = Tournament(tournament);
    }

    func setCallback(cb: @escaping (Tournament) -> Void) {
        self.cb = cb;
    }
}

