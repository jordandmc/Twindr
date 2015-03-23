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
        var host:String = helper.getPlistKey("TwindrURL")
        
        return helper.post(["authToken":session.authToken, "authTokenSecret":session.authTokenSecret], url:  host + "m/login")
    }
    
    func loginDelegate(session: TWTRSession!, error: NSError!){
        if (session != nil) {
            user = session.userName
            oauthEcho()
        }
        else {
            println("error: \(error.localizedDescription)")
        }
    }
    
    func oauthEcho(){
        let oauthSigning = TWTROAuthSigning(
            authConfig: Twitter.sharedInstance().authConfig,
            authSession: Twitter.sharedInstance().session())
        let authHeaders = oauthSigning.OAuthEchoHeadersToVerifyCredentials()
        let req = NSMutableURLRequest(URL: NSURL(string: "http://192.168.0.107:9000/m/verify_credentials")!)
        req.allHTTPHeaderFields = authHeaders
        request(req).response(responseHandler)
    }
    
    private func responseHandler(request: NSURLRequest, response: NSHTTPURLResponse?, responseObj: AnyObject?, error: NSError?){
        if(response != nil){
            if(response!.statusCode == 200){
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