//
//  TwindrTests.swift
//  TwindrTests
//
//  Created by Evan Spearman on 2015-01-23.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit
import XCTest
import TwitterKit

class TwindrTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testLogout(){
        let homeController = HomeViewController()
        homeController.Logout("")
        XCTAssertNil(Twitter.sharedInstance().session(), "Error: logging out")
    }
    
}
