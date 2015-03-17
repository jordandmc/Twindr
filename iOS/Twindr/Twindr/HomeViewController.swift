//
//  HomeViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-02-12.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit
import TwitterKit

class HomeViewController: ViewController {
    
    var geolocationHelper = Geolocation()
    
    @IBAction func Logout(sender: UIButton) {
        Logout()
        user = ""
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        self.title = user
        
        // Gets the coordinates of the user
        geolocationHelper.getLocation()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func Logout(){
        Twitter.sharedInstance().logOut()
    }
}
