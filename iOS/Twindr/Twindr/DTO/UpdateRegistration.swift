//
//  UpdateRegistration.swift
//  Twindr
//
//  Created by Tim Sands on 2015-03-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

final class UpdateRegistration: JSONSerializable, JSONDeserializable {
    
    let interests: String
    
    init() {
        self.interests = ""
    }
    
    init(interests: String) {
        self.interests = interests
    }
    
    init?(json: JSON) {
        switch(json["interests"].string) {
            case let (.Some(interests)):
                self.interests = interests
            default:
                self.interests = ""
        }
    }
    
    func toJson() -> String {
        let json: JSON = ["interests": interests]
        return json.description
    }
}