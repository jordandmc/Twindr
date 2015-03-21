//
//  JSONSerializable.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-17.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

protocol JSONSerializable {
    func toJson() -> String
}

protocol JSONDeserializable {
    init?(json: JSON)
}