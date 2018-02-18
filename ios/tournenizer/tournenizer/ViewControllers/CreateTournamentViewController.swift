//
//  CreateTournamentViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/7/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class CreateTournamentViewController : UIViewController, UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource {
    var titleLabel: UILabel!;
    var namePrompt: UILabel!;
    var startTimePrompt: UILabel!;
    var teamSizePrompt: UILabel!;
    var maxTeamsPrompt: UILabel!;
    var typePrompt: UILabel!;
    var numCourtsPrompt: UILabel!;
    var statusBarCover: UIView!;

    var startTimePicker: UIDatePicker!;
    var pickerToolbar: UIToolbar!;
    var optionPicker: UIPickerView!;

    var nameField: UITextField!;
    var startTimeField: UITextField!;
    var teamSizeField: UITextField!;
    var maxTeamsField: UITextField!;
    var typeField: UITextField!;
    var numCourtsField: UITextField!;

    var nextButton: UIButton!;

    let titleText = "Create A Tournament";
    let namePromptText = "Name:";
    let addressPromptText = "Address:";
    let startTimePromptText = "Start Time:";
    let teamSizePromptText = "Team Size:";
    let maxTeamsPromptText = "Max Teams:";
    let typePromptText = "Tournament Type:";
    let numCourtsPromptText = "# Courts:";
    let toolbarDone = "Next";

    let sideTitlePadding: CGFloat = 10;
    let topTitlePadding: CGFloat = 10;
    let promptTopPadding: CGFloat = 12;
    let promptPadding: CGFloat = 10;
    let leftPercentWidth: CGFloat = 0.45;
    let nextButtonBorderWidth: CGFloat = 1;
    let nextButtonBorderRadius: CGFloat = 5;
    let buttonWidth: CGFloat = 60;
    let errorBorderWidth: CGFloat = 1;

    let pickerOptions = [
        "Pool",
        "Bracket"
    ];

    var type: TournamentType = TournamentType.VOLLEYBALL_POOLED;

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
            view.font = UIFont(name: Constants.font.medium, size: Constants.fontSize.mediumHeader);
            view.textColor = Constants.color.red;
            view.text = titleText;
            view.textAlignment = .center;
            return view;
        }();


        namePrompt = promptGenerator();
        namePrompt.text = namePromptText;

        startTimePrompt = promptGenerator();
        startTimePrompt.text = startTimePromptText;

        teamSizePrompt = promptGenerator();
        teamSizePrompt.text = teamSizePromptText;

        maxTeamsPrompt = promptGenerator();
        maxTeamsPrompt.text = maxTeamsPromptText;

        typePrompt = promptGenerator();
        typePrompt.text = typePromptText;

        numCourtsPrompt = promptGenerator();
        numCourtsPrompt.text = numCourtsPromptText;

        nameField = fieldGenerator();

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

        numCourtsField = fieldGenerator();
        numCourtsField.keyboardType = .numbersAndPunctuation;
        numCourtsField.returnKeyType = .done;

        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard));
        tap.cancelsTouchesInView = false;
        view.addGestureRecognizer(tap);

        nextButton = {
            let view = UIButton.newAutoLayout();
            view.setTitle(toolbarDone, for: .normal);
            view.setTitleColor(Constants.color.white, for: .normal);
            view.titleLabel?.font = UIFont(name: Constants.font.normal, size: Constants.fontSize.normal);
            view.layer.cornerRadius = nextButtonBorderRadius;
            view.layer.borderWidth = nextButtonBorderWidth;
            view.layer.borderColor = Constants.color.lightBlue.cgColor;
            view.backgroundColor = Constants.color.lightBlue;
            view.titleLabel?.lineBreakMode = .byCharWrapping;
            return view;
        }();
        nextButton.addTarget(self, action: #selector(nextPage), for: .touchUpInside);


        view.addSubview(statusBarCover);
        view.addSubview(titleLabel);
        view.addSubview(namePrompt);
        view.addSubview(startTimePrompt);
        view.addSubview(teamSizePrompt);
        view.addSubview(maxTeamsPrompt);
        view.addSubview(typePrompt);
        view.addSubview(numCourtsPrompt);
        view.addSubview(nameField);
        view.addSubview(startTimeField);
        view.addSubview(teamSizeField);
        view.addSubview(maxTeamsField);
        view.addSubview(typeField);
        view.addSubview(numCourtsField);
        view.addSubview(nextButton);
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

            titleLabel.autoPinEdge(.top, to: .bottom, of: statusBarCover, withOffset: topTitlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .leading, withInset: sideTitlePadding);
            titleLabel.autoPinEdge(toSuperviewEdge: .trailing, withInset: sideTitlePadding);

            namePrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            namePrompt.autoPinEdge(.top, to: .bottom, of: titleLabel, withOffset: promptTopPadding);
            namePrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            startTimePrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            startTimePrompt.autoPinEdge(.top, to: .bottom, of: namePrompt, withOffset: promptTopPadding);
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

            numCourtsPrompt.autoPinEdge(toSuperviewEdge: .leading, withInset: promptPadding);
            numCourtsPrompt.autoPinEdge(.top, to: .bottom, of: typePrompt, withOffset: promptTopPadding);
            numCourtsPrompt.autoMatch(.width, to: .width, of: view, withMultiplier: leftPercentWidth);

            nameField.autoAlignAxis(.baseline, toSameAxisOf: namePrompt);
            nameField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            nameField.autoPinEdge(.leading, to: .trailing, of: namePrompt);

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

            numCourtsField.autoAlignAxis(.baseline, toSameAxisOf: numCourtsPrompt);
            numCourtsField.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            numCourtsField.autoPinEdge(.leading, to: .trailing, of: numCourtsPrompt);
            
            nextButton.autoPinEdge(toSuperviewEdge: .trailing, withInset: promptPadding);
            nextButton.autoPinEdge(.top, to: .bottom, of: numCourtsPrompt, withOffset: promptTopPadding);
            nextButton.autoSetDimension(.width, toSize: buttonWidth);

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
            startTimeField.becomeFirstResponder();
        } else if(textField == teamSizeField) {
            maxTeamsField.becomeFirstResponder();
        } else if(textField == maxTeamsField) {
            typeField.becomeFirstResponder();
        } else {
            numCourtsField.resignFirstResponder();
            nextPage();
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
        if(row == 0) {
            type = TournamentType.VOLLEYBALL_POOLED;
        } else {
            type = TournamentType.VOLLEYBALL_BRACKET;
        }
    };

    @objc func nextField(sender: UIBarButtonItem) {
        if(typeField.isFirstResponder) {
            pickerView(optionPicker, didSelectRow: optionPicker.selectedRow(inComponent: 0), inComponent: 0);
            numCourtsField.becomeFirstResponder();
        } else {
            updateTextField(sender: startTimePicker);
            teamSizeField.becomeFirstResponder();
        }
    }

    @objc func nextPage() {
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

        let courts: Int = {
            guard let value = Int(numCourtsField.text!) else {
                return -1;
            }
            return value;
        }();

        if(courts <= 0) {
            numCourtsField.layer.borderColor = Constants.color.red.cgColor;
            error = true;
        } else {
            numCourtsField.layer.borderColor = UIColor.clear.cgColor;
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

        if(error) {
            return;
        }

        let tournament = Tournament(
            id: 0,
            name: name,
            description: nil,
            address: "",
            startTime: startTimePicker.date,
            endTime: nil,
            maxTeams: maxTeams,
            currentTeams: 0,
            timeCreated: Date(),
            tournamentType: type,
            logo: nil,
            courts: courts,
            creatorId: 0,
            cancelled: false,
            teamSize: teamSize
        );
    }
}