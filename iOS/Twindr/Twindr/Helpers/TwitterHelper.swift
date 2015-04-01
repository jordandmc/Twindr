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
        
        sendRequestToTwitter(followEndpoint, htmlMethod: "POST", htmlParams: params)
    }
    
    class func sendUnfollow(screenName: String){
        let unfollowEndpoint = "/friendships/destroy.json"
        let params = ["screen_name": screenName]
        
        sendRequestToTwitter(unfollowEndpoint, htmlMethod: "POST", htmlParams: params)
    }
    
    class func getUsersFollowed(users: [String]) -> [Bool] {
        let checkFollowingEndpoint = "/friendships/lookup.json"
        let params = ["screen_name": ",".join(users)]
        var result = [false]
        var index = 0
        
        if let jsonArray:[JSON]? = sendRequestToTwitter(checkFollowingEndpoint, htmlMethod: "GET", htmlParams: params)?.array {
            for user:JSON in jsonArray! {
                if let followArray = user["connections"].array {
                    result.append(contains(followArray, "following"))
                }
                index++
            }
        }
        
        return result
    }
    
    class func sendRequestToTwitter(endPoint: String, htmlMethod: String, htmlParams: NSDictionary) -> JSON? {
        let twitterAPIDomain = "https://api.twitter.com/1.1"
        var clientError : NSError?
        var json:JSON? = nil
        
        let request = Twitter.sharedInstance().APIClient.URLRequestWithMethod(
            htmlMethod, URL: twitterAPIDomain + endPoint, parameters: htmlParams, error: &clientError)

        if request != nil {
            Twitter.sharedInstance().APIClient.sendTwitterRequest(request) {
                (response, data, connectionError) -> Void in
                if (connectionError == nil) {
                    var jsonError : NSError?
                    
                    json = JSON(data: data, options: nil, error: &jsonError)
                    println(json)
                }
                else {
                    println("Error: \(connectionError)")
                }
            }
        }
        else {
            println("Error: \(clientError)")
        }
        
        return json
    }
}
