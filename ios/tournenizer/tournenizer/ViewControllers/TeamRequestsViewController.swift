//
//  TeamRequestsViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/18/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class TeamRequestsViewController : TeamListViewController {

    override func viewDidLoad() {

        self.teams = [
            
        ];
    }

    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true;
    }

    override func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        let rejectRowAction = UITableViewRowAction(style: UITableViewRowActionStyle.default, title: "Reject", handler:{action, indexpath in
            print("delete");
        });
        rejectRowAction.backgroundColor = Constants.color.red;

        let approveRowAction = UITableViewRowAction(style: UITableViewRowActionStyle.default, title: "Approve", handler:{action, indexpath in
            print("more");
        });
        approveRowAction.backgroundColor = Constants.color.green;

        return [rejectRowAction, approveRowAction];
    }
}
