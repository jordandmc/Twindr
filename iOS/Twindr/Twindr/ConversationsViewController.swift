//
//  ConversationsViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-03-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import Foundation
import UIKit

class MatchedUserCell: UITableViewCell {
    
    @IBOutlet weak var FollowButton: UIButton!
    
    required init(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override init(style: UITableViewCellStyle, reuseIdentifier: String!) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
}

class ConversationsViewController: ViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView: UITableView!
    var matchedUsers: [PreparedMatch] = []
    let textCellIdentifier = "TextCell"
    
    @IBAction func FollowUser(sender: UIButton) {
        let selectedIndex: Int = sender.tag
        
        println(matchedUsers[selectedIndex].username)
        TwitterHelper.sendFollow(matchedUsers[selectedIndex].username)
        sender.backgroundColor = UIColor.redColor()
        sender.titleLabel?.text = "Unfollow"
        //sender.imageView?.image = UIImage("")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
        
        if let tkn = xAuthToken {
            Curried().getMatches(token: tkn, callback: loadedMatchesCallback)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return matchedUsers.count;
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(textCellIdentifier, forIndexPath: indexPath) as MatchedUserCell
        cell.textLabel?.text = matchedUsers[indexPath.row].username
        cell.FollowButton.tag = indexPath.row
        
        return cell
    }
    
    func tableView(tableView: UITableView!, didSelectRowAtIndexPath indexPath: NSIndexPath!) {
        converseWith = matchedUsers[indexPath.row]
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
    }
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView!, canEditRowAtIndexPath indexPath: NSIndexPath!) -> Bool {
        return true
    }
    
    func tableView(tableView: UITableView!, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath!) {
        if editingStyle == UITableViewCellEditingStyle.Delete {
            if let tkn = xAuthToken {
                unmatch(tkn, matchedUsers[indexPath.row].username)
            
                matchedUsers.removeAtIndex(indexPath.row)
                tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: UITableViewRowAnimation.Automatic)
            }
        }
    }
    
    private func loadedMatchesCallback(res: [PreparedMatch]?) {
        if let tempList = res {
            matchedUsers = tempList
            tableView.reloadData()
        }
    }
}