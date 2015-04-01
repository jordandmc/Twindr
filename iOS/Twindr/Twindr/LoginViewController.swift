//
//  LoginViewController.swift
//  Twindr
//
//  Created by Jordan on 2015-02-28.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit
import TwitterKit

var xAuthToken: String?

class LoginViewController: ViewController {
    let twitterTag = 666
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let loginButton = TWTRLogInButton(logInCompletion: loginDelegate)
        loginButton.tag = twitterTag
        centerButton(loginButton)
        self.view.addSubview(loginButton)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func centerButton(button: UIButton){
        let screenSize: CGRect = UIScreen.mainScreen().bounds
        let screenWidth: CGFloat = screenSize.width
        let screenHeight: CGFloat = screenSize.height
        
        if (floor(NSFoundationVersionNumber) <= NSFoundationVersionNumber_iOS_7_1) {
            if self.interfaceOrientation.isPortrait {
                button.center = self.view.center
            }
            else {
                button.frame.origin = CGPoint(x: (screenHeight/2)-(button.frame.width/2), y: screenWidth/2)
            }
        }
        else {
            button.center = self.view.center
        }
    }
    
    override func didRotateFromInterfaceOrientation(fromInterfaceOrientation: UIInterfaceOrientation) {
        var loginButton = self.view.viewWithTag(twitterTag) as? UIButton
        if loginButton != nil {
            centerButton(loginButton!)
        }
    }
    
    func loginDelegate(session: TWTRSession!, error: NSError!){
        if session != nil {
            user = session.userName
            println("authtoken: \(session.authToken)")
            println("authTokenSecret: \(session.authTokenSecret)")
            sendLogin(session)
        }
    }
    
    func sendLogin(session: TWTRSession){       
        let accessToken:RequestToken = RequestToken(token: session.authToken, secret: session.authTokenSecret)
        let req = NSMutableURLRequest(URL: NSURL(string: serverURI + "/m/login")!)
        
        req.HTTPMethod = "POST"
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.HTTPBody = accessToken.toJson().dataUsingEncoding(NSUTF8StringEncoding)
        request(req).responseJSON(responseHandler)
    }
    
    private func responseHandler(request: NSURLRequest, response: NSHTTPURLResponse?, data: AnyObject?, error: NSError?){
        println(request.URLString)
        if(error == nil && response != nil && data != nil){
            if response!.statusCode == 200 {
                let loginResponse = LoginResponse(json: JSON(data!))
                
                if loginResponse != nil {
                    xAuthToken = loginResponse!.xAuthToken
                    println(xAuthToken)
                    if loginResponse!.hasRegistered {
                        let navigationController = self.storyboard?.instantiateViewControllerWithIdentifier("StartNav") as UINavigationController
                        self.presentViewController(navigationController, animated: true, completion: nil)
                    } else {
                        let registrationController = self.storyboard?.instantiateViewControllerWithIdentifier("Settings") as SettingsViewController
                        registrationController.isRegistration = true
                        self.presentViewController(registrationController, animated: true, completion: nil)
                    }
                    
                } else {
                    displayLoginFailure()
                }
            }
        } else {
            displayLoginFailure()
        }
    }
    
    private func displayLoginFailure(){
        if objc_getClass("UIAlertController") != nil {
            let alertController = UIAlertController(title: "Error", message: "Unable to connect to service.\nPlease try again later.", preferredStyle: UIAlertControllerStyle.Alert)
            alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.Default,handler: nil))
            self.presentViewController(alertController, animated: true, completion: nil)
        } else {
            let alert = UIAlertView()
            alert.title = "Error"
            alert.message = "Unable to connect to service.\nPlease try again later."
            alert.addButtonWithTitle("OK")
            alert.show()
        }
    }
    
}