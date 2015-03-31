//
//  ViewController.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-01-23.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit
import CoreLocation

// globally defined variables - Only reason we're using these
// is because xcode was not liking us passing variables between
// segues that aren't "presentations" (ie. "Show")
var user:String!
var gender:String!
var dob:String!
var interests:String!
var converseWith: PreparedMatch!

extension UINavigationController {
    public override func supportedInterfaceOrientations() -> Int {
        return visibleViewController.supportedInterfaceOrientations()
    }
    
    public override func shouldAutorotate() -> Bool {
        return visibleViewController.shouldAutorotate()
    }
    
}

class ViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        setupNavigationColour()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setupNavigationColour() {
        self.navigationController?.navigationBar.barTintColor = UIColor(red: CGFloat(39.0/255.0), green: CGFloat(174.0/255.0), blue: CGFloat(96.0/255.0), alpha: CGFloat(1.0))
        self.navigationController?.navigationBar.translucent = false
        self.navigationController?.navigationBar.tintColor = UIColor.whiteColor()
        self.navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.whiteColor()]
    }
}

