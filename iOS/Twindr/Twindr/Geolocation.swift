//
//  Geolocation.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-03-16.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import CoreLocation

var location: CLLocation!

class Geolocation: NSObject, CLLocationManagerDelegate {
    let locationManager = CLLocationManager()

    // Initialize locationManager to poll geolocation
    func getLocation() {
        
        if (locationManager.respondsToSelector(Selector("requestWhenInUseAuthorization"))) {
            locationManager.requestWhenInUseAuthorization()
        }
        
        if(isLocatingAllowed()) {
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.startUpdatingLocation()
        } else {
            // To the north pole they go!
            location = setLocation(90.0, longitude: 0.0)
            println("latitude: \(location.coordinate.latitude)")
            println("longitude: \(location.coordinate.longitude)")
        }
        
        // Send location to server
    }
    
    // Set and return the location
    func setLocation(latitude: CLLocationDegrees, longitude: CLLocationDegrees) -> CLLocation {
        return CLLocation(latitude:latitude, longitude:longitude)
    }

    // If false, we'll send them to the North Pole!
    func isLocatingAllowed() -> Bool {
        if(!CLLocationManager.locationServicesEnabled()) {
            return false
        } else if(CLLocationManager.authorizationStatus() == CLAuthorizationStatus.Denied) {
            return false
        }
        
        return true
    }
    
    // Delegate function
    func locationManager(manager: CLLocationManager, didUpdateLocations locations: [AnyObject]) {
        if(locations.count > 0 && location == nil) {
            location = (locations.last as CLLocation)
            println("latitude: \(location.coordinate.latitude)")
            println("longitude: \(location.coordinate.longitude)")
            locationManager.stopUpdatingLocation()
        }
    }
    
    // Delegate function
    func locationManager(manager: CLLocationManager, didFailWithError error: NSError) {
        println(error)
    }
}
