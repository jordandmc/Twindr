//
//  RequestToken.swift
//  Twindr
//
//  Created by Jordan on 2015-03-23.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

final class RequestToken: JSONSerializable {
    
    let token: String
    let secret: String
    
    init(token: String, secret: String) {
        self.token = token
        self.secret = secret
    }
    
    func toJson() -> String {
        let json: JSON = ["token": token, "secret": secret]
        return json.description
    }
}
