//
//  PreparedMatch.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-17.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

final class PreparedMatch: JSONDeserializable {
    
    let matchID: String
    let username: String
    let sex: String
    let dateOfBirth: NSDate!
    
    init?(json: JSON) {
        switch(json["matchID"].string, json["username"].string, json["sex"].string) {
        case let (.Some(matchID), .Some(username), .Some(sex)):
            self.matchID =  matchID
            self.username = username
            self.sex = sex
            self.dateOfBirth = DateHelper.converToDate(json["dateOfBirth"].description)
        default:
            self.matchID = ""
            self.username = ""
            self.sex = ""
            self.dateOfBirth = NSDate()
            return nil
        }
    }
    
    init() {
        self.matchID = ""
        self.username = ""
        self.sex = ""
        self.dateOfBirth = NSDate()
    }

}