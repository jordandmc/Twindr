//
//  LoginViewController.swift
//  Twindr
//
//  Created by Jordan on 2015-02-28.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit
import TwitterKit

class LoginViewController: ViewController {
    
    override func viewDidLoad() {
        println("this has been loaded")
        super.viewDidLoad()
        let logInButton = TWTRLogInButton(logInCompletion:
            { (session, error) in
                if (session != nil) {
                    println("signed in as \(session.userName)");
                    
                } else {
                    println("error: \(error.localizedDescription)");
                }
        })
        logInButton.center = self.view.center
        self.view.addSubview(logInButton)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}