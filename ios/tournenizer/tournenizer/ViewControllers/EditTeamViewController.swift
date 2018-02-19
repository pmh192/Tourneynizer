//
//  EditTeamViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/18/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

class EditTeamViewController : CreateTeamViewController {
    var initialNameFieldText = ""

    override func viewDidLoad() {
        self.selectLabel.text = "Update Team";
        self.createButton.setTitle("Update", for: .normal);
        self.nameField.text = initialNameFieldText;
    }

    func setTeam(_ team : Team) {
        initialNameFieldText = team.name;
    }
}
