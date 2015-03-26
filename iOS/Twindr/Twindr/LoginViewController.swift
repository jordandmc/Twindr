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
    
    func loginDelegate(session: TWTRSession!, error: NSError!){
        if (session != nil) {
            user = session.userName
            println("authtoken: \(session.authToken)")
            println("authTokenSecret: \(session.authTokenSecret)")
            sendLogin(session)
        }
        else {
            println("error: \(error.localizedDescription)")
        }
    }
    
    func sendLogin(session: TWTRSession){
        var host:String = "http://192.168.0.107:9000/"
        //var host:String = helper.getPlistKey("TwindrURL")
        
        let accessToken:RequestToken = RequestToken(token: session.authToken, secret: session.authTokenSecret)
        let req = NSMutableURLRequest(URL: NSURL(string: host + "m/login")!)
        
        req.HTTPMethod = "POST"
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.HTTPBody = accessToken.toJson().dataUsingEncoding(NSUTF8StringEncoding)
        request(req).response(responseHandler)
    }
    
    private func responseHandler(request: NSURLRequest, response: NSHTTPURLResponse?, responseObj: AnyObject?, error: NSError?){
        if(response != nil){
            if(response!.statusCode == 200 && responseObj != nil){
                
                //JSON.rawData(responseObj?)
                let navigationController = self.storyboard?.instantiateViewControllerWithIdentifier("StartNav") as UINavigationController
                self.presentViewController(navigationController, animated: true, completion: nil)
            }
            else{
                if(error != nil){
                    println("Error: \(error?.localizedDescription)")
                }
                println("html error: \(response!.statusCode)")
                let alertController = UIAlertController(title: "Error", message: "Unable to connect to service.\nPlease try again later.", preferredStyle: UIAlertControllerStyle.Alert)
                alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.Default,handler: nil))
                self.presentViewController(alertController, animated: true, completion: nil)
            }
        }
    }
    
}