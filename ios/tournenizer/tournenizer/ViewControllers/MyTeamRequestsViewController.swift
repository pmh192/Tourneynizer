//
//  MyTeamRequestsViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/9/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;
import UIKit;

class MyTeamRequestsViewController : UIViewController {
    let teamRequests = TeamRequestListViewController();

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;
        teamRequests.setReloadCallback {
            self.loadTeamRequests();
        }
        teamRequests.setAcceptCallback(acceptRequest(_:index:));
        teamRequests.setRejectCallback(rejectRequest(_:index:));
        
        addChildViewController(teamRequests);
        teamRequests.view.frame = view.bounds;
        view.addSubview(teamRequests.view);
        teamRequests.didMove(toParentViewController: self);

        loadTeamRequests();

        view.setNeedsUpdateConstraints();
    }

    func acceptRequest(_ teamRequest: TeamRequest, index: Int) {

    }

    func rejectRequest(_ teamRequest: TeamRequest, index: Int) {

    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        super.updateViewConstraints();
    }

    func loadTeamRequests() {
        TeamRequestService.shared.getRequestsForCurrentUser { (error: String?, requests: [TeamRequest]?) in
            if(error != nil) {
                return;
            }

            DispatchQueue.main.async {
                self.teamRequests.setTeamRequests(requests!);
            }
        }
    }
}
