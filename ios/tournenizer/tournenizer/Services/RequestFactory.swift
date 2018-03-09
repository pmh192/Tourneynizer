//
//  RequestFactory.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/8/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation

class RequestFactory {
    static let shared = RequestFactory();

    func createRequest(type: HTTPMethod, url: URL) -> URLRequest {
        var request = URLRequest(url: url);
        request.setValue("application/json", forHTTPHeaderField: "Content-Type");
        request.httpMethod = "\(type)";

        let cookies = HTTPCookieStorage.shared.cookies(for: URL(string: Constants.serverURL + Constants.route.auth.login)!);
        if(cookies != nil) {
            var str = "";

            for cookie in cookies! {
                str.append("\(cookie.name)=\(cookie.value)");
            }

            request.setValue(str, forHTTPHeaderField: "Cookie");
        }

        return request;
    }
};
