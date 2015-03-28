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
    let dateTime: String
    
    init?(json: JSON) {
        switch(json["matchID"].string, json["sender"].string, json["message"].string, json["dateTime"].string) {
        case let (.Some(matchID), .Some(sender), .Some(message), .Some(dateTime)):
            self.matchID = matchID
            self.sender = sender
            self.message = message
            self.dateTime = dateTime
        default:
            self.matchID = ""
            self.sender = ""
            self.message = ""
            self.dateTime = ""
            return nil
        }
    }
    
    init() {
        self.matchID = ""
        self.sender = ""
        self.message = ""
        self.dateTime = ""
    }
    
    init(matchID: String, sender: String, message: String, dateTime: String) {
        self.matchID = matchID
        self.sender = sender
        self.message = message
        self.dateTime = dateTime
    }
    
    func toJson() -> String {
        let json: JSON = ["matchID": matchID, "sender": sender, "message": message, "dateTime": dateTime]
        return json.description
    }
}