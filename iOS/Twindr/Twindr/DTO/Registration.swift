//
//  Registration.swift
//  Twindr
//
//  Created by Tim Sands on 2015-03-25.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

final class Registration: JSONSerializable {
    
    let sex: String
    let dateOfBirth: String
    let interests: String
    
    init(sex: String, dateOfBirth: String, interests: String) {
        self.sex = sex
        self.dateOfBirth = dateOfBirth
        self.interests = interests
    }
    
    func toJson() -> String {
        let json: JSON = ["sex": sex, "dateOfBirth": dateOfBirth, "interests": interests]
        return json.description
    }
}