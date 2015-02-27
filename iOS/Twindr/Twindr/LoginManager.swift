//
//  LoginManager.swift
//  Twindr
//
//  Created by Jordan on 2015-02-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import Accounts

class LoginManager {
    init(){
        SignIntoTwitter()
    }
    
    func SignIntoTwitter(){
        var accountStore = ACAccountStore()
        var accountType = accountStore.accountTypeWithAccountTypeIdentifier(ACAccountTypeIdentifierTwitter)
        
        accountStore.requestAccessToAccountsWithType(accountType, options: nil) {
            (granted, error) in
            println("got back: \(granted)")
        }
    }
}