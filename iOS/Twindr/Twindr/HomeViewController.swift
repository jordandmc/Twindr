//
//  HomeViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-02-12.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit
import TwitterKit

var navigatedThroughButton: Bool = false

class HomeViewController: ViewController {
    
    var geolocation = Geolocation()
    
    @IBAction func Logout(sender: UIButton) {
        Logout(xAuthToken)
    }
    
    @IBAction func ConversationButtonClicked() {
        navigatedThroughButton = true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        self.title = user
        
        // Gets the coordinates of the user
        geolocation.getLocation()
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        let value = UIInterfaceOrientation.Portrait.rawValue
        UIDevice.currentDevice().setValue(value, forKey: "orientation")
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func Logout(id:String?){
        if(id != nil){
            logout(id!)
        }
        user = ""
        Twitter.sharedInstance().logOut()
    }
    
    override func supportedInterfaceOrientations() -> Int {
        return Int(UIInterfaceOrientationMask.Portrait.rawValue)
    }
}
