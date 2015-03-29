//
//  MatchMessage.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

final class MatchMessage: JSONSerializable, JSONDeserializable {
    let matchID: String
    let sender: String
    let message: String
    let dateTime: NSDate!
    
    init?(json: JSON) {
        switch(json["matchID"].string, json["sender"].string, json["message"].string) {
        case let (.Some(matchID), .Some(sender), .Some(message)):
            self.matchID = matchID
            self.sender = sender
            self.message = message
            self.dateTime = DateHelper.convertToDate(json["dateTime"].description)
        default:
            self.matchID = ""
            self.sender = ""
            self.message = ""
            self.dateTime = NSDate()
            return nil
        }
    }
    
    init() {
        self.matchID = ""
        self.sender = ""
        self.message = ""
        self.dateTime = NSDate()
    }
    
    init(matchID: String, sender: String, message: String, dateTime: String) {
        self.matchID = matchID
        self.sender = sender
        self.message = message
        self.dateTime = DateHelper.convertToDate(dateTime)
    }
    
    func toJson() -> String {
        let json: JSON = ["matchID": matchID, "sender": sender, "message": message, "dateTime": dateTime]
        return json.description
    }
}