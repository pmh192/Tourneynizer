//
//  UserService.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/8/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;

class UserService : Service {
    static let shared = UserService();

    func createUser(_ user: User, cb: @escaping (String?, User?) -> Void) {
        guard let data = try? JSONEncoder().encode(user) else {
            cb(Constants.error.genericError, nil);
            return;
        }

        makeRequest(url: "/user/create", type: .POST, body: data, cb: { (error: String?, data: Data?) in
            if(error != nil) {
                cb(error, nil);
                return;
            }

            guard let newUser = try? JSONDecoder().decode(User.self, from: data!) else {
                cb(Constants.error.genericError, nil);
                return;
            }

            cb(nil, newUser);
        });
    }

    func login(user: User, cb: @escaping (String?, User?) -> Void) {
        
    }

    func getUserList() -> [User] {
        return [];
    }
};
