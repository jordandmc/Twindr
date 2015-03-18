//
//  PotentialMatchResponse.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-17.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

final class PotentialMatchResponse: JSONSerializable {
    
    let username: String
    let status: String
    
    init(username: String, status: String) {
        self.username = username
        self.status = status
    }
    
    func toJson() -> String {
        let json: JSON = ["username": username, "status": status]
        return json.string!
    }
}