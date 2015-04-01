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
    
    convenience init?(json: JSON) {
        switch(json["matchID"].string, json["username"].string, json["sex"].string) {
        case let (.Some(matchID), .Some(username), .Some(sex)):
            self.init(matchID: matchID, username: username, sex: sex, dateOfBirth: DateHelper.convertToDate(json["dateOfBirth"].description) ?? NSDate())
        default:
            self.init(matchID: "", username: "", sex: "", dateOfBirth: NSDate())
            return nil
        }
    }
    
    convenience init() {
        self.init(matchID: "", username: "", sex: "", dateOfBirth: NSDate())
    }
    
    private init(matchID: String, username: String, sex: String, dateOfBirth: NSDate) {
        self.matchID =  matchID
        self.username = username
        self.sex = sex
        self.dateOfBirth = dateOfBirth
    }

}