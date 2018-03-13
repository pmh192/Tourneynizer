//
//  CreateTournamentViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;
import GooglePlacePicker;

class CreateTournamentViewController : UIViewController, UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource, GMSPlacePickerViewControllerDelegate {
    var titleLabel: UILabel!;
    var descriptionPrompt: UILabel!;
    var namePrompt: UILabel!;
    var startTimePrompt: UILabel!;
    var teamSizePrompt: UILabel!;
    var maxTeamsPrompt: UILabel!;
    var typePrompt: UILabel!;
    var locationPrompt: UILabel!;
    var statusBarCover: UIView!;

    var startTimePicker: UIDatePicker!;
    var pickerToolbar: UIToolbar!;
    var optionPicker: UIPickerView!;

    var place: GMSPlace!;

    var nameField: UITextField!;
    var descriptionField: UITextField!;
    var startTimeField: UITextField!;
    var teamSizeField: UITextField!;
    var maxTeamsField: UITextField!;
    var typeField: UITextField!;
    var locationField: UITextField!;
    var nextButton: UIButton!;
    var clearButton: UIButton!;

    let titleText = "Create A Tournament";
    let descriptionPromptText = "Description:";
    let namePromptText = "Name:";
    let addressPromptText = "Address:";
    let startTimePromptText = "Start Time:";
    let teamSizePromptText = "Team Size:";
    let maxTeamsPromptText = "Max Teams:";
    let typePromptText = "Tournament Type:";
    let numCourtsPromptText = "# Courts:";
    let locationPromptText = "Location:";
    let toolbarDone = "Next";
    let toolbarCreate = "Create";
    let toolbarClear = "Clear";
    let dialogTitle = "Tournament Created";
    let dialogBody = "Your tournament has been created.";
    let dialogButtonText = "Ok";

    let sideTitlePadding: CGFloat = 10;
    let topTitlePadding: CGFloat = 10;
    let promptTopPadding: CGFloat = 12;
    let promptPadding: CGFloat = 10;
    let leftPercentWidth: CGFloat = 0.45;
    let nextButtonBorderWidth: CGFloat = 1;
    let nextButtonBorderRadius: CGFloat = 5;
    let buttonWidth: CGFloat = 80;
    let errorBorderWidth: CGFloat = 1;
    let logoLabelHeight: CGFloat = 50;

    let pickerOptions = [
        "Bracket"
    ];

    var type = TournamentType.VOLLEYBALL_BRACKET;

    private func promptGenerator() -> UILabel {
        let view = UILabel.newAutoLayout();
        view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
        view.textColor = Constants.color.darkGray;
        return view;
    }

    private func fieldGenerator() -> UITextField {
        let view = UITextField.newAutoLayout();
        view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
        view.textColor = Constants.color.navy;
        view.returnKeyType = .next;
        view.backgroundColor = Constants.color.lightGray;
        view.textAlignment = .center;
        view.delegate = self;
        view.layer.borderWidth = errorBorderWidth;
        view.layer.borderColor = UIColor.clear.cgColor;
        return view;
    }

    private func buttonGenerator() -> UIButton {
        let view = UIButton.newAutoLayout();
        view.setTitleColor(Constants.color.white, for: .normal);
        view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
        view.layer.cornerRadius = nextButtonBorderRadius;
        view.layer.borderWidth = nextButtonBorderWidth;
        view.layer.borderColor = Constants.color.lightBlue.cgColor;
        view.backgroundColor = Constants.color.lightBlue;
        view.titleLabel?.lineBreakMode = .byCharWrapping;
        return view;
    }

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;

        statusBarCover = {
            let view = UIView.newAutoLayout();
            view.backgroundColor = Constants.color.navy;
            return view;
        }();

