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
        return request;
    }
};
