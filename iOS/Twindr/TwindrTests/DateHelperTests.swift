//
//  DateHelperTests.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-03-30.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import XCTest

class DateHelperTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testEmpty() {
        let date = DateHelper.convertToDate("")
        XCTAssertNil(date, "This is supposed to be nil")
    }
    
    func testInvalidNumbers() {
        let date = DateHelper.convertToDate("1234")
        XCTAssertNil(date, "This is supposed to be nil")
    }
    
    func testInvalidCharacters() {
        let date = DateHelper.convertToDate("acbd")
        XCTAssertNil(date, "This is supposed to be nil")
    }
    
    func testFutureDate() {
        let date = DateHelper.convertToDate("9999/12/1")
        XCTAssert(date?.description == "9999-12-01 06:00:00 +0000", "These should be equal")
    }
    
    func testPastDate() {
        let date = DateHelper.convertToDate("1234/12/1")
        XCTAssert(date?.description == "1234-12-01 06:28:36 +0000", "These should be equal")
    }
    
    func testInvalidMonth() {
        let date = DateHelper.convertToDate("1234/13/1")
        XCTAssertNil(date, "This is supposed to be nil")
    }
    
    func testInvalidDay() {
        let date = DateHelper.convertToDate("1234/12/41")
        XCTAssertNil(date, "This is supposed to be nil")
    }
    
}
