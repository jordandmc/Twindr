//
//  HomeViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-02-12.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit
import TwitterKit
import CoreLocation

class HomeViewController: ViewController, CLLocationManagerDelegate {
    
    let locationManager = CLLocationManager()
    
    @IBAction func Logout(sender: UIButton) {
        Logout()
        user = ""
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        self.title = user
        
        // Initialize locationManager to poll geolocation
        locationManager.requestWhenInUseAuthorization()
        if(self.isLocatingAllowed()) {
            self.locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.startUpdatingLocation()
        } else {
            // To the north pole they go!
            location = CLLocation(latitude: 90.0, longitude: 0.0)
            println(location.coordinate.latitude)
            println(location.coordinate.longitude)
            
            // Send location to server
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func locationManager(manager: CLLocationManager, didUpdateLocations locations: [AnyObject]) {
        if(locations.count > 0 && location == nil) {
            location = (locations.last as CLLocation)
            println(location.coordinate.latitude)
            println(location.coordinate.longitude)
            
            // Send location to server
            
            locationManager.stopUpdatingLocation()
        }
    }
    
    func Logout(){
        Twitter.sharedInstance().logOut()
    }
    
    func locationManager(manager: CLLocationManager, didFailWithError error: NSError) {
        println(error)
    }
    
    // If false, we'll send them to the North Pole!
    func isLocatingAllowed() -> Bool {
        var allowed = true
        if CLLocationManager.locationServicesEnabled() == false {
            allowed = false
        }
        if CLLocationManager.authorizationStatus() == CLAuthorizationStatus.Denied {
            allowed = false
        }
        return allowed
    }
    
    
}