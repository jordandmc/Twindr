//
//  MatchesViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-02-12.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit
import CoreLocation

class MatchesViewController: ViewController, CLLocationManagerDelegate {
    
    @IBOutlet weak var userLabel: UILabel!
    @IBOutlet weak var tweet1Label: UILabel!
    @IBOutlet weak var tweet2Label: UILabel!
    @IBOutlet weak var tweet3Label: UILabel!
    @IBOutlet weak var tweet4Label: UILabel!
    @IBOutlet weak var tweet5Label: UILabel!
    
    let locationManager = CLLocationManager()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        self.navigationController?.setToolbarHidden(false, animated: false)
        self.navigationController?.toolbar.barTintColor = UIColor(red: CGFloat(39.0/255.0), green: CGFloat(174.0/255.0), blue: CGFloat(96.0/255.0), alpha: CGFloat(1.0))
        self.navigationController?.toolbar.tintColor = UIColor.whiteColor()
        
        self.locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
        println("Do I get here?")
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // Decline to be matched with the user via button or swipe
    func dismissMatch() {

    }
    
    // Accept the match via button or swipe
    func acceptMatch() {
        
    }
    
    @IBAction func noButton(sender: UIBarButtonItem) {
        dismissMatch()
    }

    @IBAction func yesButton(sender: UIBarButtonItem) {
        acceptMatch()
    }
    
    // Swipe to the right (start left, move right)
    @IBAction func noSwipe(sender: UISwipeGestureRecognizer) {
        dismissMatch()
    }
    
    // Swipe to the left (start right, move left)
    @IBAction func yesSwipe(sender: UISwipeGestureRecognizer) {
        acceptMatch()
    }
    
    func locationManager(manager: CLLocationManager, didUpdateLocations locations: [AnyObject]) {
        println("did update?")
        var location = locations.last as CLLocation
        println(location.coordinate.latitude)
        println(location.coordinate.longitude)
        locationManager.stopUpdatingLocation()
    }
    
    func locationManager(manager: CLLocationManager, didFailWithError error: NSError) {
        locationManager.stopUpdatingLocation()
        print(error)
    }
}
