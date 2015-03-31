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
    
    convenience init?(json: JSON) {
        switch(json["matchID"].string, json["sender"].string, json["message"].string) {
        case let (.Some(matchID), .Some(sender), .Some(message)):
            self.init(matchID: matchID, sender: sender, message: message, dateTime: DateHelper.convertToDate(json["dateTime"].description) ?? NSDate())
        default:
            self.init(matchID: "", sender: "", message: "", dateTime: NSDate())
            return nil
        }
    }
    
    convenience init() {
        self.init(matchID: "", sender: "", message: "", dateTime: NSDate())
    }
    
    init(matchID: String, sender: String, message: String, dateTime: NSDate) {
        self.matchID = matchID
        self.sender = sender
        self.message = message
        self.dateTime = dateTime
    }
    
    func toJson() -> String {
        let json: JSON = ["_id": "", "matchID": matchID, "sender": sender, "message": message, "dateTime": dateTime.description]
        return json.description
    }
}