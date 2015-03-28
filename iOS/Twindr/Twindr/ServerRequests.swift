//
//  ServerRequests.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-17.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

let serverURI = "http://localhost:9000" //configHelper.getPlistKey("TwindrURL")

let ACCEPTED = "ACCEPTED"
let REJECTED = "REJECTED"

func getList<T: JSONDeserializable>(dummy: T, method: Method, uri: String)(token: String, callback: ([T]?)->Void) {
    var req = NSMutableURLRequest(URL: NSURL(string: serverURI + uri)!)
    req.HTTPMethod = method.rawValue
    req.setValue(NSString(CString: token, encoding: NSUTF8StringEncoding), forHTTPHeaderField: "X-Auth-Token")

    request(req)
        .response { (request, response, data, error) in
            if error == nil && data != nil && response?.statusCode == 200 {
                let json = JSON(data!)
                if let list = json.array {
                    var res:[T]? = []
                    
                    for( index: String, subJson: JSON) in json {
                        if let obj = T(json: subJson) {
                            res?.append(obj)
                        }
                    }
                    
                    callback(res)
                } else {
                    callback(nil)
                }
            } else {
                callback(nil)
            }
        }
}

func respondToMatch(response: String)(token: String, username: String) {
    var req = NSMutableURLRequest(URL: NSURL(string: serverURI + "/m/processMatchResponse")!)
    let resp = PotentialMatchResponse(username: username, status: response)
    req.HTTPMethod = "POST"
    req.setValue("application/json", forHTTPHeaderField: "Content-Type")
    req.setValue(NSString(CString: token, encoding: NSUTF8StringEncoding), forHTTPHeaderField: "X-Auth-Token")
    req.HTTPBody = (resp.toJson() as NSString).dataUsingEncoding(NSUTF8StringEncoding)
    request(req)
}

func getBusinessObject<T: JSONDeserializable>(dummy: T, method: Method, uri: String)(token: String, callback: (T?)->Void) {
    var req = NSMutableURLRequest(URL: NSURL(string: serverURI + uri)!)
    req.HTTPMethod = "GET"
    req.setValue(NSString(CString: token, encoding: NSUTF8StringEncoding), forHTTPHeaderField: "X-Auth-Token")
    println(req)
    request(req)
        .response{ (request, response, data, error) in
            if error == nil && data != nil && response?.statusCode == 200 {
                let json = JSON(data!)
                let res = T(json: json)
                callback(res)
            } else {
                println(error)
                println(response)
                println(data?.string)
                callback(nil)
            }
    }
    
}

func sendBusinessObject<T: JSONSerializable>(dummy: T, uri: String)(obj: T, token: String) {
    var req = NSMutableURLRequest(URL: NSURL(string: serverURI + uri)!)
    req.HTTPMethod = "POST"
    req.setValue("application/json", forHTTPHeaderField: "Content-Type")
    req.setValue(NSString(CString: token, encoding: NSUTF8StringEncoding), forHTTPHeaderField: "X-Auth-Token")
    req.HTTPBody = (obj.toJson()).dataUsingEncoding(NSUTF8StringEncoding)
    request(req)
}

func unmatch(token: String, match: String) {
    var req = NSMutableURLRequest(URL: NSURL(string: serverURI + "/m/unmatch")!)
    req.HTTPMethod = "POST"
    req.setValue("text/plain", forHTTPHeaderField: "Content-Type")
    req.setValue(NSString(CString: token, encoding: NSUTF8StringEncoding), forHTTPHeaderField: "X-Auth-Token")
    req.HTTPBody = match.dataUsingEncoding(NSUTF8StringEncoding)
    request(req)
}

func logout(token: String) {
    var req = NSMutableURLRequest(URL: NSURL(string: serverURI + "/m/logout")!)
    req.setValue(NSString(CString: token, encoding: NSUTF8StringEncoding), forHTTPHeaderField: "X-Auth-Token")
    req.HTTPMethod = "GET"
    request(req)
}

class Curried {
    let register = sendBusinessObject(Registration(), "/m/registerUser")
    let updateRegistration = sendBusinessObject(UpdateRegistration(), "/m/registerUser")
    let getMatches = getList(PreparedMatch(), Method.GET, "/m/matches")
    let getPotentialMatches = getList(PreparedPotentialMatch(), Method.GET, "/m/potentialMatches")
    let getProfileInformation = getBusinessObject(UpdateRegistration(), Method.GET, "/m/getProfileInformation")
    let reject = respondToMatch(REJECTED)
    let accept = respondToMatch(ACCEPTED)

}




