//
//  ConversationsViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-03-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import UIKit

class ConversationsViewController: ViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView: UITableView!
    let textCellIdentifier = "TextCell"
    
    var converseWith: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return matchedUsers.count;
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(textCellIdentifier, forIndexPath: indexPath) as UITableViewCell
        cell.textLabel?.text = matchedUsers[indexPath.row]
        
        return cell
    }
    
    func tableView(tableView: UITableView!, didSelectRowAtIndexPath indexPath: NSIndexPath!) {
        converseWith = matchedUsers[indexPath.row]
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        //var messageVC = self.storyboard?.instantiateViewControllerWithIdentifier("MessageViewController") as MessageViewController
        //messageVC.conversationWith = converseWith
        //self.showViewController(messageVC, sender: self)
    }
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView!, canEditRowAtIndexPath indexPath: NSIndexPath!) -> Bool {
        return true
    }
    
    func tableView(tableView: UITableView!, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath!) {
        if (editingStyle == UITableViewCellEditingStyle.Delete) {
            // CALL UNMATCHING LOGIC TO SERVER HERE
            
            matchedUsers.removeAtIndex(indexPath.row)
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: UITableViewRowAnimation.Automatic)
        }
    }
}