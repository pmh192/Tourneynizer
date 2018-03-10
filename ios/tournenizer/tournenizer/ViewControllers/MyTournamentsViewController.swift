//
//  MyTournamentsViewController.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 2/18/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import UIKit;
import PureLayout;

class MyTournamentsViewController : UIViewController {
    let tournamentList = TournamentListViewController();

    override func loadView() {
        view = UIView();
        view.backgroundColor = Constants.color.white;
        tournamentList.setSelectCallback(selectTournament(_:));
        tournamentList.setReloadCallback {
            self.loadTournaments();
        }

        addChildViewController(tournamentList);
        tournamentList.view.frame = view.bounds;
        view.addSubview(tournamentList.view);
        tournamentList.didMove(toParentViewController: self);

        loadTournaments();

        view.setNeedsUpdateConstraints();
    }

    func selectTournament(_ tournament: Tournament) {
        let vc = TournamentViewController();
        vc.setJoinable(false);
        vc.setTournament(tournament);
        self.navigationController?.pushViewController(vc, animated: true);
    }

    // Ensures that the corresponding methods are only called once
    var didUpdateConstraints = false;

    // Sets constraints on all views
    override func updateViewConstraints() {
        super.updateViewConstraints();
    }

    func loadTournaments() {
        TournamentService.shared.getMyTournaments { (error: String?, tournaments: [Tournament]?) in
            if(error != nil) {
                return DispatchQueue.main.async {
                    self.displayError(error!);
                }
            }

            return DispatchQueue.main.async {
                self.tournamentList.setTournaments(tournaments!);
            }
        }
    }
}


