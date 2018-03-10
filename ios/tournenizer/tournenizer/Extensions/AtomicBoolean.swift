//
//  AtomicBoolean.swift
//  tournenizer
//
//  Created by Ankush Rayabhari on 3/10/18.
//  Copyright © 2018 Ankush Rayabhari. All rights reserved.
//

import Foundation

struct AtomicBoolean {
    private var semaphore = DispatchSemaphore(value: 1);
    private var b: Bool = false;
    var value: Bool  {
        get {
            semaphore.wait();
            let tmp = b;
            semaphore.signal();
            return tmp;
        }
        set {
            semaphore.wait();
            b = newValue;
            semaphore.signal();
        }
    }

}
