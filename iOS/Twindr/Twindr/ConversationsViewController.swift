//
//  ConversationsViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-03-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import UIKit

class ConversationViewController: ViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var conversationTable: UITableView!
    let matchedUsers = ["Jordan", "Tim", "Evan","Morgan", "Caesar", "Brett","Jord", "Timothy", "Nave","Morg",
        "Junhyeok", "Bert","Nadroj", "Mit", "Vane","Nagrom", "Raseac", "Tterb", "Cole", "Sand", "Spearman",
        "Epp", "Kim", "Small", "Braico"]
    let textCellIdentifier = "TextCell"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        conversationTable.delegate = self
        conversationTable.dataSource = self
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.matchedUsers.count;
    }
    
    func tableView(conversationTable: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = conversationTable.dequeueReusableCellWithIdentifier(textCellIdentifier, forIndexPath: indexPath) as UITableViewCell
        cell.textLabel?.text = matchedUsers[indexPath.row]
        
        return cell
    }
    
    func tableView(conversationTable: UITableView!, didSelectRowAtIndexPath indexPath: NSIndexPath!) {
        conversationTable.deselectRowAtIndexPath(indexPath, animated: true)
        println(matchedUsers[indexPath.row])
    }
    
    func numberOfSectionsInTableView(conversationTable: UITableView) -> Int {
        return 1
    }
}