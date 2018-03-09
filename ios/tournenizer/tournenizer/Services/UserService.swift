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

    var currentUser: User? = nil;

    func getCurrentUser() -> User? {
        return currentUser;
    }

    func createUser(_ user: User, cb: @escaping (String?, User?) -> Void) {
        let data = encodeUser(user);
        if(data == nil) {
            return cb(Constants.error.genericError, nil);
        }

        makeRequest(url: Constants.route.user.create, type: .POST, body: data) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            let newUser = self.decodeUser(data!);
            if(newUser == nil) {
                return cb(Constants.error.genericError, nil);
            }

            return cb(nil, newUser);
        }
    }

    func login(user: User, cb: @escaping (String?, User?) -> Void) {
        let data = encodeUser(user);
        if(data == nil) {
            return cb(Constants.error.genericError, nil);
        }
        
        makeRequest(url: Constants.route.auth.login, type: .POST, body: data) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error, nil);
            }

            let currentUser = self.decodeUser(data!);
            if(currentUser == nil) {
                return cb(Constants.error.genericError, nil);
            }

            self.currentUser = currentUser;
            return cb(nil, currentUser);
        }
    }

    func logout(cb: @escaping (String?) -> Void) {
        makeRequest(url: Constants.route.auth.logout, type: .POST, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(error);
            }

            self.currentUser = nil;
            return cb(nil);
        }
    }

    func isLoggedIn(cb: @escaping (Bool) -> Void) {
        makeRequest(url: Constants.route.user.current, type: .GET, body: Data(base64Encoded: "")) { (error: String?, data: Data?) in
            if(error != nil) {
                return cb(false);
            }

            let user = self.decodeUser(data!);
            if(user == nil) {
                return cb(false);
            }

            self.currentUser = user;
            return cb(true);
        }
    }

    private func encodeUser(_ user: User) -> Data? {
        guard let data = try? JSONEncoder().encode(user) else {
            return nil;
        }

        return data;
    }

    private func decodeUser(_ data: Data) -> User? {
        guard let user = try? JSONDecoder().decode(User.self, from: data) else {
            return nil;
        }

        return user;
    }
};
