//
//  Message.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-03-27.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import JSQMessagesViewController

class Message : NSObject, JSQMessageData {
    
    var sender_ : String!
    var date_ : NSDate
    var text_ : String
    
    init(sender: String, text: String) {
        self.sender_ = sender
        self.text_ = text
        self.date_ = NSDate()
    }
    
    func senderId() -> String! {
        return sender_
    }
    
    func senderDisplayName() -> String! {
        return sender_
    }
    
    func date() -> NSDate! {
        return date_
    }
    
    func isMediaMessage() -> Bool {
        return false
    }
    
    func messageHash() -> UInt {
        var signed = rand()
        let unsigned = signed >= 0 ?
            UInt(signed) :
            UInt(signed  - Int.min) + UInt(Int.max) + 1
        return unsigned
    }
    
    func text() -> String! {
        return text_
    }
}