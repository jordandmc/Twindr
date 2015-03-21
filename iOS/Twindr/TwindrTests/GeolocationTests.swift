//
//  GeolocationTests.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-03-16.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit
import XCTest
import CoreLocation

class GeolocationTests: XCTestCase {
    
    var geolocation = Geolocation()

    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testSetLocation() {
        location = geolocation.setLocation(90.0, longitude: 0.0)
        assert(location.coordinate.latitude == 90.0)
        assert(location.coordinate.longitude == 0.0)
    }
    
    func testIsLocatingAllowed() {
        if(CLLocationManager.locationServicesEnabled() == false) {
            assert(geolocation.isLocatingAllowed() == false)
        } else if(CLLocationManager.authorizationStatus() == CLAuthorizationStatus.Denied) {
            assert(geolocation.isLocatingAllowed() == false)
        } else {
            assert(geolocation.isLocatingAllowed())
        }
    }
}
