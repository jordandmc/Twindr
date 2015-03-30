//
//  NetworkHelperTests.swift
//  Twindr
//
//  Created by Jordan on 2015-03-16.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import XCTest

class ConfigHelperTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testReturnedValueInPlist(){
        var value:String = ConfigHelper.getPlistKey("TestCaseKey") as String
        XCTAssertEqual(value, "TestCaseValue", "Error: Unexpected Result.")
    }
    
    func testForNonExistantKeyValue(){
        var value:String = ConfigHelper.getPlistKey("NonExistantKey") as String
        XCTAssertEqual(value, "", "Error: Unexpected Result.")
    }
    
    func testEmptyString() {
        var value:String = ConfigHelper.getPlistKey("NonExistantKey") as String
        XCTAssertEqual(value, "", "Error: This is supposed to be an empty string")
    }
}
