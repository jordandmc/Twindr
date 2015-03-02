//
//  LoginManager.swift
//  Twindr
//
//  Created by Jordan on 2015-02-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import Accounts

class TwitterManager {
    init() {
        SignIntoTwitter()
    }
    
    func SignIntoTwitter() {
        var account:ACAccountStore = ACAccountStore()
        var accountType:ACAccountType = account.accountTypeWithAccountTypeIdentifier(ACAccountTypeIdentifierTwitter)
        
        account.requestAccessToAccountsWithType(accountType, options: nil) {
            (granted, error) in
            if(granted) {
                var arrayOfAccounts = account.accountsWithAccountType(accountType)
                let requestAPI:NSURL = NSURL(string: "http://api.twitter.com/1.1/statuses/user_timeline.json")!
                
                println("Twitter accounts on device:")
                for item in arrayOfAccounts{
                    println("username: \(item.username)")
                    println("description: \(item.description)")
                    println()
                }
            }
            else {
                println("ERROR: \(error)")
            }
        }
    }
}