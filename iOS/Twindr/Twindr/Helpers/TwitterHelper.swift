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
        let followEndpoint = "/friendships/create.json"
        let params = ["screen_name": screenName]
        
        sendRequestToTwitter(followEndpoint, htmlMethod: "POST", htmlParams: params, onCompletion: nil)
    }
    
    class func sendUnfollow(screenName: String){
        let unfollowEndpoint = "/friendships/destroy.json"
        let params = ["screen_name": screenName]
        
        sendRequestToTwitter(unfollowEndpoint, htmlMethod: "POST", htmlParams: params, onCompletion: nil)
    }
    
    class func getUsersFollowed(users: [String], onCompletion: ([Bool]) -> Void) {
        let checkFollowingEndpoint = "/friendships/lookup.json"
        let params = ["screen_name": ",".join(users)]
        var result:[Bool] = []
        var index = 0
        
        sendRequestToTwitter(checkFollowingEndpoint, htmlMethod: "GET", htmlParams: params){
            (json) in
            if let jsonArray:[JSON]? = json.array {
                for user:JSON in jsonArray! {
                    if let followArray = user["connections"].array {
                        result.append(contains(followArray, "following"))
                    }
                    index++
                }
                onCompletion(result)
            }
        }
    }
    
    private class func sendRequestToTwitter(endPoint: String, htmlMethod: String, htmlParams: NSDictionary, onCompletion: ((JSON) -> Void)?) {
        let twitterAPIDomain = "https://api.twitter.com/1.1"
        var clientError : NSError?
        
        let request = Twitter.sharedInstance().APIClient.URLRequestWithMethod(
            htmlMethod, URL: twitterAPIDomain + endPoint, parameters: htmlParams, error: &clientError)

        if request != nil {
            Twitter.sharedInstance().APIClient.sendTwitterRequest(request) {
                (response, data, connectionError) -> Void in
                if (connectionError == nil) {
                    var jsonError : NSError?
                    let json = JSON(data: data, options: nil, error: &jsonError)
                    
                    if let hasClosure = onCompletion {
                        onCompletion!(json)
                    }
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
