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
    
    var geolocation = Geolocation()
    
    @IBAction func Logout(sender: UIButton) {
        Logout("f91900b1-d602-490e-9277-161c5a1e8a11")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        self.title = user
        
        // Gets the coordinates of the user
        geolocation.getLocation()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func Logout(id:String){
        let helper = NetworkHelper()
        helper.sendLogout(id)
        user = ""
        Twitter.sharedInstance().logOut()
    }
}
