//
//  MessageHandler.swift
//  Twindr
//
//  Created by Evan Spearman on 2015-03-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import EventSource

class MessageHandler {
    let source: EventSource
    let handler: (MatchMessage) -> Void
    
    init(matchID: String, handler: (MatchMessage) -> Void){
        self.handler = handler
        let src = EventSource(url: serverURI + "/js/receiveMessage/" + matchID, token: xAuthToken!)
        self.source = src
    }
    
    func start() {
        source.onMessage(responseHandler)
    }
    
    private func responseHandler(event: Event!) {
        let json = JSON(event.data!)
        let message = MatchMessage(json: json)
        if let msg = message {
            handler(msg)
        }
    }
}