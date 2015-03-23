//
//  NetworkHelper.swift
//  Twindr
//
//  Created by Jordan on 2015-03-16.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import TwitterKit

class NetworkHelper {
    
    func sendLogout(token: String){
        var req = NSMutableURLRequest()
        req.HTTPMethod = "POST"
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.setValue(token, forHTTPHeaderField: "X-Auth-Token")
        request(req)
    }
    
    func getPlistKey(key: String) -> String {
        var dict: NSDictionary?
        if let path = NSBundle.mainBundle().pathForResource("Info", ofType: "plist") {
            dict = NSDictionary(contentsOfFile: path)
        }
        var value:String = dict?.valueForKey(key) as String
        return value
    }
}