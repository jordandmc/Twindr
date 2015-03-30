//
//  NetworkHelper.swift
//  Twindr
//
//  Created by Jordan on 2015-03-16.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

class ConfigHelper {
    class func getPlistKey(key: String) -> String {
        var dict: NSDictionary?
        var result = ""
        if let path = NSBundle.mainBundle().pathForResource("Info", ofType: "plist") {
            dict = NSDictionary(contentsOfFile: path)
        }
        if let value: AnyObject = dict?.valueForKey(key){
            result = value as String
        }
        return result
    }
}