//
//  ViewController.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-01-23.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit
import CoreLocation

// globally defined variables
var user:String!
var gender:String!
var dob:String!
var interests:String!
//var matchedUsers:Array<String>!
var converseWith: String!
var matchedUsers = ["Jordan", "Tim", "Evan", "Morgan", "Caesar", "Brett", "Jord", "Timothy", "Nave","Morg", "Junhyeok", "Bert", "Nadroj", "Mit", "Vane", "Nagrom", "Raseac", "Tterb", "Cole", "Sand", "Spearman", "Epp", "Kim", "Small", "Braico"]

class ViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        self.navigationController?.navigationBar.barTintColor = UIColor(red: CGFloat(39.0/255.0), green: CGFloat(174.0/255.0), blue: CGFloat(96.0/255.0), alpha: CGFloat(1.0))
        self.navigationController?.navigationBar.tintColor = UIColor.whiteColor() 
        self.navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.whiteColor()]
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

