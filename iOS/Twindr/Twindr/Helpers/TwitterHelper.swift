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
    class func sendFollow(screenName: String) {
        let followEndpoint = "https://api.twitter.com/1.1/friendships/create.json"
        let params = ["screen_name": screenName]
        var clientError : NSError?
        
        let request = Twitter.sharedInstance().APIClient.URLRequestWithMethod(
            "POST", URL: followEndpoint, parameters: params, error: &clientError)
        
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
    
    class func sendUnfollow(screenName: String){
        
    }
    
    class func isFollowing(currentUser:String, otherUser: String) -> Bool {
        return false
    }
}
