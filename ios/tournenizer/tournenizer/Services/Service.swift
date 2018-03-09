//
//  Service.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/8/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation;

class Service {
    func makeRequest(url: String, type: HTTPMethod, body: Data?, cb: @escaping (String?, Data?) -> Void) {
        let path = URL(string: Constants.serverURL + url)!;

        let request = RequestFactory.shared.createRequest(type: type, url: path);
        let task = URLSession.shared.uploadTask(with: request, from: body) { dat, res, error in
            if(error != nil) {
                print("error: \(error.debugDescription)");
                return cb(Constants.error.serverError, nil);
            }

            guard let response = res as? HTTPURLResponse else {
                return cb(Constants.error.serverError, nil);
            }

            guard let data: Data = dat else {
                cb(Constants.error.serverError, nil);
                return;
            }

            if(!(200...299).contains(response.statusCode)) {
                guard let error = try? JSONDecoder().decode(ServerError.self, from: data) else {
                    return cb(Constants.error.genericError, nil);
                }

                return cb(error.message, nil);
            }

            return cb(nil, data);
        };

        task.resume();
    }
};
