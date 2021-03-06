//
//  LoginResponse.swift
//  Twindr
//
//  Created by Jordan on 2015-03-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

final class LoginResponse: JSONDeserializable {
    let xAuthToken: String
    let hasRegistered: Bool
    
    init?(json: JSON) {
        switch(json["xAuthToken"].string, json["hasRegistered"].bool) {
        case let (.Some(xAuthToken), .Some(hasRegistered)):
            self.xAuthToken = xAuthToken
            self.hasRegistered = hasRegistered
        default:
            self.xAuthToken = ""
            self.hasRegistered = false
        }
    }
    
    init(){
        self.xAuthToken = ""
        self.hasRegistered = false
    }
}