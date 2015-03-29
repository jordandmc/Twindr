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
    
    init?(json: JSON) {
        switch(json["username"].string, json["tweets"].array, json["sex"].string) {
        case let (.Some(username), .Some(tweets), .Some(sex)):
            self.username = username
            self.sex = sex
            self.dateOfBirth = DateHelper.convertToDate(json["dateOfBirth"].description)
            
            if let tweets = deserializeStringList(json["tweets"]) {
                self.tweets = tweets
            } else {
                self.tweets = []
                return nil
            }
        default:
            self.username = ""
            self.sex = ""
            self.dateOfBirth = NSDate()
            self.tweets = []
            return nil
        }
    }
    
    init() {
        self.username = ""
        self.sex = ""
        self.dateOfBirth = NSDate()
        self.tweets = []
    }
}