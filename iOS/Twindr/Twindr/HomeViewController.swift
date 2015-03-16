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
        Twitter.sharedInstance().logOut()
        user = ""
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        self.title = user
        
        // Initialize locationManager to poll geolocation
        self.locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
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
    
    func locationManager(manager: CLLocationManager, didFailWithError error: NSError) {
        println(error)
    }
    
    
}


