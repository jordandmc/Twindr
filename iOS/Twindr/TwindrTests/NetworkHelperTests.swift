//
//  NetworkHelperTests.swift
//  Twindr
//
//  Created by Jordan on 2015-03-16.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

import UIKit
import XCTest

class NetworkHelperTests: XCTestCase {
    let helper:NetworkHelper = NetworkHelper()
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func CheckReturnedValueInPlist(){
        var value:String = helper.getPlistKey("TestCaseKey") as String
        XCTAssertEqual(value, "TestCaseValue", "Error: Unexpected Result.")
    }
    
    func CheckForNonExistantKeyValue(){
        var value:String = helper.getPlistKey("NonExistantKey") as String
        XCTAssertNil(value, "Error: Unexpected Result.")
    }

}
