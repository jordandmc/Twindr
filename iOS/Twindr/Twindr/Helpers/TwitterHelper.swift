//
//  TwitterHelper.swift
//  Twindr
//
//  Created by Jordan on 2015-03-30.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import TwitterKit

class TwitterHelper {
    //class properties not yet supported
    //class let twitterAPIDomain = "https://api.twitter.com/1.1"
    
    class func sendFollow(screenName: String) {
        let twitterAPIDomain = "https://api.twitter.com/1.1"
        let followEndpoint = "/friendships/create.json"
        let params = ["screen_name": screenName]
        
        sendRequestToTwitter(followEndpoint, htmlMethod: "POST", htmlParams: params)
    }
    
    class func sendUnfollow(screenName: String){
        let twitterAPIDomain = "https://api.twitter.com/1.1"
        let unfollowEndpoint = "/friendships/destroy.json"
        let params = ["screen_name": screenName]
        
        sendRequestToTwitter(unfollowEndpoint, htmlMethod: "POST", htmlParams: params)
    }
    
    class func isFollowing(currentUser:String, othearUser: String) -> Bool {
        
        return false
    }
    
    class func sendRequestToTwitter(endPoint: String, htmlMethod: String, htmlParams: NSDictionary){
        let twitterAPIDomain = "https://api.twitter.com/1.1"
        var clientError : NSError?
        
        let request = Twitter.sharedInstance().APIClient.URLRequestWithMethod(
            htmlMethod, URL: twitterAPIDomain + endPoint, parameters: htmlParams, error: &clientError)
        
        if request != nil {
            Twitter.sharedInstance().APIClient.sendTwitterRequest(request) {
                (response, data, connectionError) -> Void in
                if (connectionError == nil) {
                    var jsonError : NSError?
                    let json : AnyObject? = NSJSONSerialization.JSONObjectWithData(data, options: nil, error: &jsonError)
                    //we could extract user information from json
                }
                else {
                    println("Error: \(connectionError)")
                }
            }
        }
        else {
            println("Error: \(clientError)")
        }
    }
}
