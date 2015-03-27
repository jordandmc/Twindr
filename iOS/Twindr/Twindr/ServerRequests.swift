//
//  ServerRequests.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-17.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

let serverURI = "ec2-54-149-24-39.us-west-2.compute.amazonaws.com:9000"

let ACCEPTED = "ACCEPTED"
let REJECTED = "REJECTED"

func getList<T: JSONDeserializable>(dummy: T, method: Method, uri: String)(token: String) -> [T]? {
    var res: [T]? = nil
    
    request(method, serverURI + uri, parameters: ["X-Auth-Token": token])
        .responseJSON { (request, response, data, error) in
            if error == nil && data != nil {
                let json = JSON(data!)
                if let list = json.array {
                    res = []
                    
                    for( index: String, subJson: JSON) in json {
                        if let obj = T(json: subJson) {
                            res?.append(obj)
                        }
                    }
                }
            }
        }
    
    return res
}

let getMatches = getList(PreparedMatch(), Method.GET, "/m/matches")
let getPotentialMatches = getList(PreparedPotentialMatch(), Method.GET, "/m/potentialMatches")

func respondToMatch(response: String)(token: String, username: String) {
    var req = NSMutableURLRequest()
    let resp = PotentialMatchResponse(username: username, status: response)
    req.HTTPMethod = "POST"
    req.setValue("application/json", forHTTPHeaderField: "Content-Type")
    req.setValue(token, forHTTPHeaderField: "X-Auth-Token")
    req.HTTPBody = (resp.toJson() as NSString).dataUsingEncoding(NSUTF8StringEncoding)
    request(req)
}

let reject = respondToMatch(REJECTED)
let accept = respondToMatch(ACCEPTED)

func getBusinessObject<T: JSONDeserializable>(dummy: T, method: Method, uri: String)(token: String) -> T? {
    var res: T? = nil
    
    request(method, serverURI + uri, parameters: ["X-Auth-Token": token])
        .responseJSON { (request, response, data, error) in
            if error == nil && data != nil {
                let json = JSON(data!)
                res = T(json: json)
            }
    }
    
    return res
}

let getProfileInformation = getBusinessObject(UpdateRegistration(), Method.GET, "/m/getProfileInformation")

func sendBusinessObject<T: JSONSerializable>(dummy: T, uri: String)(obj: T, token: String) {
    var req = NSMutableURLRequest(URL: NSURL(string: serverURI + uri)!)
    req.HTTPMethod = "POST"
    req.setValue("application/json", forHTTPHeaderField: "Content-Type")
    req.setValue(token, forHTTPHeaderField: "X-Auth-Token")
    req.HTTPBody = (obj.toJson()).dataUsingEncoding(NSUTF8StringEncoding)
    request(req)
}

let register = sendBusinessObject(Registration(), "/m/registerUser")
let updateRegistration = sendBusinessObject(UpdateRegistration(), "/m/registerUser")


func unmatch(token: String, match: String) {
    var req = NSMutableURLRequest(URL: NSURL(string: serverURI + "/m/unmatch")!)
    req.HTTPMethod = "POST"
    req.setValue("text/plain", forHTTPHeaderField: "Content-Type")
    req.setValue(token, forHTTPHeaderField: "X-Auth-Token")
    req.HTTPBody = match.dataUsingEncoding(NSUTF8StringEncoding)
    request(req)
}

func logout(token: String) {
    request(.GET, serverURI + "/m/logout", parameters: ["X-Auth-Token": token])
}




