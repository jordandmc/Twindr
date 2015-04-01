//
//  MessageHandler.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation

class MessageHandler {
    let source: EventSource
    let handler: (MatchMessage) -> Void
    
    init(matchID: String, token: String, handler: (MatchMessage) -> Void) {
        self.handler = handler
        let src = EventSource(url: serverURI + "/js/receiveMessage/" + matchID, token: token)
        self.source = src
    }
    
    func start() {
        source.onMessage(responseHandler)
    }
    
    private func responseHandler(event: Event!) {
        var jsonError: NSError?
        if let eventData = event.data {
            if let data = (eventData as NSString).dataUsingEncoding(NSUTF8StringEncoding) {
                let js = NSJSONSerialization.JSONObjectWithData(data, options: nil, error: &jsonError) as NSDictionary
                let json = JSON(js)
                let message = MatchMessage(json: json)
                if let msg = message {
                    handler(msg)
                }
            }
        }
    }
}