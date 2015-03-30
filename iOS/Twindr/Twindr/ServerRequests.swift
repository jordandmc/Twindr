//
//  ServerRequests.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-17.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

let serverURI =  ConfigHelper.getPlistKey("TwindrURL") // test on "http://localhost:9000"

let ACCEPTED = "ACCEPTED"
let REJECTED = "REJECTED"

func unmatch(token: String, matchTwitterName: String) {
    var req = createURLRequest("/m/unmatch", token, Method.POST, contentType: "text/plain")
    req.HTTPBody = matchTwitterName.dataUsingEncoding(NSUTF8StringEncoding)
    request(req)
}

func logout(token: String) {
    var req = createURLRequest("/m/logout", token, Method.GET)
    request(req)
}

private func getList<T: JSONDeserializable>(dummy: T, method: Method, uri: String)(token: String, callback: ([T]?)->Void) {
    var req = createURLRequest(uri, token, method)
    request(req)
        .responseJSON { (request, response, data, error) in
            if error == nil && data != nil && response?.statusCode == 200 {
                let json = JSON(data!)
                if let list = json.array {
                    var res:[T]? = []
                    
                    for subJson in list {
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

private func getBusinessObject<T: JSONDeserializable>(dummy: T, method: Method, uri: String)(token: String, callback: (T?)->Void) {
    var req = createURLRequest(uri, token, Method.GET)
    request(req)
        .responseJSON{ (request, response, data, error) in
            if error == nil && data != nil && response?.statusCode == 200 {
                let json = JSON(data!)
                let res = T(json: json)
                callback(res)
            } else {
                println(error)
                callback(nil)
            }
    }
}

private func sendBusinessObject<T: JSONSerializable>(dummy: T, uri: String)(obj: T, token: String) {
    var req = createURLRequest(uri, token, Method.POST)
    req.HTTPBody = (obj.toJson()).dataUsingEncoding(NSUTF8StringEncoding)
    request(req)
}

private func sendBusinessObjectTypeBased<T: JSONSerializable>(type: T.Type, uri: String)(obj: T, token: String) {
    var req = createURLRequest(uri, token, Method.POST)
    req.HTTPBody = (obj.toJson()).dataUsingEncoding(NSUTF8StringEncoding)
    request(req)
}

private func respondToMatch(response: String)(token: String, username: String) {
    sendBusinessObject(PotentialMatchResponse(), "/m/processMatchResponse")(obj: PotentialMatchResponse(username: username, status: response), token: token)
}

private func moreMessages()(token: String, matchID: String, callback: ([MatchMessage]?)->Void) {
    getList(MatchMessage(), Method.GET, "/ajax/getMoreMessages?matchID=" + matchID)(token: token, callback: callback)
}

private func createURLRequest(uri: String, token: String, httpMethod: Method, contentType: String = "application/json") -> NSMutableURLRequest {
    var req = NSMutableURLRequest(URL: NSURL(string: serverURI + uri)!)
    req.HTTPMethod = httpMethod.rawValue
    req.setValue(contentType, forHTTPHeaderField: "Content-Type")
    req.setValue(NSString(CString: token, encoding: NSUTF8StringEncoding), forHTTPHeaderField: "X-Auth-Token")
    return req
}

class Curried {
    let register = sendBusinessObject(Registration(), "/m/registerUser")
    let updateRegistration = sendBusinessObject(UpdateRegistration(), "/m/registerUser")
    let getMatches = getList(PreparedMatch(), Method.GET, "/m/matches")
    let getPotentialMatches = getList(PreparedPotentialMatch(), Method.GET, "/m/potentialMatches")
    let getProfileInformation = getBusinessObject(UpdateRegistration(), Method.GET, "/m/getProfileInformation")
    let reject = respondToMatch(REJECTED)
    let accept = respondToMatch(ACCEPTED)
    let sendMessage = sendBusinessObjectTypeBased(MatchMessage.self, "/messaging")
    let getMessages = moreMessages()

}




