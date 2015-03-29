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
    let dateOfBirth: NSDate!
    let interests: String
    
    init(sex: String, dateOfBirth: String, interests: String) {
        self.sex = sex
        self.dateOfBirth = DateHelper.converToDate(dateOfBirth)
        self.interests = interests
    }
    
    init() {
        self.sex = ""
        self.dateOfBirth = NSDate()
        self.interests = ""
    }
    
    func toJson() -> String {
        let json: JSON = ["sex": sex, "dateOfBirth": dateOfBirth.description, "interests": interests]
        return json.description
    }
}