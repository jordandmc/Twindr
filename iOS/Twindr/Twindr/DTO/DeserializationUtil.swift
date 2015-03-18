//
//  DeserializationUtil.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-17.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

func deserializeStringList(json: JSON) -> [String]? {
    var res: [String]? = nil
    if let list = json.array {
        res = []
        
        for( index: String, subJson: JSON) in json {
            if let str = subJson.string {
                res?.append(str)
            }
        }
    }
    
    return res
}