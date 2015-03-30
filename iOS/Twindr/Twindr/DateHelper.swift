//
//  DateHelper.swift
//  Twindr
//
//  Created by Jordan on 2015-03-28.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

class DateHelper {
    class func convertToDate(stringDate: String) -> NSDate? {
        var dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "yyyy/MM/dd"
        return dateFormatter.dateFromString(stringDate)
    }
}