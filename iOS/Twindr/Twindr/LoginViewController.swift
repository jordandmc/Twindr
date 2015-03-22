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
    let helper = NetworkHelper()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let logInButton = TWTRLogInButton(logInCompletion: loginDelegate)
        logInButton.center = self.view.center
        self.view.addSubview(logInButton)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func postOauthCredentials(session:TWTRSession) -> Bool {
        let helper = NetworkHelper()
        var host:String = helper.getPlistKey("TwindrURL")
        
        return helper.post(["authToken":session.authToken, "authTokenSecret":session.authTokenSecret], url:  host + "m/login")
    }
    
    func loginDelegate(session: TWTRSession!, error: NSError!){
        if (session != nil) {
            user = session.userName
            println("Logged in as: \(session.userName)")
            println("Oauth token: \(session.authToken)")
            println("Oauth token secret: \(session.authTokenSecret)")
            
            if(helper.oauthEcho()){
                let navigationController = self.storyboard?.instantiateViewControllerWithIdentifier("StartNav") as UINavigationController
                self.presentViewController(navigationController, animated: true, completion: nil)
            }
            else{
                let alertController = UIAlertController(title: "Error", message: "Unable to connect to server.\nPlease try again later.", preferredStyle: UIAlertControllerStyle.Alert)
                alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.Default,handler: nil))
                self.presentViewController(alertController, animated: true, completion: nil)
            }
        }
        else {
            println("error: \(error.localizedDescription)")
        }
    }
    
}