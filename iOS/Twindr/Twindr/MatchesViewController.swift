//
//  MatchesViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-02-12.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit

class MatchesViewController: ViewController {
    
    @IBOutlet weak var userLabel: UILabel!
    @IBOutlet weak var tweet1Label: UILabel!
    @IBOutlet weak var tweet2Label: UILabel!
    @IBOutlet weak var tweet3Label: UILabel!
    @IBOutlet weak var tweet4Label: UILabel!
    @IBOutlet weak var tweet5Label: UILabel!
    
    var potentialList: [PreparedPotentialMatch] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        setupToolbar()
        setupSwipeGestures()
        loadPotentialMatch()
    }
    
    override func viewWillDisappear(animated: Bool) {
        self.navigationController?.setToolbarHidden(true, animated: false)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setupToolbar() {
        self.navigationController?.setToolbarHidden(false, animated: false)
        self.navigationController?.toolbar.barTintColor = UIColor(red: CGFloat(39.0/255.0), green: CGFloat(174.0/255.0), blue: CGFloat(96.0/255.0), alpha: CGFloat(1.0))
        self.navigationController?.toolbar.tintColor = UIColor.whiteColor()
        self.navigationController?.toolbar.translucent = false
    }
    
    func setupSwipeGestures() {
        // Right -> Left
        let yesSwipe = UISwipeGestureRecognizer(target: self,
            action: "yesSwipe")
        yesSwipe.direction = .Left
        self.view.addGestureRecognizer(yesSwipe)
        
        // Left -> Right
        let noSwipe = UISwipeGestureRecognizer(target: self,
            action: "noSwipe")
        noSwipe.direction = .Right
        self.view.addGestureRecognizer(noSwipe)
        
    }
    
    // Decline to be matched with the user via button or swipe
    func dismissMatch() {
        if potentialList.count > 0 {
            if let tkn = xAuthToken {
                // send that you rejected them
                Curried().reject(token: tkn, username: potentialList[0].username)
            
                // load next potential match
                potentialList.removeAtIndex(0)
                loadPotentialMatch()
            }
        }
    }
    
    // Accept the match via button or swipe
    func acceptMatch() {
        if potentialList.count > 0 {
            if let tkn = xAuthToken {
                // follow that user
                
                // send that you matched with them
                Curried().accept(token: tkn, username: potentialList[0].username)
            
                // load next potential match
                potentialList.removeAtIndex(0)
                loadPotentialMatch()
            }
        }
    }
    
    // Loads the next potential match in the list or gets more potential matches
    private func loadPotentialMatch() {
        if potentialList.count == 0 {
            if let tkn = xAuthToken {
                Curried().getPotentialMatches(token: tkn, callback: potentialMatchesLoadedCallback)
            }
        }
        
        if potentialList.count > 0 {
            showNextMatch()
        } else {
            showNoMatchesAvailable()
        }
    }
    
    // Updates potential match list
    private func potentialMatchesLoadedCallback(res: [PreparedPotentialMatch]?) {
        if let tempList = res {
            potentialList = tempList
            
            if potentialList.count > 0 {
                showNextMatch()
            } else {
                showNoMatchesAvailable()
            }
        }
    }
    
    // Render the next potential match on the screen
    private func showNextMatch() {
        let potentialMatch = potentialList[0]
        userLabel.text = potentialMatch.username
        
        if potentialMatch.tweets.count >= 1 { tweet1Label.text = potentialMatch.tweets[0] } else { tweet1Label.text = "" }
        if potentialMatch.tweets.count >= 2 { tweet2Label.text = potentialMatch.tweets[1] } else { tweet2Label.text = "" }
        if potentialMatch.tweets.count >= 3 { tweet3Label.text = potentialMatch.tweets[2] } else { tweet3Label.text = "" }
        if potentialMatch.tweets.count >= 4 { tweet4Label.text = potentialMatch.tweets[3] } else { tweet4Label.text = "" }
        if potentialMatch.tweets.count >= 5 { tweet5Label.text = potentialMatch.tweets[4] } else { tweet5Label.text = "" }
    }
    
    // Render the no matches text on the screen
    private func showNoMatchesAvailable() {
        userLabel.text   = ""
        tweet1Label.text = ""
        tweet2Label.text = "We have no more potential matches at this time."
        tweet3Label.text = ""
        tweet4Label.text = ""
        tweet5Label.text = ""
    }
    
    @IBAction func noButton(sender: UIBarButtonItem) {
        dismissMatch()
    }

    @IBAction func yesButton(sender: UIBarButtonItem) {
        acceptMatch()
    }
    
    func yesSwipe() {
        acceptMatch()
    }
    
    func noSwipe() {
        dismissMatch()
    }
}
