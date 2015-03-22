//
//  NetworkHelper.swift
//  Twindr
//
//  Created by Jordan on 2015-03-16.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import TwitterKit

class NetworkHelper {
    
    func post(params : Dictionary<String, String>, url : String) -> Bool {
        var result = true
        /*var request = NSMutableURLRequest(URL: NSURL(string: url)!)
        var session = NSURLSession.sharedSession()
        var err: NSError?
        
        request.HTTPMethod = "POST"
        request.HTTPBody = NSJSONSerialization.dataWithJSONObject(params, options: nil, error: &err)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.addValue("application/json", forHTTPHeaderField: "Accept")
        
        var task = session.dataTaskWithRequest(request, completionHandler: {data, response, error -> Void in
            println("Response: \(response)")
            var strData = NSString(data: data, encoding: NSUTF8StringEncoding)
            println("Body: \(strData)")
            var err: NSError?
            var json = NSJSONSerialization.JSONObjectWithData(data, options: .MutableLeaves, error: &err) as? NSDictionary
            
            // Did the JSONObjectWithData constructor return an error? If so, log the error to the console
            if(err != nil) {
                println(err!.localizedDescription)
                let jsonStr = NSString(data: data, encoding: NSUTF8StringEncoding)
                println("Error could not parse JSON: '\(jsonStr)'")
            }
            else {
                // The JSONObjectWithData constructor didn't return an error. But, we should still
                // check and make sure that json has a value using optional binding.
                if let parseJSON = json {
                    // Okay, the parsedJSON is here, let's get the value for 'success' out of it
                    var success = parseJSON["success"] as? Int
                    println("Succes: \(success)")
                    result = true
                }
                else {
                    // Woa, okay the json object was nil, something went worng. Maybe the server isn't running?
                    let jsonStr = NSString(data: data, encoding: NSUTF8StringEncoding)
                    println("Error could not parse JSON: \(jsonStr)")
                }
            }
        })
        task.resume()*/
        return result
    }
    
    func sendLogout(token: String){
        var req = NSMutableURLRequest()
        req.HTTPMethod = "POST"
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.setValue(token, forHTTPHeaderField: "X-Auth-Token")
        request(req)
    }
    
    func oauthEcho() -> Bool {
        var result = false
        let oauthSigning = TWTROAuthSigning(
            authConfig: Twitter.sharedInstance().authConfig,
            authSession: Twitter.sharedInstance().session())
        let authHeaders = oauthSigning.OAuthEchoHeadersToVerifyCredentials()
        let req = NSMutableURLRequest(URL: NSURL(string: "http://192.168.0.107:9000/m/verify_credentials")!)
        req.allHTTPHeaderFields = authHeaders
        let response = request(req).response
        if(response != nil){
            if(response!.statusCode == 200){
                result = true
            }
        }
        
        return result
    }
    
    func getPlistKey(key: String) -> String {
        var dict: NSDictionary?
        if let path = NSBundle.mainBundle().pathForResource("Info", ofType: "plist") {
            dict = NSDictionary(contentsOfFile: path)
        }
        var value:String = dict?.valueForKey(key) as String
        return value
    }
}