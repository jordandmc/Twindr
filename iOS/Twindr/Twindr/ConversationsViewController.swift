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
        sender.titleLabel?.text == " Follow" ? setButtonToUnfollow(sender) : setButtonToFollow(sender)
    }
    
    func setButtonToUnfollow(button: UIButton){
        let selectedIndex: Int = button.tag
        
        println("trying to follow " + matchedUsers[selectedIndex].username)
        TwitterHelper.sendFollow(matchedUsers[selectedIndex].username)
        button.backgroundColor = UIColor.redColor()
        button.setTitle(" Unfollow", forState: UIControlState.Normal)
        button.setImage(nil, forState: UIControlState.Normal)
    }
    
    func setButtonToFollow(button: UIButton){
        let selectedIndex: Int = button.tag
        
        println("trying to unfollow " + matchedUsers[selectedIndex].username)
        TwitterHelper.sendUnfollow(matchedUsers[selectedIndex].username)
        button.backgroundColor = UIColor(red: 0.2745, green: 0.6039, blue: 0.9176, alpha: 1.0)
        button.setTitle(" Follow", forState: UIControlState.Normal)
        button.setImage(UIImage(named: "Twitter_Logo_White.png"), forState: UIControlState.Normal)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
        
        if let tkn = xAuthToken {
            Curried().getMatches(token: tkn, callback: loadedMatchesCallback)
        }
        
        navigatedThroughButton ? println("button nav") : (navigatedThroughButton = false)
        
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
        //correctTwitterButtonState(cell.FollowButton)
        
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