        titleLabel = {
            let view = UILabel.newAutoLayout();
            view.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.mediumHeader);
            view.textColor = Constants.color.red;
            view.backgroundColor = Constants.color.navy;
            view.text = titleText;
            view.textAlignment = .center;
            return view;
        }();

        namePrompt = promptGenerator();
        namePrompt.text = namePromptText;

        descriptionPrompt = promptGenerator();
        descriptionPrompt.text = descriptionPromptText;

        startTimePrompt = promptGenerator();
        startTimePrompt.text = startTimePromptText;

        teamSizePrompt = promptGenerator();
        teamSizePrompt.text = teamSizePromptText;

        maxTeamsPrompt = promptGenerator();
        maxTeamsPrompt.text = maxTeamsPromptText;

        typePrompt = promptGenerator();
        typePrompt.text = typePromptText;

        locationPrompt = promptGenerator();
        locationPrompt.text = locationPromptText;

        nameField = fieldGenerator();
        descriptionField = fieldGenerator();

        startTimePicker = UIDatePicker();
        startTimePicker.addTarget(self, action: #selector(updateTextField(sender:)), for: .editingDidBegin);
        startTimePicker.addTarget(self, action: #selector(updateTextField(sender:)), for: .valueChanged);
        startTimePicker.minimumDate = Date();

        pickerToolbar = UIToolbar();
        pickerToolbar.isTranslucent = true;
        pickerToolbar.sizeToFit();
        pickerToolbar.items = [
            UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil),
            UIBarButtonItem(title: toolbarDone, style: .plain, target: self, action: #selector(nextField))
        ];

        startTimeField = fieldGenerator();
        startTimeField.inputView = startTimePicker;
        startTimeField.inputAccessoryView = pickerToolbar;
        startTimeField.selectedTextRange = nil;

        teamSizeField = fieldGenerator();
        teamSizeField.keyboardType = .numbersAndPunctuation;

        maxTeamsField = fieldGenerator();
        maxTeamsField.keyboardType = .numbersAndPunctuation;

        typeField = fieldGenerator();
        optionPicker = UIPickerView();
        typeField.inputView = optionPicker;
        optionPicker.delegate = self;
        typeField.selectedTextRange = nil;
        typeField.inputAccessoryView = pickerToolbar;

        locationField = fieldGenerator();
        locationField.addTarget(self, action: #selector(locationEdit), for: .editingDidBegin);
        locationField.inputView = UIView();

        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard));
        tap.cancelsTouchesInView = false;
        view.addGestureRecognizer(tap);

        nextButton = buttonGenerator();
        nextButton.setTitle(toolbarCreate, for: .normal);
        nextButton.addTarget(self, action: #selector(create), for: .touchUpInside);

        clearButton = buttonGenerator();
        clearButton.setTitle(toolbarClear, for: .normal);
        clearButton.addTarget(self, action: #selector(clearFields), for: .touchUpInside);

        view.addSubview(statusBarCover);
        view.addSubview(titleLabel);
        view.addSubview(namePrompt);
        view.addSubview(descriptionPrompt);
        view.addSubview(startTimePrompt);
        view.addSubview(teamSizePrompt);
        view.addSubview(maxTeamsPrompt);
        view.addSubview(typePrompt);
        view.addSubview(locationPrompt);
        view.addSubview(nameField);
        view.addSubview(descriptionField);
        view.addSubview(startTimeField);
        view.addSubview(teamSizeField);
        view.addSubview(maxTeamsField);
        view.addSubview(typeField);
        view.addSubview(locationField);
        view.addSubview(nextButton);
        view.addSubview(clearButton);
        view.setNeedsUpdateConstraints();
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

            titleLabel.autoPin(toTopLayoutGuideOf: self, withInset: 0);
            titleLabel.autoSetDimension(.height, toSize: logoLabelHeight);
            titleLabel.autoPinEdge(toSuperviewEdge: .leading);
            titleLabel.autoPinEdge(toSuperviewEdge: .trailing);

            namePrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            namePrompt.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: promptTopPadding);
            namePrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            descriptionPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            descriptionPrompt.autoPinEdge(.top, to: .bottom, of: namePrompt, withOffset: promptTopPadding);
            descriptionPrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            startTimePrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            startTimePrompt.autoPinEdge(.top, to: .bottom, of: descriptionPrompt, withOffset: promptTopPadding);
            startTimePrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            teamSizePrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            teamSizePrompt.autoPinEdge(.top, to: .bottom, of: startTimePrompt, withOffset: promptTopPadding);
            teamSizePrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            maxTeamsPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            maxTeamsPrompt.autoPinEdge(.top, to: .bottom, of: teamSizePrompt, withOffset: promptTopPadding);
            maxTeamsPrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            typePrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            typePrompt.autoPinEdge(.top, to: .bottom, of: maxTeamsPrompt, withOffset: promptTopPadding);
            typePrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            locationPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            locationPrompt.autoPinEdge(.top, to: .bottom, of: typePrompt, withOffset: promptTopPadding);
            locationPrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            nameField.autoAlignAxis(.baseline, toSameAxisOf: namePrompt);
            nameField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            nameField.autoPinEdge(.leading, to: .trailing, of: namePrompt);

            descriptionField.autoAlignAxis(.baseline, toSameAxisOf: descriptionPrompt);
            descriptionField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            descriptionField.autoPinEdge(.leading, to: .trailing, of: descriptionPrompt);

            startTimeField.autoAlignAxis(.baseline, toSameAxisOf: startTimePrompt);
            startTimeField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            startTimeField.autoPinEdge(.leading, to: .trailing, of: startTimePrompt);

            teamSizeField.autoAlignAxis(.baseline, toSameAxisOf: teamSizePrompt);
            teamSizeField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            teamSizeField.autoPinEdge(.leading, to: .trailing, of: teamSizePrompt);

            maxTeamsField.autoAlignAxis(.baseline, toSameAxisOf: maxTeamsPrompt);
            maxTeamsField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            maxTeamsField.autoPinEdge(.leading, to: .trailing, of: maxTeamsPrompt);

            typeField.autoAlignAxis(.baseline, toSameAxisOf: typePrompt);
            typeField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            typeField.autoPinEdge(.leading, to: .trailing, of: typePrompt);

            locationField.autoAlignAxis(.baseline, toSameAxisOf: locationPrompt);
            locationField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            locationField.autoPinEdge(.leading, to: .trailing, of: locationPrompt);
            
            nextButton.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            nextButton.autoPinEdge(.top, to: .bottom, of: locationField, withOffset: promptTopPadding);
            nextButton.autoSetDimension(.width, toSize: buttonWidth);

            clearButton.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            clearButton.autoPinEdge(.top, to: .bottom, of: locationField, withOffset: promptTopPadding);
            clearButton.autoSetDimension(.width, toSize: buttonWidth);

            didUpdateConstraints = true;
        }

        super.updateViewConstraints();
    }

    @objc func updateTextField(sender: UIDatePicker) {
        let formatter = DateFormatter();
        formatter.dateFormat = "MM/dd/yyyy HH:mm";
        startTimeField.text = formatter.string(from: sender.date);
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if(textField == nameField) {
            descriptionField.becomeFirstResponder();
        } else if(textField == descriptionField) {
            startTimeField.becomeFirstResponder();
        } else if(textField == teamSizeField) {
            maxTeamsField.becomeFirstResponder();
        } else if(textField == maxTeamsField) {
            typeField.becomeFirstResponder();
        }

        return false;
    }

    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1;
    }

    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerOptions.count;
    }

    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerOptions[row];
    }

    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        typeField.text = pickerOptions[row];
        type = TournamentType.VOLLEYBALL_BRACKET;
    };

    @objc func nextField(sender: UIBarButtonItem) {
        if(typeField.isFirstResponder) {
            pickerView(optionPicker, didSelectRow: optionPicker.selectedRow(inComponent: 0), inComponent: 0);
            locationField.becomeFirstResponder();
        } else {
            updateTextField(sender: startTimePicker);
            teamSizeField.becomeFirstResponder();
        }
    }

    func getTournamentFromFields() -> Tournament? {
        var error = false;

        let maxTeams: Int = {
            guard let value = Int(maxTeamsField.text!) else {
                return -1;
            }
            return value;
        }();

        if(maxTeams <= 0) {
            error = true;
            maxTeamsField.layer.borderColor = Constants.color.red.cgColor;
        } else {
            maxTeamsField.layer.borderColor = UIColor.clear.cgColor;
        }

        let teamSize: Int = {
            guard let value = Int(teamSizeField.text!) else {
                return -1;
            }
            return value;
        }();

        if(teamSize <= 0) {
            teamSizeField.layer.borderColor = Constants.color.red.cgColor;
            error = true;
        } else {
            teamSizeField.layer.borderColor = UIColor.clear.cgColor;
        }

        if(nameField.text == nil || nameField.text == "") {
            nameField.layer.borderColor = Constants.color.red.cgColor;
            error = true;
        } else {
            nameField.layer.borderColor = UIColor.clear.cgColor;
        }
        let name = nameField.text!;

        if(typeField.text == nil || typeField.text == "") {
            typeField.layer.borderColor = Constants.color.red.cgColor;
            error = true;
        } else {
            typeField.layer.borderColor = UIColor.clear.cgColor;
        }

        if(startTimeField.text == nil || startTimeField.text == "") {
            startTimeField.layer.borderColor = Constants.color.red.cgColor;
            error = true;
        } else {
            startTimeField.layer.borderColor = UIColor.clear.cgColor;
        }

        if(place == nil || locationField.text == nil || locationField.text == "") {
            locationField.layer.borderColor = Constants.color.red.cgColor;
            error = true;
        } else {
            locationField.layer.borderColor = UIColor.clear.cgColor;
        }

        if(error) {
            return nil;
        }

        return Tournament(
            id: 0,
            name: name,
            description: descriptionField?.text,
            lat: place.coordinate.latitude,
            lng: place.coordinate.longitude,
            startTime: startTimePicker.date,
            maxTeams: maxTeams,
            currentTeams: 0,
            timeCreated: Date(),
            tournamentType: type,
            creatorId: 0,
            cancelled: false,
            teamSize: teamSize,
            status: nil
        );
    }

    @objc func create() {
        let tournament = getTournamentFromFields();
        if(tournament == nil) {
            return;
        }

        TournamentService.shared.createTournament(tournament!) { (error: String?, tournament: Tournament?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }

            return DispatchQueue.main.async {
                let alert = UIAlertController(title: self.dialogTitle, message: self.dialogBody, preferredStyle: .alert);
                alert.addAction(UIAlertAction(title: self.dialogButtonText, style: .default, handler: { _ in
                    self.clearFields();
                }));
                self.present(alert, animated: true, completion: nil);
            }
        }
    }

    @objc func locationEdit() {
        let config = GMSPlacePickerConfig(viewport: nil);
        let placePicker = GMSPlacePickerViewController(config: config);
        placePicker.delegate = self;
        placePicker.modalPresentationStyle = .overFullScreen;
        present(placePicker, animated: true, completion: nil);
    }

    func placePicker(_ viewController: GMSPlacePickerViewController, didPick place: GMSPlace) {
        viewController.dismiss(animated: true, completion: nil);
        self.place = place;
        locationField.text = place.formattedAddress;
        if(place.formattedAddress == nil) {
            locationField.text = "\(place.coordinate.latitude) \(place.coordinate.longitude)";
        }

        locationField.resignFirstResponder();
    }

    func placePickerDidCancel(_ viewController: GMSPlacePickerViewController) {
        viewController.dismiss(animated: true, completion: nil);

        locationField.resignFirstResponder();
    }

    @objc func clearFields() {
        place = nil;
        startTimePicker.date = Date();

        nameField.text = "";
        descriptionField.text = "";
        startTimeField.text = "";
        teamSizeField.text = "";
        maxTeamsField.text = "";
        typeField.text = "";
        locationField.text = "";

        nameField.layer.borderColor = UIColor.clear.cgColor;
        startTimeField.layer.borderColor = UIColor.clear.cgColor;
        teamSizeField.layer.borderColor = UIColor.clear.cgColor;
        maxTeamsField.layer.borderColor = UIColor.clear.cgColor;
        typeField.layer.borderColor = UIColor.clear.cgColor;
        locationField.layer.borderColor = UIColor.clear.cgColor;
    }
}
