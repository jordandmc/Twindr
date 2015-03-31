//
//  PreparedPotentialMatch.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-17.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

final class PreparedPotentialMatch: JSONDeserializable {
    
    let username: String
    let tweets: [String]
    let sex: String
    let dateOfBirth: NSDate!
    
    convenience init?(json: JSON) {
        switch(json["username"].string, json["tweets"].array, json["sex"].string) {
        case let (.Some(username), .Some(tweets), .Some(sex)):
            self.init(username: username, sex: sex, dateOfBirth: DateHelper.convertToDate(json["dateOfBirth"].description) ?? NSDate(), tweets: deserializeStringList(json["tweets"]) ?? [])
        default:
            self.init(username: "", sex: "", dateOfBirth: NSDate(), tweets: [])
            return nil
        }
    }
    
    convenience init() {
        self.init(username: "", sex: "", dateOfBirth: NSDate(), tweets: [])
    }
    
    private init(username: String, sex: String, dateOfBirth: NSDate, tweets: [String]) {
        self.username = username
        self.sex = sex
        self.dateOfBirth = dateOfBirth
        self.tweets = tweets
    }
}